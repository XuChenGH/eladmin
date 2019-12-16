package hundsun.pdpm.modules.system.service.dto;

import hundsun.pdpm.annotation.Query;
import lombok.Data;

/**
* @author yantt
* @date 2019-11-29
*/
@Data
public class CustomerQueryCriteria {

    // 精确
    @Query
    private String id;

    // 精确
    @Query
    private String custType;

    // 精确
    @Query
    private String area;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String custName;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String memo;
}
