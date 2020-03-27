package hundsun.pdpm.modules.ftpfile.service.dto;

import lombok.Data;
import hundsun.pdpm.annotation.Query;

/**
* @author yantt
* @date 2019-12-31
*/
@Data
public class FtpUserQueryCriteria{

    // 精确
    @Query
    private Integer id;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String username;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String sftpHost;

    // 精确
    @Query
    private Integer sftpPort;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String sftpPath;

    // 精确
    @Query
    private String sftpUseType;
}
