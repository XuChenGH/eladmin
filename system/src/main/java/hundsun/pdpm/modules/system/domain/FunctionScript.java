package hundsun.pdpm.modules.system.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import java.io.Serializable;

/**
* @author yantt
* @date 2019-12-27
*/
@Entity
@Data
@Table(name="function_script")
public class FunctionScript implements Serializable {

    // 标识符
    @Id
    @Column(name = "id")
    private String id;

    // 功能标识符
    @Column(name = "function_id")
    private String functionId;

    // 脚本标识符
    @Column(name = "script_id")
    private String scriptId;

    public void copy(FunctionScript source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
