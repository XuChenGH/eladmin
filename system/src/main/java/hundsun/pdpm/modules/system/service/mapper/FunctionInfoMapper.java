package hundsun.pdpm.modules.system.service.mapper;

import hundsun.pdpm.base.BaseMapper;
import hundsun.pdpm.modules.system.domain.FunctionInfo;
import hundsun.pdpm.modules.system.domain.ScriptInfo;
import hundsun.pdpm.modules.system.service.dto.FunctionInfoDTO;
import hundsun.pdpm.modules.system.service.dto.ScriptInfoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;

/**
* @author yantt
* @date 2019-12-17
*/
@Component
public interface FunctionInfoMapper  {

    /**
     * DTO转Entity
     */
    FunctionInfo toEntity(FunctionInfoDTO dto);

    /**
     * Entity转DTO
     */
    FunctionInfoDTO toDto(FunctionInfo entity,boolean haveScript);

    /**
     * DTO集合转Entity集合
     */
    List<FunctionInfo> toEntity(List<FunctionInfoDTO> dtoList);

    /**
     * Entity集合转DTO集合
     */
    List <FunctionInfoDTO> toDto(List<FunctionInfo> entityList,boolean haveScript);
}
