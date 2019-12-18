package hundsun.pdpm.modules.system.service.mapper;


import hundsun.pdpm.base.BaseMapper;
import hundsun.pdpm.modules.system.domain.CustUser;
import hundsun.pdpm.modules.system.service.dto.CustUserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author yantt
* @date 2019-11-30
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustUserMapper extends BaseMapper<CustUserDTO, CustUser> {

}