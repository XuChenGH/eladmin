package hundsun.pdpm.modules.system.service.mapper;

import hundsun.pdpm.base.BaseMapper;
import hundsun.pdpm.modules.system.domain.Job;
import hundsun.pdpm.modules.system.service.dto.JobSmallDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author Zheng Jie
* @date 2019-03-29
*/
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JobSmallMapper extends BaseMapper<JobSmallDTO, Job> {

}