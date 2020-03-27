package hundsun.pdpm.modules.datapermission.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hundsun.pdpm.modules.system.domain.Role;
import hundsun.pdpm.modules.system.domain.User;
import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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

    @JsonIgnore
    @ManyToMany(mappedBy = "dataPermissions")
    private Set<Role> roles;

    @JsonIgnore
    @ManyToMany(mappedBy = "dataPermissions")
    private Set<User> users;

    @OneToMany(mappedBy = "table",cascade={CascadeType.PERSIST,CascadeType.REMOVE})
    private List<DataPermissionField> fields;

    public void copy(DataPermission source){
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
        DataPermission dataPermission = (DataPermission) o;
        return Objects.equals(id, dataPermission.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
