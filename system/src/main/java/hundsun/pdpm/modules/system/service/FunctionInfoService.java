package hundsun.pdpm.modules.system.service;

import hundsun.pdpm.modules.system.domain.FunctionInfo;
import hundsun.pdpm.modules.system.service.dto.FunctionInfoDTO;
import hundsun.pdpm.modules.system.service.dto.FunctionInfoQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;
/**
* @author yantt
* @date 2019-12-17
*/
public interface FunctionInfoService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(FunctionInfoQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<FunctionInfoDTO>
    */
    List<FunctionInfoDTO> queryAll(FunctionInfoQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return FunctionInfoDTO
     */
    FunctionInfoDTO findById(String id);

    List<FunctionInfoDTO> findByIdlist(List<FunctionInfoDTO> functionInfoList);

    FunctionInfoDTO create(FunctionInfo resources);

    void update(FunctionInfo resources);

    void delete(String id);

    void download(List<FunctionInfoDTO> all, HttpServletResponse response) throws IOException;

    List<FunctionInfoDTO> upload(MultipartFile multipartFiles) throws Exception;
}
