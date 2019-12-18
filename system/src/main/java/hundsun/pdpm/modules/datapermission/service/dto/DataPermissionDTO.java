package hundsun.pdpm.modules.datapermission.service.dto;

import hundsun.pdpm.modules.datapermission.domain.DataPermissionField;
import lombok.Data;
import hundsun.pdpm.annotation.Excel;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.List;


/**
* @author yantt
* @date 2019-12-13
*/
@Data
public class DataPermissionDTO implements Serializable {

    // 标识符
    @Excel(title = "标识符")
    private String id;

    // 方案名
    @Excel(title = "方案名")
    private String name;

    // 角色
    @Excel(title = "角色")
    private Long roleId;

    // 表代码
    @Excel(title = "表代码")
    private String tableCode;

    // 表名称
    @Excel(title = "表名称")
    private String tableName;


    private String className;

    private List<DataPermissionFieldDTO> fields;
}
