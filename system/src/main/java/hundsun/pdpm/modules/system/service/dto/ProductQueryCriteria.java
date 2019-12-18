package hundsun.pdpm.modules.system.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import hundsun.pdpm.annotation.Query;

/**
* @author yantt
* @date 2019-12-05
*/
@Data
public class ProductQueryCriteria{

    // 精确
    @Query
    private String id;

    // 精确
    @Query
    private String productId;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String versionNo;

    // 精确
    @Query
    private String versionType;

    // 精确
    @Query
    private String baseVersionNo;

    // 精确
    @Query
    private String releaseStatus;

    // 精确
    @Query
    private Timestamp releaseTime;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String memo;
}
