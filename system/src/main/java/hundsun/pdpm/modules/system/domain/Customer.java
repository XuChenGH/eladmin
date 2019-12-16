package hundsun.pdpm.modules.system.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
* @author yantt
* @date 2019-11-29
*/
@Entity
@Data
@Table(name="customer")
public class Customer implements Serializable {

    // ID
    @Id
    @Column(name = "id")
    private String id;

    // 客户类型
    @Column(name = "cust_type",nullable = false)
    private String custType;

    // 地区
    @Column(name = "area",nullable = false)
    private String area;

    // 客户名称
    @Column(name = "cust_name",nullable = false)
    private String custName;

    // 备注
    @Column(name = "memo")
    private String memo;

    public void copy(Customer source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
