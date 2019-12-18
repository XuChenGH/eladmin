package hundsun.pdpm.modules.datapermission.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import java.io.Serializable;

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
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "permission_id")
    private DataPermission table;

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
}
