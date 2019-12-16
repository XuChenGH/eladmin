package hundsun.pdpm.modules.system.service.mapper;

import hundsun.pdpm.base.BaseMapper;
import hundsun.pdpm.modules.system.domain.Customer;
import hundsun.pdpm.modules.system.service.dto.CustomerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author yantt
* @date 2019-11-29
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerMapper extends BaseMapper<CustomerDTO, Customer> {

}