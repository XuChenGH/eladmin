package hundsun.pdpm.modules.datapermission.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
* @author yantt
* @date 2019-12-13
*/
@Entity
@Data
@Table(name="data_permission")
public class DataPermission implements Serializable {

    // 标识符
    @Id
    @Column(name = "id")
    private String id;

    // 方案名
    @Column(name = "name",nullable = false)
    private String name;

    // 角色
    @Column(name = "role_id",nullable = false)
    private Long roleId;

    // 表代码
    @Column(name = "table_code",nullable = false)
    private String tableCode;

    // 表名称
    @Column(name = "table_name",nullable = false)
    private String tableName;

    @OneToMany(mappedBy = "table",cascade={CascadeType.PERSIST,CascadeType.REMOVE})
    private List<DataPermissionField> fields;

    public void copy(DataPermission source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
