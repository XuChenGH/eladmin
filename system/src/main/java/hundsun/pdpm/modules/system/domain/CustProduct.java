package hundsun.pdpm.modules.system.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;

/**
* @author yantt
* @date 2019-12-05
*/
@Entity
@Data
@Table(name="cust_product")
public class CustProduct implements Serializable {

    // ID
    @Id
    @Column(name = "id")
    private String id;

    // 客户ID
    @Column(name = "cust_id",nullable = false)
    private String custId;

    // 产品ID
    @OneToOne
    @JoinColumn(name = "product_id")
    @NotFound(action= NotFoundAction.IGNORE)
    private Product product;

    // 版本类型
    @Column(name = "version_type",nullable = false)
    private String versionType;

    public void copy(CustProduct source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
