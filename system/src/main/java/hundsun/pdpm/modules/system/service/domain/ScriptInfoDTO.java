package hundsun.pdpm.modules.system.service.dto;

import lombok.Data;
import hundsun.pdpm.annotation.Excel;
import java.io.Serializable;


/**
* @author yantt
* @date 2019-12-18
*/
@Data
public class ScriptInfoDTO implements Serializable {

    // 标识符
    @Excel(title = "标识符")
    private String id;

    // 功能标识符
    @Excel(title = "功能标识符")
    private String functionId;

    // 脚本类别
    @Excel(title = "脚本类别", dictname = "script_class")
    private String scriptClass;

    // 脚本名称
    @Excel(title = "脚本名称")
    private String scriptName;

    // 生效版本
    @Excel(title = "生效版本")
    private String effectVersion;

    // 用户手册名称
    @Excel(title = "用户手册名称")
    private String userManualName;

    // 备注
    @Excel(title = "备注")
    private String memo;
}
