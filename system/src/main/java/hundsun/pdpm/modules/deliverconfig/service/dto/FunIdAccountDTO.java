package hundsun.pdpm.modules.deliverconfig.service.dto;

import lombok.Data;
import hundsun.pdpm.annotation.Excel;
import java.io.Serializable;


/**
* @author yantt
* @date 2020-02-23
*/
@Data
public class FunIdAccountDTO implements Serializable {

    // 标识符
    @Excel(title = "标识符")
    private String id;

    // 功能编号
    @Excel(title = "产品名称",dictname = "product_id")
    private String productId;


    // 功能编号
    @Excel(title = "功能编号")
    private String functionNo;

    // 是否控制
    @Excel(title = "是否控制", dictname = "yes_no")
    private String controled;

    // 功能脚本
    @Excel(title = "功能脚本")
    private String scripts;
}
