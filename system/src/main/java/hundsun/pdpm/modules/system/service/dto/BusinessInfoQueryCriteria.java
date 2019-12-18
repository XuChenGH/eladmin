package hundsun.pdpm.modules.system.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import hundsun.pdpm.annotation.Query;

/**
* @author yantt
* @date 2019-12-09
*/
@Data
public class BusinessInfoQueryCriteria{

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

    // 精确
    @Query
    private String productId;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String productDescription;

    // 精确
    @Query
    private String projectType;

    // 精确
    @Query
    private String projectClass;

    // 精确
    @Query(type = Query.Type.INNER_LIKE)
    private String competitor;

    // 精确
    @Query
    private Double contractBalance;

    // 精确
    @Query
    private Double confirmBalance;

    // 精确
    @Query
    private String successRate;

    // 精确
    @Query
    private String contractTime;

    // 精确
    @Query
    private String stage;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String followPerson;

    // 精确
    @Query
    private String updateTime;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String salePerson;

    // 精确
    @Query
    private String estimatedTime;

    // 精确
    @Query
    private String contractFillDate;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String memo;
}
