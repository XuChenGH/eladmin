package hundsun.pdpm.modules.system.service.mapper;

import hundsun.pdpm.base.BaseMapper;
import hundsun.pdpm.modules.system.domain.FunctionInfo;
import hundsun.pdpm.modules.system.service.dto.FunctionInfoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author yantt
* @date 2019-12-17
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FunctionInfoMapper extends BaseMapper<FunctionInfoDTO, FunctionInfo> {

}
