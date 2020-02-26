package hundsun.pdpm.modules.system.service.impl;

import hundsun.pdpm.modules.system.domain.FunctionInfo;
import hundsun.pdpm.modules.system.domain.FunctionScript;
import hundsun.pdpm.modules.datapermission.utils.PermissionUtils;
import hundsun.pdpm.modules.system.domain.ScriptInfo;
import hundsun.pdpm.modules.system.repository.FunctionInfoRepository;
import hundsun.pdpm.modules.system.repository.ScriptInfoRepository;
import hundsun.pdpm.modules.system.service.DictDetailService;
import hundsun.pdpm.modules.system.service.dto.FunctionInfoDTO;
import hundsun.pdpm.modules.system.service.dto.ScriptInfoDTO;
import hundsun.pdpm.modules.system.service.mapper.FunctionInfoMapper;
import hundsun.pdpm.modules.system.service.mapper.ScriptInfoMapper;
import org.springframework.util.CollectionUtils;
import hundsun.pdpm.utils.ValidationUtil;
import hundsun.pdpm.utils.FileUtil;
import hundsun.pdpm.modules.system.repository.FunctionScriptRepository;
import hundsun.pdpm.modules.system.service.FunctionScriptService;
import hundsun.pdpm.modules.system.service.dto.FunctionScriptDTO;
import hundsun.pdpm.modules.system.service.dto.FunctionScriptQueryCriteria;
import hundsun.pdpm.modules.system.service.mapper.FunctionScriptMapper;
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
* @date 2019-12-27
*/
@Service
@CacheConfig(cacheNames = "functionScript")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FunctionScriptServiceImpl implements FunctionScriptService {

    private final FunctionScriptRepository functionScriptRepository;

    private final FunctionScriptMapper functionScriptMapper;


    @Autowired
    private FunctionInfoRepository functionInfoRepository;

    @Autowired
    private FunctionInfoMapper functionInfoMapper;

    @Autowired
    private ScriptInfoRepository scriptInfoRepository;

    @Autowired
    private ScriptInfoMapper scriptInfoMapper;

    @Autowired
    private DictDetailService dictDetailService;

    public FunctionScriptServiceImpl(FunctionScriptRepository functionScriptRepository, FunctionScriptMapper functionScriptMapper) {
        this.functionScriptRepository = functionScriptRepository;
        this.functionScriptMapper = functionScriptMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(FunctionScriptQueryCriteria criteria, Pageable pageable){
        Page<FunctionScript> page = functionScriptRepository.findAll((root, criteriaQuery, criteriaBuilder) ->
                                   PermissionUtils.getPredicate(root,
                                       QueryHelp.getPredicate(root,criteria,criteriaBuilder),
                                       criteriaBuilder,FunctionScriptDTO.class),pageable);
        return PageUtil.toPage(page.map(functionScriptMapper::toDto));
    }

    @Override
    @Cacheable
    public List<FunctionScriptDTO> queryAll(FunctionScriptQueryCriteria criteria){
        return functionScriptMapper.toDto(functionScriptRepository.findAll((root, criteriaQuery, criteriaBuilder) ->
                                                PermissionUtils.getPredicate(root,
                                                QueryHelp.getPredicate(root,criteria,criteriaBuilder),
                                                criteriaBuilder,FunctionScriptDTO.class)));
    }

    @Override
    @Cacheable(key = "#p0")
    public FunctionScriptDTO findById(String id) {
        FunctionScript functionScript = functionScriptRepository.findById(id).orElseGet(FunctionScript::new);
        ValidationUtil.isNull(functionScript.getId(),"FunctionScript","id",id);
        return functionScriptMapper.toDto(functionScript);
    }
    @Override
    @Cacheable
    public List<FunctionScriptDTO> findByIdlist(List<FunctionScriptDTO> functionScriptList) {
        if (CollectionUtils.isEmpty(functionScriptList)){
        return  new ArrayList<>();
        }
        List<String> idlist = functionScriptList.stream().map(FunctionScriptDTO::getId).collect(Collectors.toList());
        return functionScriptMapper.toDto(functionScriptRepository.findAllByIdIn(idlist));
    }
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public FunctionScriptDTO create(FunctionScript resources) {
        resources.setId(StringUtils.get32UUID());
        return functionScriptMapper.toDto(functionScriptRepository.save(resources));
    }
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void create(List<FunctionScript> resources) {
         if(!CollectionUtils.isEmpty(resources)){
             resources.forEach((item)->{
                 item.setId(StringUtils.get32UUID());
             });
             functionScriptRepository.deleteAllByFunctionId(resources.get(0).getFunctionId());
             functionScriptRepository.saveAll(resources);
         }
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(FunctionScript resources) {
        FunctionScript functionScript = functionScriptRepository.findById(resources.getId()).orElseGet(FunctionScript::new);
        ValidationUtil.isNull( functionScript.getId(),"FunctionScript","id",resources.getId());
        functionScript.copy(resources);
        functionScriptRepository.save(functionScript);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(String functionId,String scriptId) {
        functionScriptRepository.deleteAllByFunctionIdEqualsAndScriptIdEquals(functionId,scriptId);
    }


    @Override
    public void download(List<FunctionScriptDTO> all, HttpServletResponse response) throws IOException {
        Map<String, List<DictDetail>> dictMap = dictDetailService.queryAll(FunctionScriptDTO.class);
        ExcelHelper.exportExcel(response,all,dictMap,FunctionScriptDTO.class,false);
    }
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public List<FunctionScriptDTO> upload(MultipartFile multipartFiles) throws Exception {
       Map<String, List<DictDetail>> dictMap = dictDetailService.queryAll(FunctionScriptDTO.class);
       List<FunctionScriptDTO> data = ExcelHelper.importExcel(multipartFiles,FunctionScriptDTO.class,dictMap,false);
       if(!CollectionUtils.isEmpty(data)){
          List<FunctionScript> savelist =  new ArrayList<>();
          List<String> idlist = new ArrayList<>();
          for(FunctionScriptDTO functionScriptDTO:data){
             if(!StringUtils.isEmpty(functionScriptDTO.getId())){
                    //删除库中
                 idlist.add(functionScriptDTO.getId());
             }else {
                 functionScriptDTO.setId(StringUtils.get32UUID());
             }
             savelist.add(functionScriptMapper.toEntity(functionScriptDTO));
          }
       functionScriptRepository.deleteAllByIdIn(idlist);
       functionScriptRepository.saveAll(savelist);
       }
        return  data;
     }


    @Override
    public List<FunctionInfoDTO> getFunctionByScriptId(String scriptId) {
        List<FunctionScript> functionScriptList = functionScriptRepository.findAllByScriptId(scriptId);
        List<String> functionIdList = functionScriptList.stream().map(FunctionScript::getFunctionId).collect(Collectors.toList());
        List<FunctionInfo> functionInfoList = functionInfoRepository.findAllByIdIn(functionIdList);
        return functionInfoMapper.toDto(functionInfoList,false);
    }

    @Override
    public List<ScriptInfoDTO> getScriptByFunctionId(String functionId) {
        List<FunctionScript> functionScriptList = functionScriptRepository.findAllByFunctionId(functionId);
        List<String> scriptIdList = functionScriptList.stream().map(FunctionScript::getScriptId).collect(Collectors.toList());
        List<ScriptInfo> scriptInfoList = scriptInfoRepository.findAllByIdIn(scriptIdList);
        return scriptInfoMapper.toDto(scriptInfoList,false);
    }
}
