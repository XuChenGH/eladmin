package hundsun.pdpm.modules.deliverconfig.service.dto;

import lombok.Data;
import hundsun.pdpm.annotation.Query;

/**
* @author yantt
* @date 2020-02-23
*/
@Data
public class FunIdAccountQueryCriteria{

    // 精确
    @Query
    private String id;

    @Query
    private String productId;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String functionNo;

    // 精确
    @Query
    private String controled;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String scripts;
}
