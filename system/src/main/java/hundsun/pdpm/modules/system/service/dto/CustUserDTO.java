package hundsun.pdpm.modules.system.service.dto;

import hundsun.pdpm.modules.system.domain.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.Set;


/**
* @author yantt
* @date 2019-11-30
*/
@Data
public class CustUserDTO implements Serializable {

    // ID
    private Long id;

    // 客户ID
    private String custId;

    // 用户ID
    private Long userId;

    private UserDTO user;

}
