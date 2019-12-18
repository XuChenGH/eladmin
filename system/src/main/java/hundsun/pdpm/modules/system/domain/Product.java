package hundsun.pdpm.modules.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @author yantt
* @date 2019-12-05
*/
@Entity
@Data
@Table(name="product")
public class Product implements Serializable {

    // ID
    @Id
    @Column(name = "id")
    private String id;

    // 产品名称
    @Column(name = "product_id",nullable = false)
    private String productId;

    // 版本号
    @Column(name = "version_no",nullable = false)
    private String versionNo;

    // 版本类型
    @Column(name = "version_type",nullable = false)
    private String versionType;

    // 基础版本号
    @Column(name = "base_version_no")
    private String baseVersionNo;

    // 发布状态
    @Column(name = "release_status")
    private String releaseStatus;

    // 发布时间
    @Column(name = "release_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp releaseTime;

    // 备注
    @Column(name = "memo")
    private String memo;

    public void copy(Product source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
