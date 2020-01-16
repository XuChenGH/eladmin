package hundsun.pdpm.modules.ftpfile.domain;

import hundsun.pdpm.modules.system.domain.Dept;
import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
* @author yantt
* @date 2019-12-31
*/
@Entity
@Data
@Table(name="ftp_file")
public class FtpFile implements Serializable {

    // 标识符
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @NotNull(groups = Dept.Update.class)
    private Integer id;

    @Column(name = "ftp_id")
    private Integer ftpId;

    @Column(name = "parent_id")
    private Integer parentId;

    // 文件名
    @Column(name = "path_name",nullable = false)
    private String pathName;
    // 文件名
    @Column(name = "file_name",nullable = false)
    private String fileName;

    // 是否目录
    @Column(name = "is_dir",nullable = false)
    private String isDir;

    // 文件大小
    @Column(name = "file_size")
    private Integer fileSize;

    // 最近访问时间
    @Column(name = "file_atime")
    private String fileAtime;

    // 最近修改时间
    @Column(name = "file_mtime")
    private String fileMtime;


    public void copy(FtpFile source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
