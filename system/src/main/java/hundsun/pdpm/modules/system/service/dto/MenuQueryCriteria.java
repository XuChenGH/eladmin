package hundsun.pdpm.modules.system.service.dto;

import hundsun.pdpm.annotation.Query;
import lombok.Data;

import java.sql.Timestamp;

/**
 * 公共查询类
 */
@Data
public class MenuQueryCriteria {

    // 多字段模糊
    @Query(blurry = "name,path,component")
    private String blurry;

    @Query(type = Query.Type.GREATER_THAN,propName = "createTime")
    private Timestamp startTime;

    @Query(type = Query.Type.LESS_THAN,propName = "createTime")
    private Timestamp endTime;
}
