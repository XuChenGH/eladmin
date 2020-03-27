package hundsun.pdpm.modules.deliverconfig.service;

import hundsun.pdpm.modules.deliverconfig.domain.FunIdAccount;
import hundsun.pdpm.modules.deliverconfig.service.dto.FunIdAccountDTO;
import hundsun.pdpm.modules.deliverconfig.service.dto.FunIdAccountQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;
/**
* @author yantt
* @date 2020-02-23
*/
public interface FunIdAccountService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(FunIdAccountQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<FunIdAccountDTO>
    */
    List<FunIdAccountDTO> queryAll(FunIdAccountQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return FunIdAccountDTO
     */
    FunIdAccountDTO findById(String id);

    List<FunIdAccountDTO> findByIdlist(List<FunIdAccountDTO> funIdAccountList);

    FunIdAccountDTO create(FunIdAccount resources);

    void update(FunIdAccount resources);

    void delete(String id);

    void download(List<FunIdAccountDTO> all, HttpServletResponse response) throws IOException;

    void upload(MultipartFile multipartFiles, String id) throws Exception;

    Map<String,FunIdAccountDTO> getFunctionNoList(FunIdAccountQueryCriteria criteria);
}
