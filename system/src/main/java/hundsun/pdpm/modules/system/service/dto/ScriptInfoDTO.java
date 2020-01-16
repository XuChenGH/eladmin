package hundsun.pdpm.modules.system.service.dto;

import hundsun.pdpm.annotation.PermissionField;
import hundsun.pdpm.annotation.PermissionObject;
import lombok.Data;
import hundsun.pdpm.annotation.Excel;
import java.io.Serializable;
import java.util.List;


/**
* @author yantt
* @date 2019-12-18
*/
@Data
@PermissionObject(tablecode = "scriptInfo",tablename = "脚本信息")
public class ScriptInfoDTO implements Serializable {

    // 标识符
    @Excel(title = "标识符",colwidth = 1)
    private String id;

    @Excel(title = "产品名称", dictname = "product_id")
    @PermissionField(fieldcode = "productId",fieldname = "产品",dictname = "product_id")
    private String productId;

    // 脚本类别
    @Excel(title = "脚本类别", dictname = "script_class")
    @PermissionField(fieldcode = "scriptClass",fieldname = "脚本类别",dictname = "script_class")
    private String scriptClass;

    @Excel(title = "模块", dictname = "module_type")
    @PermissionField(fieldcode = "moduleType",fieldname = "模块",dictname = "module_type")
    private String moduleType;

    // 脚本名称
    @Excel(title = "脚本名称")
    @PermissionField(fieldcode = "scriptName",fieldname = "脚本名称")
    private String scriptName;

    // 生效版本
    @Excel(title = "生效版本")
    @PermissionField(fieldcode = "effectVersion",fieldname = "生效版本")
    private String effectVersion;

    // 用户手册名称
    @Excel(title = "用户手册名称")
    private String userManualName;

    // 备注
    @Excel(title = "备注")
    private String memo;

    private List<FunctionInfoDTO> functions;
}
