package hundsun.pdpm.modules.system.service.dto;

import lombok.Data;
import hundsun.pdpm.annotation.Excel;
import java.io.Serializable;


/**
* @author yantt
* @date 2019-12-25
*/
@Data
public class DeliveryDTO implements Serializable {

    @Excel(title = "标识符",colwidth = 1,order = "10,10,10,10,10")
    private String id;

    @Excel(title = "是否生效",autosize = true,order = "11,11,11,11,11",dictname = "yes_no")
    private String used;
    // 交付任务号
    @Excel(title = "交付任务号",order = "20,20,20,20,20",autosize = true,align = Excel.Align.ALIGN_LEFT)
    private String deliverId;
    // 交付类型
    @Excel(title = "交付类型", dictname = "deliver_type",sheet = true, order = "0,0,0,0,0")
    private String deliverType;

    @Excel(title = "产品名称", dictname = "product_id",order = "30,30,30,30,30",autosize = true,align = Excel.Align.ALIGN_LEFT)
    private String productId;

    // 客户类型
    @Excel(title = "客户类型", dictname = "cust_type",order = "40,40,40,40,40",autosize = true,align = Excel.Align.ALIGN_LEFT)
    private String custType;

    // 地区
    @Excel(title = "客户区域", dictname = "area",order = "50,50,50,50,50",autosize = true,align = Excel.Align.ALIGN_LEFT)
    private String area;

    // 客户名称
    @Excel(title = "客户名称",order = "60,60,60,60,60",autosize = true,align = Excel.Align.ALIGN_LEFT)
    private String custName;

    // 模块
    @Excel(title = "模块", dictname = "module_type",order = "0,0,70,70,0",autosize = true,align = Excel.Align.ALIGN_LEFT)
    private String moduleType;

    // 功能模块
    @Excel(title = "功能模块" ,order = "0,70,0,0,0",autosize = true,align = Excel.Align.ALIGN_LEFT)
    private String functionModule;

    // TA代码
    @Excel(title = "TA代码" ,order = "0,0,0,0,70",autosize = true,align = Excel.Align.ALIGN_LEFT)
    private String taCode;

    // 模块
    @Excel(title = "模块", dictname = "module",order = "0,0,0,0,80",autosize = true,align = Excel.Align.ALIGN_LEFT)
    private String module;

    @Excel(title = "功能编号",order = "0,0,0,0,90",autosize = true,align = Excel.Align.ALIGN_LEFT)
    private String functionNo;


    // 配置项名称
    @Excel(title = "配置项名称",order = "0,80,80,80,100",autosize = true,align = Excel.Align.ALIGN_LEFT)
    private String configName;

    // 功能模式
    @Excel(title = "功能模式",order = "0,81,0,0,0",autosize = true,align = Excel.Align.ALIGN_LEFT)
    private String functionMode;

    @Excel(title = "授权期限", dictname = "author_date_type",order = "0,0,0,0,110",autosize = true,align = Excel.Align.ALIGN_LEFT)
    private String authorDateType;
    // 项目编号
    @Excel(title = "项目编号",order = "0,90,0,0,130",autosize = true,align = Excel.Align.ALIGN_LEFT)
    private String projectNo;

    // 非主干版本
    @Excel(title = "非主干版本",order = "70,0,0,0,0" ,autosize = true,align = Excel.Align.ALIGN_LEFT)
    private String noTrunkVersion;

    // 对应主干版本
    @Excel(title = "对应主干版本",order = "80,0,0,0,0",autosize = true,align = Excel.Align.ALIGN_LEFT)
    private String trunkVersion;

    // 程序包明细
    @Excel(title = "程序包明细",order = "81,0,0,0,0",autosize = true,align = Excel.Align.ALIGN_LEFT)
    private String packageDetails;

    // 发放来源
    @Excel(title = "发放来源", dictname = "issue_source",order = "0,0,0,0,0",autosize = true,align = Excel.Align.ALIGN_LEFT)
    private String issueSource;

    // 任务号/修改单号
    @Excel(title = "任务号/修改单号",order = "90,0,0,0,0",autosize = true,align = Excel.Align.ALIGN_LEFT)
    private String taskNo;

    // SVN日志号
    @Excel(title = "SVN日志号",order = "100,0,0,0,0",autosize = true,align = Excel.Align.ALIGN_LEFT)
    private String svnNo;

    // 需求编号
    @Excel(title = "需求编号",order = "110,0,0,0,0",autosize = true,align = Excel.Align.ALIGN_LEFT)
    private String requireNo;

    // 发放依据
    @Excel(title = "发放依据", dictname = "issue_gist_no_accretion,issue_gist_accretion,issue_gist_no_accretion,issue_gist_no_accretion,issue_gist_author",order = "120,100,90,90,120",autosize = true,align = Excel.Align.ALIGN_LEFT)
    private String issueGist;

    // 需求类型
    @Excel(title = "需求类型", dictname = "require_type",order = "0,0,0,0,0",autosize = true,align = Excel.Align.ALIGN_LEFT)
    private String requireType;

    // 发放日期
    @Excel(title = "发放日期", plain = true,order = "130,110,100,100,140",autosize = true,align = Excel.Align.ALIGN_LEFT)
    private String issueDate;

    // 发放人
    @Excel(title = "发放人",order = "140,120,110,110,150",autosize = true,align = Excel.Align.ALIGN_LEFT)
    private String issuePerson;

    @Excel(title = "审批人", dictname = "delivery_approver",order = "150,130,120,120,0",autosize = true,align = Excel.Align.ALIGN_LEFT)
    private String approver;

    // 备注
    @Excel(title = "备注",order = "160,140,130,130,160",autosize = true,align = Excel.Align.ALIGN_LEFT)
    private String memo;





}
