package hundsun.pdpm.modules.deliverconfig.service.impl;

import hundsun.pdpm.modules.deliverconfig.domain.DeliverConfig;
import hundsun.pdpm.modules.datapermission.utils.PermissionUtils;
import hundsun.pdpm.modules.deliverconfig.service.dto.FunctionNoDTO;
import hundsun.pdpm.modules.system.service.DictDetailService;
import hundsun.pdpm.modules.system.service.dto.DeliveryDTO;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.util.CollectionUtils;
import hundsun.pdpm.utils.ValidationUtil;
import hundsun.pdpm.utils.FileUtil;
import hundsun.pdpm.modules.deliverconfig.repository.DeliverConfigRepository;
import hundsun.pdpm.modules.deliverconfig.service.DeliverConfigService;
import hundsun.pdpm.modules.deliverconfig.service.dto.DeliverConfigDTO;
import hundsun.pdpm.modules.deliverconfig.service.dto.DeliverConfigQueryCriteria;
import hundsun.pdpm.modules.deliverconfig.service.mapper.DeliverConfigMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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

import java.io.File;
import java.util.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;
import hundsun.pdpm.utils.*;
/**
* @author yantt
* @date 2020-02-22
*/
@Service
@CacheConfig(cacheNames = "deliverConfig")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DeliverConfigServiceImpl implements DeliverConfigService {

    private final DeliverConfigRepository deliverConfigRepository;

    private final DeliverConfigMapper deliverConfigMapper;

    @Autowired
    private DictDetailService dictDetailService;

    public DeliverConfigServiceImpl(DeliverConfigRepository deliverConfigRepository, DeliverConfigMapper deliverConfigMapper) {
        this.deliverConfigRepository = deliverConfigRepository;
        this.deliverConfigMapper = deliverConfigMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(DeliverConfigQueryCriteria criteria, Pageable pageable){
        Page<DeliverConfig> page = deliverConfigRepository.findAll((root, criteriaQuery, criteriaBuilder) ->
                                   PermissionUtils.getPredicate(root,
                                       QueryHelp.getPredicate(root,criteria,criteriaBuilder),
                                       criteriaBuilder,DeliverConfigDTO.class),pageable);
        return PageUtil.toPage(page.map(deliverConfigMapper::toDto));
    }

    @Override
    @Cacheable
    public List<DeliverConfigDTO> queryAll(DeliverConfigQueryCriteria criteria){
        return deliverConfigMapper.toDto(deliverConfigRepository.findAll((root, criteriaQuery, criteriaBuilder) ->
                                                PermissionUtils.getPredicate(root,
                                                QueryHelp.getPredicate(root,criteria,criteriaBuilder),
                                                criteriaBuilder,DeliverConfigDTO.class)));
    }

    @Override
    @Cacheable(key = "#p0")
    public DeliverConfigDTO findById(Integer id) {
        DeliverConfig deliverConfig = deliverConfigRepository.findById(id).orElseGet(DeliverConfig::new);
        ValidationUtil.isNull(deliverConfig.getId(),"DeliverConfig","id",id);
        return deliverConfigMapper.toDto(deliverConfig);
    }
    @Override
    @Cacheable
    public List<DeliverConfigDTO> findByIdlist(List<DeliverConfigDTO> deliverConfigList) {
        if (CollectionUtils.isEmpty(deliverConfigList)){
        return  new ArrayList<>();
        }
        List<Integer> idlist = deliverConfigList.stream().map(DeliverConfigDTO::getId).collect(Collectors.toList());
        return deliverConfigMapper.toDto(deliverConfigRepository.findAllByIdIn(idlist));
    }
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public DeliverConfigDTO create(DeliverConfig resources) {
        return deliverConfigMapper.toDto(deliverConfigRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(DeliverConfig resources) {
        DeliverConfig deliverConfig = deliverConfigRepository.findById(resources.getId()).orElseGet(DeliverConfig::new);
        ValidationUtil.isNull( deliverConfig.getId(),"DeliverConfig","id",resources.getId());
        deliverConfig.copy(resources);
        deliverConfigRepository.save(deliverConfig);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        deliverConfigRepository.deleteById(id);
    }


    @Override
    public Map<String,FunctionNoDTO> getConfigData(String productId) {
        String username = SecurityUtils.getUsername();
        Map<String,FunctionNoDTO> data =  new HashMap<>();
        DeliverConfigQueryCriteria queryCriteria = new DeliverConfigQueryCriteria();
        queryCriteria.setProductId(productId);
        queryCriteria.setUsername(username);
        List<DeliverConfigDTO> deliverConfigs = queryAll(queryCriteria);
        if(!CollectionUtils.isEmpty(deliverConfigs)){
            DeliverConfigDTO deliverConfig  = deliverConfigs.get(0);
            String sFilePath = deliverConfig.getAddress();
            File file = new File(sFilePath);
            if(file.exists() && file.canRead() && file.isFile() && (file.getName().endsWith(".xlsx") || file.getName().endsWith(".xls"))){
                Map<String, List<DictDetail>> dictMap = dictDetailService.queryAll(FunctionNoDTO.class);
                try {
                  List<FunctionNoDTO>  result = ExcelHelper.importExcel(FunctionNoDTO.class,sFilePath,dictMap,false);
                  if(!CollectionUtils.isEmpty(result)){
                      BeanCopier beanCopier = BeanCopier.create(FunctionNoDTO.class, FunctionNoDTO.class, false);
                      result.forEach((item)->{
                           if(!StringUtils.isEmpty(item.getFunctionScript())){
                               String[] scripts = item.getFunctionScript().split("\n");
                               for(String script : scripts){
                                   FunctionNoDTO functionNoDTO = new FunctionNoDTO();
                                   beanCopier.copy(item,functionNoDTO,null);
                                   data.put(script,functionNoDTO);
                               }
                           }
                      });
                  }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }
        return data;
    }
}
