package hundsun.pdpm.modules.datapermission.service.dto;

import lombok.Data;
import hundsun.pdpm.annotation.Query;

/**
* @author yantt
* @date 2019-12-13
*/
@Data
public class DataPermissionFieldQueryCriteria{

    // 精确
    @Query
    private String id;

    @Query(type = Query.Type.INNER_LIKE)
    private String name;
    // 精确
    @Query
    private String permissionId;

    // 精确
    @Query
    private String fieldCode;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String fieldName;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String dictName;

    // 精确
    @Query
    private String operateType;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String operateValue;
}
