package hundsun.pdpm.service.mapper;

import hundsun.pdpm.base.BaseMapper;
import hundsun.pdpm.domain.Log;
import hundsun.pdpm.service.dto.LogSmallDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author Zheng Jie
 * @date 2019-5-22
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LogSmallMapper extends BaseMapper<LogSmallDTO, Log> {

}