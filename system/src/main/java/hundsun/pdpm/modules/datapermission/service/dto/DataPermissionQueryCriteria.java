package hundsun.pdpm.modules.datapermission.service.dto;

import lombok.Data;
import hundsun.pdpm.annotation.Query;

/**
* @author yantt
* @date 2019-12-13
*/
@Data
public class DataPermissionQueryCriteria{

    // 精确
    @Query
    private String id;

    // 精确
    @Query
    private Long roleId;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String tableCode;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String tableName;
}
