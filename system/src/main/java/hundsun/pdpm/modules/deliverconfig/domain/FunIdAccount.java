package hundsun.pdpm.modules.deliverconfig.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import java.io.Serializable;

/**
* @author yantt
* @date 2020-02-23
*/
@Entity
@Data
@Table(name="fun_id_account")
public class FunIdAccount implements Serializable {

    // 标识符
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "product_id")
    private String productId;

    // 功能编号
    @Column(name = "function_no")
    private String functionNo;

    // 是否控制
    @Column(name = "controled")
    private String controled;

    // 功能脚本
    @Column(name = "scripts")
    private String scripts;

    public void copy(FunIdAccount source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
