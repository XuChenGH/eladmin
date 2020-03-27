package hundsun.pdpm.modules.deliverconfig.service.mapper;

import hundsun.pdpm.base.BaseMapper;
import hundsun.pdpm.modules.deliverconfig.domain.FunIdAccount;
import hundsun.pdpm.modules.deliverconfig.service.dto.FunIdAccountDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author yantt
* @date 2020-02-23
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FunIdAccountMapper extends BaseMapper<FunIdAccountDTO, FunIdAccount> {

}
