package hundsun.pdpm.modules.datapermission.service.dto;

import lombok.Data;
import hundsun.pdpm.annotation.Excel;
import java.io.Serializable;


/**
* @author yantt
* @date 2019-12-13
*/
@Data
public class DataPermissionFieldDTO implements Serializable {

    // 标识符
    @Excel(title = "标识符")
    private String id;

    // 数据权限标识符
    @Excel(title = "数据权限标识符")
    private String permissionId;

    // 字段代码
    @Excel(title = "字段代码")
    private String fieldCode;

    // 字段名称
    @Excel(title = "字段名称")
    private String fieldName;

    // 字典代码
    @Excel(title = "字典代码")
    private String dictName;

    // 操作类型
    @Excel(title = "操作类型", dictname = "operate_type")
    private String operateType;

    // 操作值
    @Excel(title = "操作值")
    private String operateValue;
}
