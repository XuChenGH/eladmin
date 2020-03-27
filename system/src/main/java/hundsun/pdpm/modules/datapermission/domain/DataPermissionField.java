package hundsun.pdpm.modules.datapermission.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hundsun.pdpm.modules.system.domain.Role;
import hundsun.pdpm.modules.system.domain.User;
import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
* @author yantt
* @date 2019-12-13
*/
@Entity
@Data
@Table(name="data_permission_field")
public class DataPermissionField implements Serializable {

    // 标识符
    @Id
    @Column(name = "id")
    private String id;

    // 数据权限标识符
    @JsonIgnore
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "permission_id")
    private DataPermission table;

    // 表代码
    @Column(name = "table_code",nullable = false)
    private String tableCode;

    // 表名称
    @Column(name = "table_name",nullable = false)
    private String tableName;


    // 字段代码
    @Column(name = "field_code",nullable = false)
    private String fieldCode;

    // 字段名称
    @Column(name = "field_name",nullable = false)
    private String fieldName;

    // 字典代码
    @Column(name = "dict_name")
    private String dictName;

    // 操作类型
    @Column(name = "operate_type",nullable = false)
    private String operateType;

    // 操作值
    @Column(name = "operate_value",nullable = false)
    private String operateValue;



    public void copy(DataPermissionField source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DataPermissionField dataPermissionField = (DataPermissionField) o;
        return Objects.equals(id, dataPermissionField.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
