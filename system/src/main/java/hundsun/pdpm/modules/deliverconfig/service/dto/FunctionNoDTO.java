package hundsun.pdpm.modules.deliverconfig.service.dto;

import hundsun.pdpm.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

/**
 * @author ：yantt21019
 * @date ：Created in 2020/02/22 18:06
 * @description：
 * @version:
 */
@Data
public class FunctionNoDTO implements Serializable {

    @Excel(title = "功能编号")
    private String functionNo;

    @Excel(title = "是否控制",dictname = "yes_no")
    private String controled;

    @Excel(title = "功能脚本")
    private String functionScript;

}
