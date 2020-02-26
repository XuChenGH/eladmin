package hundsun.pdpm.modules.ftpfile.service;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import hundsun.pdpm.modules.ftpfile.domain.FtpFile;
import hundsun.pdpm.modules.ftpfile.domain.FtpUser;
import hundsun.pdpm.modules.ftpfile.service.dto.FtpFileDTO;
import hundsun.pdpm.modules.ftpfile.service.dto.FtpFileQueryCriteria;
import hundsun.pdpm.modules.ftpfile.service.dto.SftpDTO;
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
public interface FtpFileService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(FtpFileQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<FtpFileDTO>
    */
    List<FtpFileDTO> queryAll(FtpFileQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return FtpFileDTO
     */
    FtpFileDTO findById(Integer id);

    List<FtpFileDTO> findByIdlist(List<FtpFileDTO> ftpFileList);

    FtpFileDTO create(FtpFile resources);

    void update(FtpFile resources);

    void delete(Integer id);

    void download(List<FtpFileDTO> all, HttpServletResponse response) throws IOException;

    List<FtpFileDTO> upload(MultipartFile multipartFiles) throws Exception;

    void  refresh(FtpUser ftpUser, String pathName, int parentId) throws Exception ;

    List<FtpFileDTO> getFileList(String pathName, SftpDTO sftp) throws SftpException;

    void getFileList(String pathName, List<FtpFileDTO> file, ChannelSftp sftp) throws SftpException;


    void getFileList(String pathName, String ext) throws SftpException;
}
