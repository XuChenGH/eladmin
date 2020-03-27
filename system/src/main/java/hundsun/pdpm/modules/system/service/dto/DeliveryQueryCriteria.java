package hundsun.pdpm.modules.system.service.dto;

import lombok.Data;
import hundsun.pdpm.annotation.Query;

/**
* @author yantt
* @date 2019-12-25
*/
@Data
public class DeliveryQueryCriteria{

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String id;
    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String deliverId;
    // 精确
    @Query
    private String deliverType;

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
    private String noTrunkVersion;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String trunkVersion;

    // 精确
    @Query
    private String issueSource;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String taskNo;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String svnNo;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String requireNo;

    // 精确
    @Query
    private String requireType;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String issueDate;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String issuePerson;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String memo;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String functionModule;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String configName;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String projectNo;

    // 精确
    @Query
    private String issueGist;

    // 精确
    @Query
    private String moduleType;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String taCode;

    // 精确
    @Query
    private String module;

    // 精确
    @Query
    private String authorDateType;

    @Query(type = Query.Type.INNER_LIKE)
    private String functionNo;

    @Query
    private String productId;

    @Query
    private String approver;


    @Query(type = Query.Type.INNER_LIKE)
    private String functionMode;

    @Query(type = Query.Type.INNER_LIKE)
    private String packageDetails;

    @Query
    private String used;
}
