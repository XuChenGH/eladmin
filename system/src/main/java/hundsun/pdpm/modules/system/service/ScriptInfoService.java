package hundsun.pdpm.modules.system.service;

import hundsun.pdpm.modules.system.domain.ScriptInfo;
import hundsun.pdpm.modules.system.service.dto.ScriptInfoDTO;
import hundsun.pdpm.modules.system.service.dto.ScriptInfoQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;
/**
* @author yantt
* @date 2019-12-18
*/
public interface ScriptInfoService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(ScriptInfoQueryCriteria criteria, Pageable pageable,boolean haveFunc);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<ScriptInfoDTO>
    */
    List<ScriptInfoDTO> queryAll(ScriptInfoQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return ScriptInfoDTO
     */
    ScriptInfoDTO findById(String id);

    List<ScriptInfoDTO> findScirptByName(List<String> scripts);

    List<ScriptInfoDTO> findByIdlist(List<ScriptInfoDTO> scriptInfoList);

    ScriptInfoDTO create(ScriptInfo resources);

    void update(ScriptInfo resources);

    void delete(String id);

    void download(List<ScriptInfoDTO> all, HttpServletResponse response) throws IOException;

    List<ScriptInfoDTO> upload(MultipartFile multipartFiles) throws Exception;
}
