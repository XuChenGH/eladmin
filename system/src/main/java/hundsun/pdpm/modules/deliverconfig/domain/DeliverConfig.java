package hundsun.pdpm.modules.deliverconfig.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import java.io.Serializable;

/**
* @author yantt
* @date 2020-02-22
*/
@Entity
@Data
@Table(name="deliver_config")
public class DeliverConfig implements Serializable {

    // 标识符
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 产品名称
    @Column(name = "product_id")
    private String productId;

    // 地址
    @Column(name = "address")
    private String address;

    // 地址
    @Column(name = "username")
    private String username;

    public void copy(DeliverConfig source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
