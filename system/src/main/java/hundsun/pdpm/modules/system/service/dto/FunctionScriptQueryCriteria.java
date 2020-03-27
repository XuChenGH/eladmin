package hundsun.pdpm.modules.system.service.dto;

import lombok.Data;
import hundsun.pdpm.annotation.Query;

/**
* @author yantt
* @date 2019-12-27
*/
@Data
public class FunctionScriptQueryCriteria{

    // 精确
    @Query
    private String id;

    // 精确
    @Query
    private String functionId;

    // 精确
    @Query
    private String scriptId;
}
