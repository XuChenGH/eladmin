package hundsun.pdpm.modules.ftpfile.service.dto;

import hundsun.pdpm.modules.ftpfile.domain.FtpUser;
import lombok.Data;

import java.io.Serializable;

/**
 * @author ：yantt21019
 * @date ：Created in 2019/12/31 14:48
 * @description：
 * @version:
 */
@Data
public class RefreshDTO implements Serializable {

    FtpUser user;
    String pathName;
    int parentId;
}
