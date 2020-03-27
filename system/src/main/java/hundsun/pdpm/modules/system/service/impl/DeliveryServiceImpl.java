package hundsun.pdpm.modules.system.service.impl;

import hundsun.pdpm.modules.execl.ExcelUtils;
import hundsun.pdpm.modules.system.domain.*;
import hundsun.pdpm.modules.datapermission.utils.PermissionUtils;
import hundsun.pdpm.modules.system.repository.CustomerRepository;
import hundsun.pdpm.modules.system.repository.FunctionInfoRepository;
import hundsun.pdpm.modules.system.repository.ScriptInfoRepository;
import hundsun.pdpm.modules.system.service.*;
import hundsun.pdpm.modules.system.service.dto.*;
import hundsun.pdpm.modules.system.service.mapper.FunctionInfoMapper;
import hundsun.pdpm.modules.system.service.mapper.ScriptInfoMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.CollectionUtils;
import hundsun.pdpm.utils.ValidationUtil;
import hundsun.pdpm.modules.system.repository.DeliveryRepository;
import hundsun.pdpm.modules.system.service.mapper.DeliveryMapper;
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
import org.springframework.web.multipart.MultipartFile;
import hundsun.pdpm.utils.PageUtil;
import hundsun.pdpm.utils.QueryHelp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.stream.Collectors;
import hundsun.pdpm.utils.*;
/**
* @author yantt
* @date 2019-12-25
*/
@Service
@CacheConfig(cacheNames = "delivery")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DeliveryServiceImpl implements DeliveryService {

    private static  final String SPLIT_CHAR = "\n";

    private final DeliveryRepository deliveryRepository;

    private final DeliveryMapper deliveryMapper;

    @Autowired
    private DictDetailService dictDetailService;

    @Autowired
    private FunctionInfoRepository functionInfoRepository;

    @Autowired
    private FunctionInfoService functionInfoService;

    @Autowired
    private FunctionInfoMapper functionInfoMapper;

    @Autowired
    private ScriptInfoRepository scriptInfoRepository;

    @Autowired
    private ScriptInfoService scriptInfoService;

    @Autowired
    private ScriptInfoMapper scriptInfoMapper;


    @Autowired
    private FunctionScriptService functionScriptService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerService customerService;

    public DeliveryServiceImpl(DeliveryRepository deliveryRepository, DeliveryMapper deliveryMapper) {
        this.deliveryRepository = deliveryRepository;
        this.deliveryMapper = deliveryMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(DeliveryQueryCriteria criteria, Pageable pageable){
        Page<Delivery> page = deliveryRepository.findAll((root, criteriaQuery, criteriaBuilder) ->
                                   PermissionUtils.getPredicate(root,
                                       QueryHelp.getPredicate(root,criteria,criteriaBuilder),
                                       criteriaBuilder,DeliveryDTO.class),pageable);
        return PageUtil.toPage(page.map(deliveryMapper::toDto));
    }

    @Override
    @Cacheable
    public List<DeliveryDTO> queryAll(DeliveryQueryCriteria criteria){
        return deliveryMapper.toDto(deliveryRepository.findAll((root, criteriaQuery, criteriaBuilder) ->
                                                PermissionUtils.getPredicate(root,
                                                QueryHelp.getPredicate(root,criteria,criteriaBuilder),
                                                criteriaBuilder,DeliveryDTO.class)));
    }

    @Override
    @Cacheable(key = "#p0")
    public DeliveryDTO findById(String id) {
        Delivery delivery = deliveryRepository.findById(id).orElseGet(Delivery::new);
        ValidationUtil.isNull(delivery.getId(),"Delivery","id",id);
        return deliveryMapper.toDto(delivery);
    }
    @Override
    @Cacheable
    public List<DeliveryDTO> findByIdlist(List<DeliveryDTO> deliveryList) {
        if (CollectionUtils.isEmpty(deliveryList)){
        return  new ArrayList<>();
        }
        List<String> idlist = deliveryList.stream().map(DeliveryDTO::getId).collect(Collectors.toList());
        return deliveryMapper.toDto(deliveryRepository.findAllByIdIn(idlist));
    }
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void create(List<Delivery> resources) {
         Customer customer = new Customer();
         if(!CollectionUtils.isEmpty(resources)){
             String taskId = String.format("%32d",Integer.parseInt(deliveryRepository.getDeliveryTaskId())).replace(" ","0");
            resources.forEach((item)->{
                item.setId(StringUtils.get32UUID());
                item.setDeliverId(taskId);
                if(StringUtils.isEmpty(customer.getArea())){
                    customer.setArea(item.getArea());
                }
                if(StringUtils.isEmpty(customer.getCustType())){
                    customer.setCustType(item.getCustType());
                }
                if(StringUtils.isEmpty(customer.getCustName())){
                    customer.setCustName(item.getCustName());
                }
            });
         }
         deliveryRepository.saveAll(resources);
         addCustomer(customer);
         addDeliverFuncAndScript(resources);
    }

    @Async
    void addCustomer(Customer customer){
        List<Customer> customers =   customerRepository.findAllByCustTypeEqualsAndAreaEqualsAndCustNameEquals(customer.getCustType(),customer.getArea(),customer.getCustName());
        if(CollectionUtils.isEmpty(customers)){
            //反插入客户信息
            customerService.create(customer);
        }
    }

    @Async
    void addDeliverFuncAndScript(List<Delivery> resources){
        resources.forEach(item->{addDeliverFuncAndScript(item);});
    }

    private void  addDeliverFuncAndScript( Delivery delivery){
        //只操作增值功能且发放依据不为脚本更新&&!StringUtils.equals(delivery.getIssueGist(),"10")
        if(StringUtils.equals(delivery.getDeliverType(),"1")&&!StringUtils.equals(delivery.getIssueGist(),"10")&&
           !StringUtils.contains(delivery.getFunctionModule(),"接口合并工具")){
             //查询功能是否存在
             String sFunctionName = delivery.getFunctionModule();
             String sFundcodeMode = delivery.getFunctionMode();
             String sProductId    = delivery.getProductId();
             List<FunctionInfo> functionInfos = functionInfoRepository.findAllByFunctionNameEqualsAndFunctionModeEqualsAndProductIdEquals(sFunctionName,sFundcodeMode,sProductId);
             FunctionInfoDTO functionInfo;
             if(!CollectionUtils.isEmpty(functionInfos)){
                 functionInfo = functionInfoMapper.toDto(functionInfos.get(0),true);
             }else {
                 FunctionInfo function = new FunctionInfo();
                 function.setFunctionName(sFunctionName);
                 function.setFunctionMode(sFundcodeMode);
                 function.setId(StringUtils.get32UUID());
                 function.setProductId(delivery.getProductId());
                 //增值
                 function.setFunctionType("1");
                 functionInfo = functionInfoMapper.toDto(functionInfoRepository.save(function),true);
             }
             List<ScriptInfoDTO> scripts = functionInfo.getScripts();
             if(!StringUtils.isEmpty(delivery.getConfigName())){
                 List<FunctionScript> functionScripts = new ArrayList<>();
                 String[] sScripts = delivery.getConfigName().split(SPLIT_CHAR);
                 for(String scriptName:sScripts){
                     //检查脚本是否存在
                     List<ScriptInfo> scriptInfos = scriptInfoRepository.findAllByScriptNameEqualsAndProductIdEquals(scriptName,sProductId);
                     ScriptInfo scriptInfo;
                     if(!CollectionUtils.isEmpty(scriptInfos)){
                         scriptInfo = scriptInfos.get(0);
                     }else {
                         //不存在插入
                         scriptInfo = new ScriptInfo();
                         scriptInfo.setProductId(sProductId);
                         scriptInfo.setScriptName(scriptName);
                         scriptInfo.setModuleType(getScriptMoudle(scriptName));
                         scriptInfo.setScriptClass(getScriptClass(scriptName));
                         scriptInfo = scriptInfoMapper.toEntity(scriptInfoService.create(scriptInfo));
                     }
                     boolean bFlag = false;
                     if(!CollectionUtils.isEmpty(scripts)){
                         //判断功能是否关联此脚本
                         for(ScriptInfoDTO scriptInfoDTO : scripts){
                             if(StringUtils.equals(scriptInfoDTO.getId(),scriptInfo.getId())){
                                 bFlag = true;
                                 break;
                             }
                         }
                         if(!bFlag){
                             if(!CollectionUtils.isEmpty(functionScripts)){
                                 for(FunctionScript functionScript:functionScripts){
                                     if(StringUtils.equals(functionScript.getScriptId(),scriptInfo.getId())){
                                         bFlag = true;
                                         break;
                                     }
                                 }
                             }
                         }
                     }
                     if(!bFlag){
                         FunctionScript functionScript = new FunctionScript();
                         functionScript.setFunctionId(functionInfo.getId());
                         functionScript.setScriptId(scriptInfo.getId());
                         functionScripts.add(functionScript);
                     }
                 }
                 //添加功能与脚本关联
                 if(!CollectionUtils.isEmpty(functionScripts)){
                     functionScriptService.create(functionScripts);
                 }
             }

        }
    }


    private String getScriptClass(String scriptName){
        String sScriptClass = "1";
        if(scriptName.contains("开通")){
            sScriptClass = "0";
        }
        return  sScriptClass;
    }

    private  String getScriptMoudle(String scriptName){
        String sScriptMoudle = "0";
        if(scriptName.contains("功能")){
            sScriptMoudle = "0";
        }else if(scriptName.contains("报表")){
            sScriptMoudle = "1";
        }else if(scriptName.contains("查询")){
            sScriptMoudle = "2";
        }else if(scriptName.contains("接口")){
            sScriptMoudle = "3";
        }else if(scriptName.contains("文档")){
            sScriptMoudle = "4";
        }

        return  sScriptMoudle;
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(Delivery resources) {
        Delivery delivery = deliveryRepository.findById(resources.getId()).orElseGet(Delivery::new);
        ValidationUtil.isNull( delivery.getId(),"Delivery","id",resources.getId());
        delivery.copy(resources);
        deliveryRepository.save(delivery);
    }
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdate(List<Delivery> resources) {
        deliveryRepository.saveAll(resources);
    }
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        deliveryRepository.deleteById(id);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<String> id) {
        deliveryRepository.deleteAllByIdIn(id);
    }

    @Override
    public void download(List<DeliveryDTO> all, HttpServletResponse response) throws IOException {
        Map<String, List<DictDetail>> dictMap = dictDetailService.queryAll(DeliveryDTO.class);
        ExcelHelper.exportExcel(response,all,dictMap,DeliveryDTO.class,false);
    }
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void upload(MultipartFile multipartFiles,String id) throws Exception {
       Map<String, List<DictDetail>> dictMap = dictDetailService.queryAll(DeliveryDTO.class);
       List<DeliveryDTO> data = ExcelHelper.importExcel(id,DeliveryDTO.class,dictMap,false);
       if(!CollectionUtils.isEmpty(data)){
          List<Delivery> savelist =  new ArrayList<>();
          List<String> idlist = new ArrayList<>();
          for(DeliveryDTO deliveryDTO:data){
             if(!StringUtils.isEmpty(deliveryDTO.getId())){
                 //删除库中
                 idlist.add(deliveryDTO.getId());
             }else {
                 deliveryDTO.setId(StringUtils.get32UUID());
             }
             savelist.add(deliveryMapper.toEntity(deliveryDTO));
          }
          deliveryRepository.deleteAllByIdIn(idlist);
          int count = savelist.size();
          int num = 0;
          ExcelUtils.updateExeclStatus(ExcelUtils.INSERT_IMP,id);
          for(Delivery delivery: savelist){
              deliveryRepository.save(delivery);
              num++;
              ExcelUtils.updateExeclInserNum(count,num,id);
          }
       }
     }



}
