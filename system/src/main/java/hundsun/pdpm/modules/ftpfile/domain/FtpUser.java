package hundsun.pdpm.modules.ftpfile.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import java.io.Serializable;

/**
* @author yantt
* @date 2019-12-31
*/
@Entity
@Data
@Table(name="ftp_user")
public class FtpUser implements Serializable {

    // 标识符
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 用户名称
    @Column(name = "username",nullable = false)
    private String username;
    // 用户名称
    @Column(name = "name",nullable = false)
    private String name;
    // 密码
    @Column(name = "password",nullable = false)
    private String password;

    // ftp主机地址
    @Column(name = "sftp_host",nullable = false)
    private String sftpHost;

    // ftp主机端口
    @Column(name = "sftp_port",nullable = false)
    private Integer sftpPort;

    // 默认访问路径
    @Column(name = "sftp_path",nullable = false)
    private String sftpPath;

    // 使用类型
    @Column(name = "sftp_use_type")
    private String sftpUseType;

    public void copy(FtpUser source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
