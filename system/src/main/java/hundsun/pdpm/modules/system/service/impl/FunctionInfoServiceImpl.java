package hundsun.pdpm.modules.system.service.impl;

import hundsun.pdpm.modules.system.domain.FunctionInfo;
import hundsun.pdpm.modules.datapermission.utils.PermissionUtils;
import hundsun.pdpm.modules.system.service.DictDetailService;
import org.springframework.util.CollectionUtils;
import hundsun.pdpm.utils.ValidationUtil;
import hundsun.pdpm.utils.FileUtil;
import hundsun.pdpm.modules.system.repository.FunctionInfoRepository;
import hundsun.pdpm.modules.system.service.FunctionInfoService;
import hundsun.pdpm.modules.system.service.dto.FunctionInfoDTO;
import hundsun.pdpm.modules.system.service.dto.FunctionInfoQueryCriteria;
import hundsun.pdpm.modules.system.service.mapper.FunctionInfoMapper;
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
* @date 2019-12-17
*/
@Service
@CacheConfig(cacheNames = "functionInfo")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FunctionInfoServiceImpl implements FunctionInfoService {

    private final FunctionInfoRepository functionInfoRepository;

    private final FunctionInfoMapper functionInfoMapper;

    @Autowired
    private DictDetailService dictDetailService;

    public FunctionInfoServiceImpl(FunctionInfoRepository functionInfoRepository, FunctionInfoMapper functionInfoMapper) {
        this.functionInfoRepository = functionInfoRepository;
        this.functionInfoMapper = functionInfoMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(FunctionInfoQueryCriteria criteria, Pageable pageable){
        Page<FunctionInfo> page = functionInfoRepository.findAll((root, criteriaQuery, criteriaBuilder) ->
                                   PermissionUtils.getPredicate(root,
                                       QueryHelp.getPredicate(root,criteria,criteriaBuilder),
                                       criteriaBuilder,FunctionInfoDTO.class),pageable);
        return PageUtil.toPage(page.map(functionInfoMapper::toDto));
    }

    @Override
    @Cacheable
    public List<FunctionInfoDTO> queryAll(FunctionInfoQueryCriteria criteria){
        return functionInfoMapper.toDto(functionInfoRepository.findAll((root, criteriaQuery, criteriaBuilder) ->
                                                PermissionUtils.getPredicate(root,
                                                QueryHelp.getPredicate(root,criteria,criteriaBuilder),
                                                criteriaBuilder,FunctionInfoDTO.class)));
    }

    @Override
    @Cacheable(key = "#p0")
    public FunctionInfoDTO findById(String id) {
        FunctionInfo functionInfo = functionInfoRepository.findById(id).orElseGet(FunctionInfo::new);
        ValidationUtil.isNull(functionInfo.getId(),"FunctionInfo","id",id);
        return functionInfoMapper.toDto(functionInfo);
    }
    @Override
    @Cacheable
    public List<FunctionInfoDTO> findByIdlist(List<FunctionInfoDTO> functionInfoList) {
        if (CollectionUtils.isEmpty(functionInfoList)){
        return  new ArrayList<>();
        }
        List<String> idlist = functionInfoList.stream().map(FunctionInfoDTO::getId).collect(Collectors.toList());
        return functionInfoMapper.toDto(functionInfoRepository.findAllByIdIn(idlist));
    }
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public FunctionInfoDTO create(FunctionInfo resources) {
        resources.setId(StringUtils.get32UUID());
        return functionInfoMapper.toDto(functionInfoRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(FunctionInfo resources) {
        FunctionInfo functionInfo = functionInfoRepository.findById(resources.getId()).orElseGet(FunctionInfo::new);
        ValidationUtil.isNull( functionInfo.getId(),"FunctionInfo","id",resources.getId());
        functionInfo.copy(resources);
        functionInfoRepository.save(functionInfo);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        functionInfoRepository.deleteById(id);
    }


    @Override
    public void download(List<FunctionInfoDTO> all, HttpServletResponse response) throws IOException {
        Map<String, List<DictDetail>> dictMap = dictDetailService.queryAll(FunctionInfoDTO.class);
        ExcelHelper.exportExcel(all,dictMap,FunctionInfoDTO.class,false);
    }
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public List<FunctionInfoDTO> upload(MultipartFile multipartFiles) throws Exception {
       Map<String, List<DictDetail>> dictMap = dictDetailService.queryAll(FunctionInfoDTO.class);
       List<FunctionInfoDTO> data = ExcelHelper.importExcel(multipartFiles,FunctionInfoDTO.class,dictMap,false);
       if(!CollectionUtils.isEmpty(data)){
          List<FunctionInfo> savelist =  new ArrayList<>();
          List<String> idlist = new ArrayList<>();
          for(FunctionInfoDTO functionInfoDTO:data){
             if(!StringUtils.isEmpty(functionInfoDTO.getId())){
                    //删除库中
                 idlist.add(functionInfoDTO.getId());
             }else {
                 functionInfoDTO.setId(StringUtils.get32UUID());
             }
             savelist.add(functionInfoMapper.toEntity(functionInfoDTO));
          }
       functionInfoRepository.deleteAllByIdIn(idlist);
       functionInfoRepository.saveAll(savelist);
       }
        return  data;
     }
}
