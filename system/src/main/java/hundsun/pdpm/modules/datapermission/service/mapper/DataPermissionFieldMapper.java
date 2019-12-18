package hundsun.pdpm.modules.datapermission.service.mapper;

import hundsun.pdpm.base.BaseMapper;
import hundsun.pdpm.modules.datapermission.domain.DataPermissionField;
import hundsun.pdpm.modules.datapermission.service.dto.DataPermissionFieldDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author yantt
* @date 2019-12-13
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DataPermissionFieldMapper extends BaseMapper<DataPermissionFieldDTO, DataPermissionField> {

}
