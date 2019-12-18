package hundsun.pdpm.modules.system.service.dto;

import hundsun.pdpm.annotation.PermissionField;
import hundsun.pdpm.annotation.PermissionObject;
import lombok.Data;
import hundsun.pdpm.annotation.Excel;
import java.sql.Timestamp;
import java.io.Serializable;


/**
* @author yantt
* @date 2019-12-05
*/
@Data
@PermissionObject(tablecode = "product",tablename = "产品信息")
public class ProductDTO implements Serializable {

    // ID
    @Excel(title = "ID")
    private String id;

    // 产品名称
    @Excel(title = "产品名称", dictname = "product_id")
    @PermissionField(fieldcode = "productId",fieldname = "产品名称",dictname = "product_id")
    private String productId;

    // 版本号
    @Excel(title = "版本号")
    @PermissionField(fieldcode = "versionNo",fieldname = "版本号")
    private String versionNo;

    // 版本类型
    @Excel(title = "版本类型", dictname = "version_type")
    @PermissionField(fieldcode = "versionType",fieldname = "版本类型",dictname = "version_type")
    private String versionType;

    // 基础版本号
    @Excel(title = "基础版本号")
    private String baseVersionNo;

    // 发布状态
    @Excel(title = "发布状态", dictname = "release_status")
    @PermissionField(fieldcode = "releaseStatus",fieldname = "发布状态",dictname = "release_status")
    private String releaseStatus;

    // 发布时间
    @Excel(title = "发布时间")
    private Timestamp releaseTime;

    // 备注
    @Excel(title = "备注")
    private String memo;
}
