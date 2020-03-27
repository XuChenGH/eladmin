package hundsun.pdpm.modules.system.service.dto;

import lombok.Data;
import hundsun.pdpm.annotation.Excel;
import java.io.Serializable;


/**
* @author yantt
* @date 2019-12-27
*/
@Data
public class FunctionScriptDTO implements Serializable {

    // 标识符
    @Excel(title = "标识符")
    private String id;

    // 功能标识符
    @Excel(title = "功能标识符")
    private String functionId;

    // 脚本标识符
    @Excel(title = "脚本标识符")
    private String scriptId;
}
