package hundsun.pdpm.modules.datapermission.service.impl;

import hundsun.pdpm.modules.datapermission.domain.DataPermission;
import hundsun.pdpm.modules.datapermission.domain.DataPermissionField;
import hundsun.pdpm.modules.datapermission.repository.DataPermissionRepository;
import hundsun.pdpm.modules.system.service.DictDetailService;
import hundsun.pdpm.modules.system.service.dto.RoleSmallDTO;
import org.springframework.util.CollectionUtils;
import hundsun.pdpm.utils.ValidationUtil;
import hundsun.pdpm.utils.FileUtil;
import hundsun.pdpm.modules.datapermission.repository.DataPermissionFieldRepository;
import hundsun.pdpm.modules.datapermission.service.DataPermissionFieldService;
import hundsun.pdpm.modules.datapermission.service.dto.DataPermissionFieldDTO;
import hundsun.pdpm.modules.datapermission.service.dto.DataPermissionFieldQueryCriteria;
import hundsun.pdpm.modules.datapermission.service.mapper.DataPermissionFieldMapper;
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
* @date 2019-12-13
*/
@Service
@CacheConfig(cacheNames = "dataPermissionField")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DataPermissionFieldServiceImpl implements DataPermissionFieldService {

    private final DataPermissionFieldRepository dataPermissionFieldRepository;

    private final DataPermissionFieldMapper dataPermissionFieldMapper;

    @Autowired
    private DataPermissionRepository dataPermissionRepository;

    @Autowired
    private DictDetailService dictDetailService;

    public DataPermissionFieldServiceImpl(DataPermissionFieldRepository dataPermissionFieldRepository, DataPermissionFieldMapper dataPermissionFieldMapper) {
        this.dataPermissionFieldRepository = dataPermissionFieldRepository;
        this.dataPermissionFieldMapper = dataPermissionFieldMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(DataPermissionFieldQueryCriteria criteria, Pageable pageable){
        Page<DataPermissionField> page = dataPermissionFieldRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(dataPermissionFieldMapper::toDto));
    }

    @Override
    @Cacheable
    public List<DataPermissionFieldDTO> queryAll(DataPermissionFieldQueryCriteria criteria){
        return dataPermissionFieldMapper.toDto(dataPermissionFieldRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public DataPermissionFieldDTO findById(String id) {
        DataPermissionField dataPermissionField = dataPermissionFieldRepository.findById(id).orElseGet(DataPermissionField::new);
        ValidationUtil.isNull(dataPermissionField.getId(),"DataPermissionField","id",id);
        return dataPermissionFieldMapper.toDto(dataPermissionField);
    }

    @Override
    @Cacheable(key = "#p0")
    public List<DataPermissionFieldDTO> findByPermissionId(String permissionId) {
        DataPermission dataPermission = new DataPermission();
        dataPermission.setId(permissionId);
        List<DataPermission> list = new ArrayList<>();
        list.add(dataPermission);
        return dataPermissionFieldMapper.toDto(dataPermissionFieldRepository.findAllByTableIn(list));
    }




    @Override
    @Cacheable
    public List<DataPermissionFieldDTO> findByIdlist(List<DataPermissionFieldDTO> dataPermissionFieldList) {
        if (CollectionUtils.isEmpty(dataPermissionFieldList)){
        return  new ArrayList<>();
        }
        List<String> idlist = dataPermissionFieldList.stream().map(DataPermissionFieldDTO::getId).collect(Collectors.toList());
        return dataPermissionFieldMapper.toDto(dataPermissionFieldRepository.findAllByIdIn(idlist));
    }


    @Override
    public List<DataPermissionFieldDTO> findByRoleId(List<RoleSmallDTO> roleId,String tablecode) {
        List<DataPermissionFieldDTO> fieldDTOList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(roleId)){
            List<Long> idList = roleId.stream().map(RoleSmallDTO::getId).collect(Collectors.toList());
            List<DataPermission> dataPermissionList = dataPermissionRepository.findAllByRoleIdInAndTableCode(idList,tablecode);
           // List<String> tableIdlist = dataPermissionList.stream().map(DataPermission::getId).collect(Collectors.toList());
            List<DataPermissionField> dataPermissionFieldList = dataPermissionFieldRepository.findAllByTableIn(dataPermissionList);
            fieldDTOList = dataPermissionFieldMapper.toDto(dataPermissionFieldList);
        }
        return fieldDTOList;
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public DataPermissionFieldDTO create(DataPermissionField resources) {
        resources.setId(StringUtils.get32UUID());
        return dataPermissionFieldMapper.toDto(dataPermissionFieldRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(DataPermissionField resources) {
        DataPermissionField dataPermissionField = dataPermissionFieldRepository.findById(resources.getId()).orElseGet(DataPermissionField::new);
        ValidationUtil.isNull( dataPermissionField.getId(),"DataPermissionField","id",resources.getId());
        dataPermissionField.copy(resources);
        dataPermissionFieldRepository.save(dataPermissionField);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        dataPermissionFieldRepository.deleteById(id);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void deleteByTableId(String tableId) {
        DataPermission dataPermission = new DataPermission();
        dataPermission.setId(tableId);
        dataPermissionFieldRepository.deleteAllByTable(dataPermission);
    }

    @Override
    public void download(List<DataPermissionFieldDTO> all, HttpServletResponse response) throws IOException {
        Map<String, List<DictDetail>> dictMap = dictDetailService.queryAll(DataPermissionFieldDTO.class);
        ExcelHelper.exportExcel(response,all,dictMap,DataPermissionFieldDTO.class,false);
    }
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public List<DataPermissionFieldDTO> upload(MultipartFile multipartFiles) throws Exception {
       Map<String, List<DictDetail>> dictMap = dictDetailService.queryAll(DataPermissionFieldDTO.class);
       List<DataPermissionFieldDTO> data = ExcelHelper.importExcel(multipartFiles,DataPermissionFieldDTO.class,dictMap,false);
       if(!CollectionUtils.isEmpty(data)){
          List<DataPermissionField> savelist =  new ArrayList<>();
          List<String> idlist = new ArrayList<>();
          for(DataPermissionFieldDTO dataPermissionFieldDTO:data){
             if(!StringUtils.isEmpty(dataPermissionFieldDTO.getId())){
                    //删除库中
                 idlist.add(dataPermissionFieldDTO.getId());
             }else {
                 dataPermissionFieldDTO.setId(StringUtils.get32UUID());
             }
             savelist.add(dataPermissionFieldMapper.toEntity(dataPermissionFieldDTO));
          }
       dataPermissionFieldRepository.deleteAllByIdIn(idlist);
       dataPermissionFieldRepository.saveAll(savelist);
       }
        return  data;
     }
}
