package hundsun.pdpm.modules.system.service.dto;

import lombok.Data;
import hundsun.pdpm.annotation.Excel;
import java.io.Serializable;


/**
* @author yantt
* @date 2019-12-17
*/
@Data
public class FunctionInfoDTO implements Serializable {

    // 标识符
    @Excel(title = "标识符")
    private String id;

    // 产品名称
    @Excel(title = "产品名称", dictname = "product_id")
    private String productId;

    // 功能名称
    @Excel(title = "功能名称")
    private String functionName;

    // 功能类型
    @Excel(title = "功能类型", dictname = "function_type")
    private String functionType;

    // 功能模式
    @Excel(title = "功能模式", dictname = "function_mode")
    private String functionMode;

    // 备注
    @Excel(title = "备注")
    private String memo;
}
