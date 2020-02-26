package hundsun.pdpm.modules.deliverconfig.service.dto;

import lombok.Data;
import hundsun.pdpm.annotation.Excel;
import java.io.Serializable;


/**
* @author yantt
* @date 2020-02-22
*/
@Data
public class DeliverConfigDTO implements Serializable {

    // 标识符
    @Excel(title = "标识符")
    private Integer id;

    // 产品名称
    @Excel(title = "产品名称", dictname = "product_id")
    private String productId;

    // 地址
    @Excel(title = "地址")
    private String address;

    private String username;
}
