package hundsun.pdpm.modules.ftpfile.service.mapper;

import hundsun.pdpm.base.BaseMapper;
import hundsun.pdpm.modules.ftpfile.domain.FtpFile;
import hundsun.pdpm.modules.ftpfile.service.dto.FtpFileDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author yantt
* @date 2019-12-31
*/
@Mapper
public interface FtpFileMapper extends BaseMapper<FtpFileDTO, FtpFile> {

}
