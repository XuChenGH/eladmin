package hundsun.pdpm.modules.system.service.mapper;

import hundsun.pdpm.base.BaseMapper;
import hundsun.pdpm.modules.system.domain.Dept;
import hundsun.pdpm.modules.system.service.dto.DeptDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author Zheng Jie
* @date 2019-03-25
*/
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeptMapper extends BaseMapper<DeptDTO, Dept> {

}