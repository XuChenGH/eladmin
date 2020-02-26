package hundsun.pdpm.modules.ftpfile.service.dto;

import lombok.Data;
import hundsun.pdpm.annotation.Query;

/**
* @author yantt
* @date 2019-12-31
*/
@Data
public class FtpFileQueryCriteria{

    // 精确
    @Query
    private Integer id;

    @Query
    private Integer ftpId;
    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String fileName;

    // 精确
    @Query
    private String isDir;
}
