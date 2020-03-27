package hundsun.pdpm.modules.deliverconfig.service.dto;

import lombok.Data;
import hundsun.pdpm.annotation.Query;

/**
* @author yantt
* @date 2020-02-22
*/
@Data
public class DeliverConfigQueryCriteria{

    // 精确
    @Query
    private Integer id;

    // 精确
    @Query
    private String productId;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String address;

    @Query
    private String username;

}
