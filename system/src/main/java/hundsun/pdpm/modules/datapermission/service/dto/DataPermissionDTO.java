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

    private List<DataPermissionFieldDTO> fields;
}
