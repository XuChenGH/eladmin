package hundsun.pdpm.modules.system.service.dto;

import lombok.Data;
import hundsun.pdpm.annotation.Query;

/**
* @author yantt
* @date 2019-12-18
*/
@Data
public class ScriptInfoQueryCriteria{

    // 精确
    @Query
    private String id;

    // 精确
    @Query
    private String functionId;

    // 精确
    @Query
    private String scriptClass;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String scriptName;

    // 精确
    @Query
    private String effectVersion;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String userManualName;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String memo;
}
