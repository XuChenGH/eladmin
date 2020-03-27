package hundsun.pdpm.modules.deliverconfig.service.mapper;

import hundsun.pdpm.base.BaseMapper;
import hundsun.pdpm.modules.deliverconfig.domain.DeliverConfig;
import hundsun.pdpm.modules.deliverconfig.service.dto.DeliverConfigDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author yantt
* @date 2020-02-22
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeliverConfigMapper extends BaseMapper<DeliverConfigDTO, DeliverConfig> {

}
