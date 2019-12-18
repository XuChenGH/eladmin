package hundsun.pdpm.modules.system.service.dto;

import lombok.Data;
import hundsun.pdpm.annotation.Excel;
import java.io.Serializable;


/**
* @author yantt
* @date 2019-12-05
*/
@Data
public class CustProductDTO implements Serializable {

    // ID
    @Excel(title = "ID")
    private String id;

    // 客户ID
    @Excel(title = "客户ID")
    private String custId;

    // 产品ID
    @Excel(title = "产品ID")
    private String productId;

    // 版本类型
    @Excel(title = "版本类型", dictname = "cust_version_type")
    private String versionType;

    private ProductDTO product;
}
