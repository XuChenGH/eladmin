package hundsun.pdpm.modules.ftpfile.service.mapper;

import hundsun.pdpm.base.BaseMapper;
import hundsun.pdpm.modules.ftpfile.domain.FtpUser;
import hundsun.pdpm.modules.ftpfile.service.dto.FtpUserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author yantt
* @date 2019-12-31
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FtpUserMapper extends BaseMapper<FtpUserDTO, FtpUser> {

}
