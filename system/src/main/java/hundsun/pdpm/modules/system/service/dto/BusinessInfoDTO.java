package hundsun.pdpm.modules.system.service.dto;

import hundsun.pdpm.annotation.PermissionField;
import hundsun.pdpm.annotation.PermissionObject;
import lombok.Data;
import hundsun.pdpm.annotation.Excel;
import java.sql.Timestamp;
import java.io.Serializable;


/**
* @author yantt
* @date 2019-12-09
*/
@Data
@PermissionObject(tablecode = "businessInfo",tablename = "TA商机信息")
public class BusinessInfoDTO implements Serializable {

    // 标识符
    @Excel(title = "标识符", colwidth = 1)
    private String id;

    // 行业
    @Excel(title = "行业", dictname = "cust_type")
    @PermissionField(fieldcode = "custType",fieldname = "行业",dictname = "cust_type")
    private String custType;

    // 地区
    @Excel(title = "地区", dictname = "area")
    @PermissionField(fieldcode = "area",fieldname = "地区",dictname = "area")
    private String area;

    // 客户名称
    @Excel(title = "客户名称", autosize = true)
    @PermissionField(fieldcode = "custName",fieldname = "客户名称")
    private String custName;

    // 产品
    @Excel(title = "产品", dictname = "bis_product_id")
    @PermissionField(fieldcode = "productId",fieldname = "产品",dictname = "bis_product_id")
    private String productId;

    // 项目描述
    @Excel(title = "项目描述", autosize = true)
    private String productDescription;

    // 项目类型
    @Excel(title = "项目类型", dictname = "project_type")
    @PermissionField(fieldcode = "projectType",fieldname = "项目类型",dictname = "project_type")
    private String projectType;

    // 项目类别
    @Excel(title = "项目类别", dictname = "project_class")
    @PermissionField(fieldcode = "projectClass",fieldname = "项目类别",dictname = "project_class")
    private String projectClass;

    // 竞争对手
    @Excel(title = "竞争对手", dictname = "competitor", autosize = true)
    @PermissionField(fieldcode = "competitor",fieldname = "竞争对手",dictname = "competitor")
    private String competitor;

    // 预计合同金额(万元)
    @Excel(title = "预计合同金额(万元)", autosize = true)
    private Double contractBalance;

    // 成交金额(万元)
    @Excel(title = "成交金额(万元)", autosize = true)
    private Double confirmBalance;

    // 成功率
    @Excel(title = "成功率", dictname = "success_rate", percent = true)
    private String successRate;

    // 预计合同签订日期
    @Excel(title = "预计合同签订日期", autosize = true)
    private String contractTime;

    // 所处阶段
    @Excel(title = "所处阶段", dictname = "stage", autosize = true)
    @PermissionField(fieldcode = "stage",fieldname = "所处阶段",dictname = "stage")
    private String stage;

    // 跟进人员
    @Excel(title = "跟进人员", dictname = "follow_person", autosize = true)
    @PermissionField(fieldcode = "followPerson",fieldname = "跟进人员",dictname = "follow_person")
    private String followPerson;

    // 更新时间
    @Excel(title = "更新时间",tranfer = "eightDate", autosize = true)
    private String updateTime;

    // 销售
    @Excel(title = "销售", autosize = true)
    private String salePerson;

    // 签单时间
    @Excel(title = "签单时间",tranfer = "eightDate", autosize = true)
    private String estimatedTime;

    // 合同归档日期
    @Excel(title = "合同归档日期",tranfer = "eightDate", autosize = true)
    private String contractFillDate;

    // 问题反馈及备注
    @Excel(title = "问题反馈及备注")
    private String memo;
}
