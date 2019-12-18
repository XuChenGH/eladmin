package hundsun.pdpm.modules.system.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import java.io.Serializable;

/**
* @author yantt
* @date 2019-12-17
*/
@Entity
@Data
@Table(name="function_info")
public class FunctionInfo implements Serializable {

    // 标识符
    @Id
    @Column(name = "id")
    private String id;

    // 产品名称
    @Column(name = "product_id",nullable = false)
    private String productId;

    // 功能名称
    @Column(name = "function_name",nullable = false)
    private String functionName;

    // 功能类型
    @Column(name = "function_type",nullable = false)
    private String functionType;

    // 功能模式
    @Column(name = "function_mode")
    private String functionMode;

    // 备注
    @Column(name = "memo")
    private String memo;

    public void copy(FunctionInfo source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
