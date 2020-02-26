package hundsun.pdpm.modules.system.service;

import hundsun.pdpm.modules.system.domain.FunctionInfo;
import hundsun.pdpm.modules.system.domain.FunctionScript;
import hundsun.pdpm.modules.system.domain.ScriptInfo;
import hundsun.pdpm.modules.system.service.dto.FunctionInfoDTO;
import hundsun.pdpm.modules.system.service.dto.FunctionScriptDTO;
import hundsun.pdpm.modules.system.service.dto.FunctionScriptQueryCriteria;
import hundsun.pdpm.modules.system.service.dto.ScriptInfoDTO;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;
/**
* @author yantt
* @date 2019-12-27
*/
public interface FunctionScriptService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(FunctionScriptQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<FunctionScriptDTO>
    */
    List<FunctionScriptDTO> queryAll(FunctionScriptQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return FunctionScriptDTO
     */
    FunctionScriptDTO findById(String id);

    List<FunctionScriptDTO> findByIdlist(List<FunctionScriptDTO> functionScriptList);

    FunctionScriptDTO create(FunctionScript resources);

    void create(List<FunctionScript> resources);

    void update(FunctionScript resources);

    void delete(String functionId,String scriptId);

    void download(List<FunctionScriptDTO> all, HttpServletResponse response) throws IOException;

    List<FunctionScriptDTO> upload(MultipartFile multipartFiles) throws Exception;


    List<FunctionInfoDTO> getFunctionByScriptId(String scriptId);

    List<ScriptInfoDTO> getScriptByFunctionId(String functionId);


}
