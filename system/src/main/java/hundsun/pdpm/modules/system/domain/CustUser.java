package hundsun.pdpm.modules.system.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
* @author yantt
* @date 2019-11-30
*/
@Entity
@Data
@Table(name="cust_user")
public class CustUser implements Serializable {

    // ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // 客户ID
    @Column(name = "cust_id",nullable = false)
    private String custId;

    // 用户ID
    @OneToOne
    @JoinColumn(name = "user_id")
    @NotFound(action= NotFoundAction.IGNORE)
    private User user;



    public void copy(CustUser source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
