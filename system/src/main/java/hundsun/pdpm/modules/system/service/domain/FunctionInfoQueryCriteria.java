package hundsun.pdpm.modules.system.service.dto;

import lombok.Data;
import hundsun.pdpm.annotation.Query;

/**
* @author yantt
* @date 2019-12-17
*/
@Data
public class FunctionInfoQueryCriteria{

    // 精确
    @Query
    private String id;

    // 精确
    @Query
    private String productId;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String functionName;

    // 精确
    @Query
    private String functionType;

    // 精确
    @Query
    private String functionMode;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String memo;
}
