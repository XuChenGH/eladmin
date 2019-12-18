package hundsun.pdpm.modules.system.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import java.io.Serializable;

/**
* @author yantt
* @date 2019-12-18
*/
@Entity
@Data
@Table(name="script_info")
public class ScriptInfo implements Serializable {

    // 标识符
    @Id
    @Column(name = "id")
    private String id;

    // 功能标识符
    @Column(name = "function_id",nullable = false)
    private String functionId;

    // 脚本类别
    @Column(name = "script_class",nullable = false)
    private String scriptClass;

    // 脚本名称
    @Column(name = "script_name",nullable = false)
    private String scriptName;

    // 生效版本
    @Column(name = "effect_version",nullable = false)
    private String effectVersion;

    // 用户手册名称
    @Column(name = "user_manual_name")
    private String userManualName;

    // 备注
    @Column(name = "memo")
    private String memo;

    public void copy(ScriptInfo source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
