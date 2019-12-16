package hundsun.pdpm.modules.system.service.mapper;

import hundsun.pdpm.base.BaseMapper;
import hundsun.pdpm.modules.system.domain.Role;
import hundsun.pdpm.modules.system.service.dto.RoleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author Zheng Jie
 * @date 2018-11-23
 */
@Mapper(componentModel = "spring", uses = {MenuMapper.class, DeptMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper extends BaseMapper<RoleDTO, Role> {

}
