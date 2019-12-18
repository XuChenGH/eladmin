package hundsun.pdpm.modules.system.service.dto;

import lombok.Data;
import hundsun.pdpm.annotation.Query;

/**
* @author yantt
* @date 2019-12-05
*/
@Data
public class CustProductQueryCriteria{

    // 精确
    @Query
    private String id;

    // 精确
    @Query
    private String custId;

    // 精确
    @Query
    private String productId;

    // 精确
    @Query
    private String versionType;
}
