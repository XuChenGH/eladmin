package hundsun.pdpm.modules.system.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import java.io.Serializable;

/**
* @author yantt
* @date 2019-12-25
*/
@Entity
@Data
@Table(name="delivery")
public class Delivery implements Serializable {


    @Id
    @Column(name = "id")
    private String id;
    // 交付任务号
    @Column(name = "deliver_id")
    private String deliverId;

    @Column(name = "product_id",nullable = false)
    private String productId;

    // 交付类型
    @Column(name = "deliver_type")
    private String deliverType;

    // 客户类型
    @Column(name = "cust_type")
    private String custType;

    // 地区
    @Column(name = "area")
    private String area;

    // 客户名称
    @Column(name = "cust_name")
    private String custName;

    // 非主干版本
    @Column(name = "no_trunk_version")
    private String noTrunkVersion;

    // 对应主干版本
    @Column(name = "trunk_version")
    private String trunkVersion;

    // 发放来源
    @Column(name = "issue_source")
    private String issueSource;

    // 任务号/修改单号
    @Column(name = "task_no")
    private String taskNo;

    // SVN日志号
    @Column(name = "svn_no")
    private String svnNo;

    // 需求编号
    @Column(name = "require_no")
    private String requireNo;

    // 需求类型
    @Column(name = "require_type")
    private String requireType;

    // 发放日期
    @Column(name = "issue_date")
    private String issueDate;

    // 发放人
    @Column(name = "issue_person")
    private String issuePerson;

    // 备注
    @Column(name = "memo")
    private String memo;

    // 功能模块
    @Column(name = "function_module")
    private String functionModule;

    // 配置项名称
    @Column(name = "config_name")
    private String configName;

    // 项目编号
    @Column(name = "project_no")
    private String projectNo;

    // 发放依据
    @Column(name = "issue_gist")
    private String issueGist;

    // 模块
    @Column(name = "module_type")
    private String moduleType;

    // TA代码
    @Column(name = "ta_code")
    private String taCode;

    // 模块
    @Column(name = "module")
    private String module;

    @Column(name = "author_date_type")
    private String authorDateType;

    // 功能编号
    @Column(name = "function_no")
    private String functionNo;

    // 审批人
    @Column(name = "approver")
    private String approver;

    // 功能模式
    @Column(name = "function_mode")
    private String functionMode;

    // 程序包明细
    @Column(name = "package_details")
    private String packageDetails;


    // 是否生效
    @Column(name = "used")
    private String used;

    public void copy(Delivery source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
