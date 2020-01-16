package hundsun.pdpm.modules.system.service.impl;

import hundsun.pdpm.modules.datapermission.utils.PermissionUtils;
import hundsun.pdpm.modules.system.domain.BusinessInfo;
import hundsun.pdpm.modules.system.service.DictDetailService;
import org.springframework.util.CollectionUtils;
import hundsun.pdpm.utils.ValidationUtil;
import hundsun.pdpm.utils.FileUtil;
import hundsun.pdpm.modules.system.repository.BusinessInfoRepository;
import hundsun.pdpm.modules.system.service.BusinessInfoService;
import hundsun.pdpm.modules.system.service.dto.BusinessInfoDTO;
import hundsun.pdpm.modules.system.service.dto.BusinessInfoQueryCriteria;
import hundsun.pdpm.modules.system.service.mapper.BusinessInfoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import cn.hutool.core.util.IdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import hundsun.pdpm.modules.execl.ExcelHelper;
import hundsun.pdpm.modules.system.domain.DictDetail;
import org.springframework.web.multipart.MultipartFile;
import hundsun.pdpm.utils.PageUtil;
import hundsun.pdpm.utils.QueryHelp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;
import hundsun.pdpm.utils.*;
/**
* @author yantt
* @date 2019-12-09
*/
@Service
@CacheConfig(cacheNames = "businessInfo")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class BusinessInfoServiceImpl implements BusinessInfoService {

    private final BusinessInfoRepository businessInfoRepository;

    private final BusinessInfoMapper businessInfoMapper;

    @Autowired
    private DictDetailService dictDetailService;

    public BusinessInfoServiceImpl(BusinessInfoRepository businessInfoRepository, BusinessInfoMapper businessInfoMapper) {
        this.businessInfoRepository = businessInfoRepository;
        this.businessInfoMapper = businessInfoMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(BusinessInfoQueryCriteria criteria, Pageable pageable){
        Page<BusinessInfo> page = businessInfoRepository.findAll((root, criteriaQuery, criteriaBuilder) ->
                PermissionUtils.getPredicate(root,
                        QueryHelp.getPredicate(root,criteria,criteriaBuilder),
                        criteriaBuilder,
                        BusinessInfoDTO.class),pageable);
        return PageUtil.toPage(page.map(businessInfoMapper::toDto));
    }

    @Override
    @Cacheable
    public List<BusinessInfoDTO> queryAll(BusinessInfoQueryCriteria criteria){
        return businessInfoMapper.toDto(businessInfoRepository.findAll((root, criteriaQuery, criteriaBuilder) ->
                PermissionUtils.getPredicate(root,
                        QueryHelp.getPredicate(root,criteria,criteriaBuilder),
                        criteriaBuilder,
                        BusinessInfoDTO.class)));
    }

    @Override
    @Cacheable(key = "#p0")
    public BusinessInfoDTO findById(String id) {
        BusinessInfo businessInfo = businessInfoRepository.findById(id).orElseGet(BusinessInfo::new);
        ValidationUtil.isNull(businessInfo.getId(),"BusinessInfo","id",id);
        return businessInfoMapper.toDto(businessInfo);
    }
    @Override
    @Cacheable
    public List<BusinessInfoDTO> findByIdlist(List<BusinessInfoDTO> businessInfoList) {
        if (CollectionUtils.isEmpty(businessInfoList)){
        return  new ArrayList<>();
        }
        List<String> idlist = businessInfoList.stream().map(BusinessInfoDTO::getId).collect(Collectors.toList());
        return businessInfoMapper.toDto(businessInfoRepository.findAllByIdIn(idlist));
    }
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public BusinessInfoDTO create(BusinessInfo resources) {
        resources.setId(StringUtils.get32UUID());
        return businessInfoMapper.toDto(businessInfoRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(BusinessInfo resources) {
        BusinessInfo businessInfo = businessInfoRepository.findById(resources.getId()).orElseGet(BusinessInfo::new);
        ValidationUtil.isNull( businessInfo.getId(),"BusinessInfo","id",resources.getId());
        businessInfo.copy(resources);
        businessInfoRepository.save(businessInfo);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        businessInfoRepository.deleteById(id);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<String> id) {
        businessInfoRepository.deleteAllByIdIn(id);
    }

    @Override
    public void download(List<BusinessInfoDTO> all, HttpServletResponse response) throws IOException {
        Map<String, List<DictDetail>> dictMap = dictDetailService.queryAll(BusinessInfoDTO.class);
        ExcelHelper.exportExcel(response,all,dictMap,BusinessInfoDTO.class,false);
    }
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public List<BusinessInfoDTO> upload(MultipartFile multipartFiles) throws Exception {
       Map<String, List<DictDetail>> dictMap = dictDetailService.queryAll(BusinessInfoDTO.class);
       List<BusinessInfoDTO> data = ExcelHelper.importExcel(multipartFiles,BusinessInfoDTO.class,dictMap,false);
       if(!CollectionUtils.isEmpty(data)){
          List<BusinessInfo> savelist =  new ArrayList<>();
          List<String> idlist = new ArrayList<>();
          for(BusinessInfoDTO businessInfoDTO:data){
             if(!StringUtils.isEmpty(businessInfoDTO.getId())){
                    //删除库中
                 idlist.add(businessInfoDTO.getId());
             }else {
                 businessInfoDTO.setId(StringUtils.get32UUID());
             }
             savelist.add(businessInfoMapper.toEntity(businessInfoDTO));
          }
       businessInfoRepository.deleteAllByIdIn(idlist);
       businessInfoRepository.saveAll(savelist);
       }
        return  data;
     }
}
