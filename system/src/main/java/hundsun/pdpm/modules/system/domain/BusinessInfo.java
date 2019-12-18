package hundsun.pdpm.modules.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @author yantt
* @date 2019-12-09
*/
@Entity
@Data
@Table(name="business_info")
public class BusinessInfo implements Serializable {

    // 标识符
    @Id
    @Column(name = "id")
    private String id;

    // 行业
    @Column(name = "cust_type",nullable = false)
    private String custType;

    // 地区
    @Column(name = "area",nullable = false)
    private String area;

    // 客户名称
    @Column(name = "cust_name",nullable = false)
    private String custName;

    // 产品
    @Column(name = "product_id",nullable = false)
    private String productId;

    // 项目描述
    @Column(name = "product_description",nullable = false)
    private String productDescription;

    // 项目类型
    @Column(name = "project_type")
    private String projectType;

    // 项目类别
    @Column(name = "project_class")
    private String projectClass;

    // 竞争对手
    @Column(name = "competitor")
    private String competitor;

    // 预计合同金额(万元)
    @Column(name = "contract_balance",nullable = false)
    private Double contractBalance;

    // 成交金额(万元)
    @Column(name = "confirm_balance")
    private Double confirmBalance;

    // 成功率
    @Column(name = "success_rate",nullable = false)
    private String successRate;

    // 预计合同签订日期
    @Column(name = "contract_time")
    private String contractTime;

    // 所处阶段
    @Column(name = "stage",nullable = false)
    private String stage;

    // 跟进人员
    @Column(name = "follow_person",nullable = false)
    private String followPerson;

    // 更新时间
    @Column(name = "update_time")
    private String updateTime;

    // 销售
    @Column(name = "sale_person",nullable = false)
    private String salePerson;

    // 签单时间
    @Column(name = "estimated_time")
    private String estimatedTime;

    // 合同归档日期
    @Column(name = "contract_fill_date")
    private String contractFillDate;

    // 问题反馈及备注
    @Column(name = "memo")
    private String memo;

    public void copy(BusinessInfo source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
