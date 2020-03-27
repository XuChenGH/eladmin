package hundsun.pdpm.modules.system.service.mapper;

import hundsun.pdpm.base.BaseMapper;
import hundsun.pdpm.modules.system.domain.Delivery;
import hundsun.pdpm.modules.system.service.dto.DeliveryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author yantt
* @date 2019-12-25
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeliveryMapper extends BaseMapper<DeliveryDTO, Delivery> {

}
