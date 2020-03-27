package hundsun.pdpm.modules.ftpfile.service.dto;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import lombok.Data;

import java.io.Serializable;

/**
 * @author ：yantt21019
 * @date ：Created in 2019/12/31 10:45
 * @description：
 * @version:
 */
@Data
public class SftpDTO implements Serializable {
     JSch ftp;
     ChannelSftp sftp;
     Channel channel;
     Session sshSession;
     boolean bLogin;

     public SftpDTO(){
         this.sftp = null;
         this.channel = null;
         this.sshSession= null;
         this.bLogin = false;
     }
}
