package hundsun.pdpm.modules.system.service.impl;

import hundsun.pdpm.modules.system.domain.FunctionInfo;
import hundsun.pdpm.modules.system.domain.ScriptInfo;
import hundsun.pdpm.modules.datapermission.utils.PermissionUtils;
import hundsun.pdpm.modules.system.service.DictDetailService;
import org.springframework.util.CollectionUtils;
import hundsun.pdpm.utils.ValidationUtil;
import hundsun.pdpm.utils.FileUtil;
import hundsun.pdpm.modules.system.repository.ScriptInfoRepository;
import hundsun.pdpm.modules.system.service.ScriptInfoService;
import hundsun.pdpm.modules.system.service.dto.ScriptInfoDTO;
import hundsun.pdpm.modules.system.service.dto.ScriptInfoQueryCriteria;
import hundsun.pdpm.modules.system.service.mapper.ScriptInfoMapper;
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
* @date 2019-12-18
*/
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ScriptInfoServiceImpl implements ScriptInfoService {

    private final ScriptInfoRepository scriptInfoRepository;

    private final ScriptInfoMapper scriptInfoMapper;

    @Autowired
    private DictDetailService dictDetailService;

    public ScriptInfoServiceImpl(ScriptInfoRepository scriptInfoRepository, ScriptInfoMapper scriptInfoMapper) {
        this.scriptInfoRepository = scriptInfoRepository;
        this.scriptInfoMapper = scriptInfoMapper;
    }

    @Override
    public Map<String,Object> queryAll(ScriptInfoQueryCriteria criteria, Pageable pageable,boolean haveFunc){
        Page<ScriptInfo> page = scriptInfoRepository.findAll((root, criteriaQuery, criteriaBuilder) ->
                                   PermissionUtils.getPredicate(root,
                                       QueryHelp.getPredicate(root,criteria,criteriaBuilder),
                                       criteriaBuilder,ScriptInfoDTO.class),pageable);
        return PageUtil.toPage(page.map((scriptInfo)->scriptInfoMapper.toDto(scriptInfo,haveFunc)));
    }

    @Override
    public List<ScriptInfoDTO> queryAll(ScriptInfoQueryCriteria criteria){
        return scriptInfoMapper.toDto(scriptInfoRepository.findAll((root, criteriaQuery, criteriaBuilder) ->
                                                PermissionUtils.getPredicate(root,
                                                QueryHelp.getPredicate(root,criteria,criteriaBuilder),
                                                criteriaBuilder,ScriptInfoDTO.class)),true);
    }

    @Override
    public ScriptInfoDTO findById(String id) {
        ScriptInfo scriptInfo = scriptInfoRepository.findById(id).orElseGet(ScriptInfo::new);
        ValidationUtil.isNull(scriptInfo.getId(),"ScriptInfo","id",id);
        return scriptInfoMapper.toDto(scriptInfo,true);
    }
    @Override
    public List<ScriptInfoDTO> findByIdlist(List<ScriptInfoDTO> scriptInfoList) {
        if (CollectionUtils.isEmpty(scriptInfoList)){
        return  new ArrayList<>();
        }
        List<String> idlist = scriptInfoList.stream().map(ScriptInfoDTO::getId).collect(Collectors.toList());
        return scriptInfoMapper.toDto(scriptInfoRepository.findAllByIdIn(idlist),true);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ScriptInfoDTO create(ScriptInfo resources) {
        resources.setId(StringUtils.get32UUID());
        ScriptInfo scriptInfo = scriptInfoRepository.save(resources);
        return scriptInfoMapper.toDto(scriptInfo,true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ScriptInfo resources) {
        ScriptInfo scriptInfo = scriptInfoRepository.findById(resources.getId()).orElseGet(ScriptInfo::new);
        ValidationUtil.isNull( scriptInfo.getId(),"ScriptInfo","id",resources.getId());
        scriptInfo.copy(resources);
        scriptInfoRepository.save(scriptInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        scriptInfoRepository.deleteById(id);
    }


    @Override
    public void download(List<ScriptInfoDTO> all, HttpServletResponse response) throws IOException {
        Map<String, List<DictDetail>> dictMap = dictDetailService.queryAll(ScriptInfoDTO.class);
        ExcelHelper.exportExcel(response,all,dictMap,ScriptInfoDTO.class,false);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ScriptInfoDTO> upload(MultipartFile multipartFiles) throws Exception {
       Map<String, List<DictDetail>> dictMap = dictDetailService.queryAll(ScriptInfoDTO.class);
       List<ScriptInfoDTO> data = ExcelHelper.importExcel(multipartFiles,ScriptInfoDTO.class,dictMap,false);
       if(!CollectionUtils.isEmpty(data)){
          List<ScriptInfo> savelist =  new ArrayList<>();
          List<String> idlist = new ArrayList<>();
          for(ScriptInfoDTO scriptInfoDTO:data){
             if(!StringUtils.isEmpty(scriptInfoDTO.getId())){
                    //删除库中
                 idlist.add(scriptInfoDTO.getId());
             }else {
                 scriptInfoDTO.setId(StringUtils.get32UUID());
             }
             savelist.add(scriptInfoMapper.toEntity(scriptInfoDTO));
          }
       scriptInfoRepository.deleteAllByIdIn(idlist);
       scriptInfoRepository.saveAll(savelist);
       }
        return  data;
     }
}
