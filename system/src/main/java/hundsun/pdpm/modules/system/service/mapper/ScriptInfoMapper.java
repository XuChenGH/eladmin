package hundsun.pdpm.modules.system.service.mapper;

import hundsun.pdpm.base.BaseMapper;
import hundsun.pdpm.modules.system.domain.ScriptInfo;
import hundsun.pdpm.modules.system.service.dto.ScriptInfoDTO;
import org.springframework.stereotype.Component;

import java.util.List;


/**
* @author yantt
* @date 2019-12-18
*/
@Component
public interface ScriptInfoMapper{
    /**
     * DTO转Entity
     */
    ScriptInfo toEntity(ScriptInfoDTO dto);

    /**
     * Entity转DTO
     */
    ScriptInfoDTO toDto(ScriptInfo entity,boolean haveFunc);

    /**
     * DTO集合转Entity集合
     */
    List <ScriptInfo> toEntity(List<ScriptInfoDTO> dtoList);

    /**
     * Entity集合转DTO集合
     */
    List <ScriptInfoDTO> toDto(List<ScriptInfo> entityList,boolean haveFunc);
}
