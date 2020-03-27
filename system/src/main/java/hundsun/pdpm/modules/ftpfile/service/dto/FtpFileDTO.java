package hundsun.pdpm.modules.ftpfile.service.dto;

import lombok.Data;
import hundsun.pdpm.annotation.Excel;
import java.io.Serializable;
import java.util.List;


/**
* @author yantt
* @date 2019-12-31
*/
@Data
public class FtpFileDTO implements Serializable {

    // 标识符
    @Excel(title = "标识符")
    private Integer id;

    private Integer parentId;

    private String pathName;

    private Integer ftpId;
    // 文件名
    @Excel(title = "文件名")
    private String fileName;

    // 是否目录
    @Excel(title = "是否目录", dictname = "is_dir")
    private String isDir;

    // 文件大小
    @Excel(title = "文件大小")
    private Integer fileSize;

    // 最近访问时间
    @Excel(title = "最近访问时间")
    private String fileAtime;

    // 最近修改时间
    @Excel(title = "最近修改时间")
    private String fileMtime;

    private List<FtpFileDTO> children;
}
