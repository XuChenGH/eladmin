package hundsun.pdpm.modules.datapermission.service.dto;

import hundsun.pdpm.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author ：yantt21019
 * @date ：Created in 2020/03/17 16:31
 * @description：
 * @version:
 */
@Data
public class DataPermissionCustomDTO implements Serializable {

    // 表代码
    private String tableCode;

    // 表名称
    private String tableName;


    private String className;

    private List<DataPermissionFieldDTO> fields;

}
