package hundsun.pdpm.modules.ftpfile.service.dto;

import lombok.Data;
import hundsun.pdpm.annotation.Excel;
import java.io.Serializable;


/**
* @author yantt
* @date 2019-12-31
*/
@Data
public class FtpUserDTO implements Serializable {

    // 标识符
    @Excel(title = "标识符")
    private Integer id;

    // 用户名称
    @Excel(title = "用户名称")
    private String username;

    private String name;

    // 密码
    @Excel(title = "密码")
    private String password;

    // ftp主机地址
    @Excel(title = "ftp主机地址")
    private String sftpHost;

    // ftp主机端口
    @Excel(title = "ftp主机端口")
    private Integer sftpPort;

    // 默认访问路径
    @Excel(title = "默认访问路径")
    private String sftpPath;

    // 使用类型
    @Excel(title = "使用类型", dictname = "sftp_use_type")
    private String sftpUseType;
}
