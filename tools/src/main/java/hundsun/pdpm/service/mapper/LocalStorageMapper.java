package hundsun.pdpm.service.mapper;

import hundsun.pdpm.base.BaseMapper;
import hundsun.pdpm.domain.LocalStorage;
import hundsun.pdpm.service.dto.LocalStorageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author Zheng Jie
* @date 2019-09-05
*/
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LocalStorageMapper extends BaseMapper<LocalStorageDTO, LocalStorage> {

}