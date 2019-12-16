package hundsun.pdpm.modules.system.service.mapper;

import hundsun.pdpm.base.BaseMapper;
import hundsun.pdpm.modules.system.domain.Role;
import hundsun.pdpm.modules.system.service.dto.RoleSmallDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author Zheng Jie
 * @date 2019-5-23
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleSmallMapper extends BaseMapper<RoleSmallDTO, Role> {

}
