package hundsun.pdpm.modules.system.service.dto;

import hundsun.pdpm.annotation.Excel;
import hundsun.pdpm.annotation.PermissionField;
import hundsun.pdpm.annotation.PermissionObject;
import lombok.Data;

import java.io.Serializable;


/**
* @author yantt
* @date 2019-11-29
*/
@Data
@PermissionObject(tablecode = "customer",tablename = "客户信息表")
public class CustomerDTO implements Serializable {

    // ID
    @Excel(title = "标识符",colwidth = 1)
    private String id;

    // 客户类型
    @Excel(title = "客户类型", dictname = "cust_type")
    @PermissionField(fieldcode = "custType",fieldname = "客户类型", dictname = "cust_type")
    private String custType;

    // 地区
    @Excel(title = "地区", dictname = "area")
    @PermissionField(fieldcode = "area",fieldname = "地区", dictname = "area")
    private String area;

    // 客户名称
    @Excel(title = "客户名称")
    @PermissionField(fieldcode = "custName",fieldname = "客户名称")
    private String custName;

    // 备注
    @Excel(title = "备注")
    private String memo;
}
