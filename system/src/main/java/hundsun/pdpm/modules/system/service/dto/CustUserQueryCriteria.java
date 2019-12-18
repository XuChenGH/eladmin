package hundsun.pdpm.modules.system.service.dto;

import hundsun.pdpm.annotation.Query;
import lombok.Data;


/**
* @author yantt
* @date 2019-11-30
*/
@Data
public class CustUserQueryCriteria{

    // 精确
    @Query
    private Long id;

    // 精确
    @Query
    private String custId;

    // 精确
    @Query
    private Long userId;
}
