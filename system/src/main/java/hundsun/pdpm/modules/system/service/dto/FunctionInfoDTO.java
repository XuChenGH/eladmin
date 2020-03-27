package hundsun.pdpm.modules.system.service.dto;

import hundsun.pdpm.annotation.PermissionField;
import hundsun.pdpm.annotation.PermissionObject;
import lombok.Data;
import hundsun.pdpm.annotation.Excel;
import java.io.Serializable;
import java.util.List;


/**
* @author yantt
* @date 2019-12-17
*/
@Data
@PermissionObject(tablecode = "functionInfo",tablename = "功能信息")
public class FunctionInfoDTO implements Serializable {

    // 标识符
    @Excel(title = "标识符",colwidth = 1)
    private String id;

    // 产品名称
    @Excel(title = "产品名称", dictname = "product_id")
    @PermissionField(fieldcode = "productId",fieldname = "产品",dictname = "product_id")
    private String productId;

    // 功能名称
    @Excel(title = "功能名称")
    @PermissionField(fieldcode = "functionName",fieldname = "功能名称")
    private String functionName;

    // 功能类型
    @Excel(title = "功能类型", dictname = "function_type")
    @PermissionField(fieldcode = "functionType",fieldname = "功能类型",dictname = "function_type")
    private String functionType;

    // 功能模式
    @Excel(title = "功能模式")
    @PermissionField(fieldcode = "functionMode",fieldname = "功能模式")
    private String functionMode;

    // 备注
    @Excel(title = "备注")
    private String memo;


    private List<ScriptInfoDTO> scripts;
}
