package hundsun.pdpm.modules.datapermission.service.impl;

import com.alibaba.fastjson.JSON;
import hundsun.pdpm.annotation.PermissionField;
import hundsun.pdpm.annotation.PermissionObject;
import hundsun.pdpm.modules.datapermission.domain.DataPermission;
import hundsun.pdpm.modules.datapermission.domain.DataPermissionField;
import hundsun.pdpm.modules.datapermission.service.ClassScaner;
import hundsun.pdpm.modules.datapermission.service.DataPermissionFieldService;
import hundsun.pdpm.modules.datapermission.service.dto.DataPermissionFieldDTO;
import hundsun.pdpm.modules.datapermission.utils.PermissionUtils;
import hundsun.pdpm.modules.monitor.service.RedisService;
import hundsun.pdpm.modules.system.service.DictDetailService;
import hundsun.pdpm.modules.system.service.RoleService;
import hundsun.pdpm.modules.system.service.dto.RoleSmallDTO;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.util.CollectionUtils;
import hundsun.pdpm.utils.ValidationUtil;
import hundsun.pdpm.utils.FileUtil;
import hundsun.pdpm.modules.datapermission.repository.DataPermissionRepository;
import hundsun.pdpm.modules.datapermission.service.DataPermissionService;
import hundsun.pdpm.modules.datapermission.service.dto.DataPermissionDTO;
import hundsun.pdpm.modules.datapermission.service.dto.DataPermissionQueryCriteria;
import hundsun.pdpm.modules.datapermission.service.mapper.DataPermissionMapper;
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

import java.lang.reflect.Field;
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
@CacheConfig(cacheNames = "dataPermission")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DataPermissionServiceImpl implements DataPermissionService {

    private final DataPermissionRepository dataPermissionRepository;

    private final DataPermissionMapper dataPermissionMapper;

    @Autowired
    private DataPermissionFieldService dataPermissionFieldService;

    @Autowired
    private DictDetailService dictDetailService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RedisService redisService;

    public DataPermissionServiceImpl(DataPermissionRepository dataPermissionRepository, DataPermissionMapper dataPermissionMapper) {
        this.dataPermissionRepository = dataPermissionRepository;
        this.dataPermissionMapper = dataPermissionMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(DataPermissionQueryCriteria criteria, Pageable pageable){
        Page<DataPermission> page = dataPermissionRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(dataPermissionMapper::toDto));
    }

    @Override
    @Cacheable
    public List<DataPermissionDTO> queryAll(DataPermissionQueryCriteria criteria){
        return dataPermissionMapper.toDto(dataPermissionRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public DataPermissionDTO findById(String id) {
        DataPermission dataPermission = dataPermissionRepository.findById(id).orElseGet(DataPermission::new);
        ValidationUtil.isNull(dataPermission.getId(),"DataPermission","id",id);
        return dataPermissionMapper.toDto(dataPermission);
    }
    @Override
    @Cacheable
    public List<DataPermissionDTO> findByIdlist(List<DataPermissionDTO> dataPermissionList) {
        if (CollectionUtils.isEmpty(dataPermissionList)){
        return  new ArrayList<>();
        }
        List<String> idlist = dataPermissionList.stream().map(DataPermissionDTO::getId).collect(Collectors.toList());
        return dataPermissionMapper.toDto(dataPermissionRepository.findAllByIdIn(idlist));
    }
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public DataPermissionDTO create(DataPermission resources) {
        String tableId = StringUtils.get32UUID();
        resources.setId(tableId);
        //存储表
        DataPermission dataPermission = new DataPermission();
        dataPermission.setId(resources.getId());
        dataPermission.setName(resources.getName());
        dataPermission.setRoleId(resources.getRoleId());
        dataPermission.setTableCode(resources.getTableCode());
        dataPermission.setTableName(resources.getTableName());
        DataPermissionDTO dto = dataPermissionMapper.toDto(dataPermissionRepository.save(dataPermission));
        //存储字段
        saveField(resources,tableId);
        //清除缓存
        redisService.deleteByKey(resources.getTableCode());
        return dto;
    }

    private  void saveField(DataPermission resources,String tableId){
        //存储字段
        if(!CollectionUtils.isEmpty(resources.getFields())){
            resources.getFields().forEach(field->{
                DataPermission table = new DataPermission();
                table.setId(tableId);
                field.setTable(table);
                dataPermissionFieldService.create(field);
            });
        }
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(DataPermission resources) {
           dataPermissionRepository.save(resources);
           //删除字段
           dataPermissionFieldService.deleteByTableId(resources.getId());
           //存储字段
            saveField(resources,resources.getId());
           //清除缓存
           redisService.deleteByKey(resources.getTableCode());
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        dataPermissionRepository.deleteById(id);
        redisService.deleteAll();
    }


    @Override
    public void download(List<DataPermissionDTO> all, HttpServletResponse response) throws IOException {
        Map<String, List<DictDetail>> dictMap = dictDetailService.queryAll(DataPermissionDTO.class);
        ExcelHelper.exportExcel(response,all,dictMap,DataPermissionDTO.class,false);
    }
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public List<DataPermissionDTO> upload(MultipartFile multipartFiles) throws Exception {
       Map<String, List<DictDetail>> dictMap = dictDetailService.queryAll(DataPermissionDTO.class);
       List<DataPermissionDTO> data = ExcelHelper.importExcel(multipartFiles,DataPermissionDTO.class,dictMap,false);
       if(!CollectionUtils.isEmpty(data)){
          List<DataPermission> savelist =  new ArrayList<>();
          List<String> idlist = new ArrayList<>();
          for(DataPermissionDTO dataPermissionDTO:data){
             if(!StringUtils.isEmpty(dataPermissionDTO.getId())){
                    //删除库中
                 idlist.add(dataPermissionDTO.getId());
             }else {
                 dataPermissionDTO.setId(StringUtils.get32UUID());
             }
             savelist.add(dataPermissionMapper.toEntity(dataPermissionDTO));
          }
       dataPermissionRepository.deleteAllByIdIn(idlist);
       dataPermissionRepository.saveAll(savelist);
       }
        return  data;
     }


    @Override
    public List<DataPermissionFieldDTO> getFieldByRoleIdAndTableCode(Class clzz) {
        List<RoleSmallDTO> roles =  roleService.findByUsers_Id(SecurityUtils.getUserId());
        PermissionObject permissionObject = (PermissionObject)clzz.getAnnotation(PermissionObject.class);
        if(permissionObject == null){
            return  new ArrayList<>();
        }
        String tablecode =  permissionObject.tablecode();
        return  dataPermissionFieldService.findByRoleId(roles,tablecode);
    }

    @Override
    public Map<String, Object> permission(Map<String,Object> data, Class clazz) {
        Object obj = data.get("content");
        if(obj != null){
           List<Object> objects = (List<Object>) obj;
           List<Object> objectList = new ArrayList<>();
           List<DataPermissionFieldDTO>  fieldDTOS = getFieldByRoleIdAndTableCode(clazz);
           if(!CollectionUtils.isEmpty(fieldDTOS)){
               for(Object object :objects){
                   if(PermissionUtils.isNotLimit(object,fieldDTOS)){
                       objectList.add(object);
                   }
               }
               data.put("content",objectList);
           }
        }

        return data;
    }


    private   DataPermissionDTO getSet(Class clzz){
        DataPermissionDTO  data  = new DataPermissionDTO();
        PermissionObject permissionObject = (PermissionObject)clzz.getAnnotation(PermissionObject.class);
        if (permissionObject != null){
            data.setTableCode(permissionObject.tablecode());
            data.setTableName(permissionObject.tablename());
            data.setClassName(clzz.getName());
            List<DataPermissionFieldDTO> fieldDtoList = new ArrayList<>();
            data.setFields(fieldDtoList);
            Field[] fields = clzz.getDeclaredFields();
            for (Field field:fields){
                PermissionField permissionField = field.getAnnotation(PermissionField.class);
                if(permissionField != null){
                    DataPermissionFieldDTO fieldDto = new DataPermissionFieldDTO();
                    fieldDto.setFieldCode(permissionField.fieldcode());
                    fieldDto.setFieldName(permissionField.fieldname());
                    fieldDto.setDictName(permissionField.dictname());
                    fieldDtoList.add(fieldDto);
                }
            }
        }

        return data;
    }

    @Override
    public List<DataPermissionDTO> scan(String packageName) {
        List<DataPermissionDTO> data = new ArrayList<>();
        /*获取所有加这个注解的类*/
        ClassScaner.scan(packageName, PermissionObject.class)
                .stream().forEach((clazz)->{ data.add(getSet(clazz)); });

        return  data;
    }
}
