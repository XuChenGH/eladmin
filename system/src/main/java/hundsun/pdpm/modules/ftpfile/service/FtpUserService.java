package hundsun.pdpm.modules.ftpfile.service;

import hundsun.pdpm.modules.ftpfile.domain.FtpUser;
import hundsun.pdpm.modules.ftpfile.service.dto.FtpUserDTO;
import hundsun.pdpm.modules.ftpfile.service.dto.FtpUserQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;
/**
* @author yantt
* @date 2019-12-31
*/
public interface FtpUserService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(FtpUserQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<FtpUserDTO>
    */
    List<FtpUserDTO> queryAll(FtpUserQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return FtpUserDTO
     */
    FtpUserDTO findById(Integer id);

    List<FtpUserDTO> findByIdlist(List<FtpUserDTO> ftpUserList);

    FtpUserDTO create(FtpUser resources);

    void update(FtpUser resources);

    void delete(Integer id);

    void download(List<FtpUserDTO> all, HttpServletResponse response) throws IOException;

    List<FtpUserDTO> upload(MultipartFile multipartFiles) throws Exception;
}
