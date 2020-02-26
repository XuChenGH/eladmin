package hundsun.pdpm.modules.system.service.mapper;

import hundsun.pdpm.base.BaseMapper;
import hundsun.pdpm.modules.system.domain.FunctionScript;
import hundsun.pdpm.modules.system.service.dto.FunctionScriptDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author yantt
* @date 2019-12-27
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FunctionScriptMapper extends BaseMapper<FunctionScriptDTO, FunctionScript> {

}
