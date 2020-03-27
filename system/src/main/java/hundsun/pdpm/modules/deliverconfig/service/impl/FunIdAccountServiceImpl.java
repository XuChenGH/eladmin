package hundsun.pdpm.modules.deliverconfig.service.impl;

import hundsun.pdpm.modules.deliverconfig.domain.FunIdAccount;
import hundsun.pdpm.modules.datapermission.utils.PermissionUtils;
import hundsun.pdpm.modules.deliverconfig.service.dto.FunctionNoDTO;
import hundsun.pdpm.modules.execl.ExcelUtils;
import hundsun.pdpm.modules.system.domain.BusinessInfo;
import hundsun.pdpm.modules.system.service.DictDetailService;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.util.CollectionUtils;
import hundsun.pdpm.utils.ValidationUtil;
import hundsun.pdpm.utils.FileUtil;
import hundsun.pdpm.modules.deliverconfig.repository.FunIdAccountRepository;
import hundsun.pdpm.modules.deliverconfig.service.FunIdAccountService;
import hundsun.pdpm.modules.deliverconfig.service.dto.FunIdAccountDTO;
import hundsun.pdpm.modules.deliverconfig.service.dto.FunIdAccountQueryCriteria;
import hundsun.pdpm.modules.deliverconfig.service.mapper.FunIdAccountMapper;
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

import java.util.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;
import hundsun.pdpm.utils.*;
/**
* @author yantt
* @date 2020-02-23
*/
@Service
@CacheConfig(cacheNames = "funIdAccount")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FunIdAccountServiceImpl implements FunIdAccountService {

    private final FunIdAccountRepository funIdAccountRepository;

    private final FunIdAccountMapper funIdAccountMapper;

    @Autowired
    private DictDetailService dictDetailService;

    public FunIdAccountServiceImpl(FunIdAccountRepository funIdAccountRepository, FunIdAccountMapper funIdAccountMapper) {
        this.funIdAccountRepository = funIdAccountRepository;
        this.funIdAccountMapper = funIdAccountMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(FunIdAccountQueryCriteria criteria, Pageable pageable){
        Page<FunIdAccount> page = funIdAccountRepository.findAll((root, criteriaQuery, criteriaBuilder) ->
                                   PermissionUtils.getPredicate(root,
                                       QueryHelp.getPredicate(root,criteria,criteriaBuilder),
                                       criteriaBuilder,FunIdAccountDTO.class),pageable);
        return PageUtil.toPage(page.map(funIdAccountMapper::toDto));
    }

    @Override
    @Cacheable
    public List<FunIdAccountDTO> queryAll(FunIdAccountQueryCriteria criteria){
        return funIdAccountMapper.toDto(funIdAccountRepository.findAll((root, criteriaQuery, criteriaBuilder) ->
                                                PermissionUtils.getPredicate(root,
                                                QueryHelp.getPredicate(root,criteria,criteriaBuilder),
                                                criteriaBuilder,FunIdAccountDTO.class)));
    }

    @Override
    @Cacheable(key = "#p0")
    public FunIdAccountDTO findById(String id) {
        FunIdAccount funIdAccount = funIdAccountRepository.findById(id).orElseGet(FunIdAccount::new);
        ValidationUtil.isNull(funIdAccount.getId(),"FunIdAccount","id",id);
        return funIdAccountMapper.toDto(funIdAccount);
    }
    @Override
    @Cacheable
    public List<FunIdAccountDTO> findByIdlist(List<FunIdAccountDTO> funIdAccountList) {
        if (CollectionUtils.isEmpty(funIdAccountList)){
        return  new ArrayList<>();
        }
        List<String> idlist = funIdAccountList.stream().map(FunIdAccountDTO::getId).collect(Collectors.toList());
        return funIdAccountMapper.toDto(funIdAccountRepository.findAllByIdIn(idlist));
    }
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public FunIdAccountDTO create(FunIdAccount resources) {
        resources.setId(StringUtils.get32UUID());
        return funIdAccountMapper.toDto(funIdAccountRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(FunIdAccount resources) {
        FunIdAccount funIdAccount = funIdAccountRepository.findById(resources.getId()).orElseGet(FunIdAccount::new);
        ValidationUtil.isNull( funIdAccount.getId(),"FunIdAccount","id",resources.getId());
        funIdAccount.copy(resources);
        funIdAccountRepository.save(funIdAccount);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        funIdAccountRepository.deleteById(id);
    }


    @Override
    public void download(List<FunIdAccountDTO> all, HttpServletResponse response) throws IOException {
        Map<String, List<DictDetail>> dictMap = dictDetailService.queryAll(FunIdAccountDTO.class);
        ExcelHelper.exportExcel(response,all,dictMap,FunIdAccountDTO.class,false);
    }
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void upload(MultipartFile multipartFiles, String id) throws Exception {
       Map<String, List<DictDetail>> dictMap = dictDetailService.queryAll(FunIdAccountDTO.class);
       List<FunIdAccountDTO> data = ExcelHelper.importExcel(multipartFiles,FunIdAccountDTO.class,dictMap,false);
       if(!CollectionUtils.isEmpty(data)){
          List<FunIdAccount> savelist =  new ArrayList<>();
          List<String> idlist = new ArrayList<>();
          List<String> functionList = new ArrayList<>();
          for(FunIdAccountDTO funIdAccountDTO:data){
             if(!StringUtils.isEmpty(funIdAccountDTO.getId())){
                    //删除库中
                 idlist.add(funIdAccountDTO.getId());
             }else {
                 funIdAccountDTO.setId(StringUtils.get32UUID());
             }
             if(!StringUtils.isEmpty(funIdAccountDTO.getFunctionNo())){
                 functionList.add(funIdAccountDTO.getFunctionNo());
             }
             savelist.add(funIdAccountMapper.toEntity(funIdAccountDTO));
          }
          funIdAccountRepository.deleteAllByIdIn(idlist);
          funIdAccountRepository.deleteAllByFunctionNoIn(functionList);
          int count = savelist.size();
          int num = 0;
          ExcelUtils.updateExeclStatus(ExcelUtils.INSERT_IMP,id);
           for(FunIdAccount funIdAccount: savelist){
              funIdAccountRepository.save(funIdAccount);
              num++;
              ExcelUtils.updateExeclInserNum(count,num,id);
          }
       }

     }

    @Override
    public Map<String, FunIdAccountDTO> getFunctionNoList(FunIdAccountQueryCriteria criteria) {
        Map<String, FunIdAccountDTO> data = new HashMap<>();
            List<FunIdAccountDTO> result = queryAll(criteria);
            if(!CollectionUtils.isEmpty(result)){
                BeanCopier beanCopier = BeanCopier.create(FunIdAccountDTO.class, FunIdAccountDTO.class, false);
                result.forEach((item)->{
                    if(!StringUtils.isEmpty(item.getScripts()) && StringUtils.equals(item.getControled(),"1")){
                        String[] scripts = item.getScripts().split("\n");
                        for(String script : scripts){
                            FunIdAccountDTO funIdAccountDTO = new FunIdAccountDTO();
                            beanCopier.copy(item,funIdAccountDTO,null);
                            data.put(script,funIdAccountDTO);
                        }
                    }
                });
            }
        return data;
    }
}
