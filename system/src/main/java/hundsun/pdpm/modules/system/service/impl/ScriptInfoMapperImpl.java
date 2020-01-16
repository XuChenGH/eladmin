package hundsun.pdpm.modules.system.service.impl;

import hundsun.pdpm.modules.system.domain.FunctionInfo;
import hundsun.pdpm.modules.system.domain.FunctionScript;
import hundsun.pdpm.modules.system.domain.ScriptInfo;
import hundsun.pdpm.modules.system.repository.FunctionInfoRepository;
import hundsun.pdpm.modules.system.repository.FunctionScriptRepository;
import hundsun.pdpm.modules.system.service.FunctionScriptService;
import hundsun.pdpm.modules.system.service.dto.ScriptInfoDTO;
import hundsun.pdpm.modules.system.service.mapper.FunctionInfoMapper;
import hundsun.pdpm.modules.system.service.mapper.ScriptInfoMapper;
import hundsun.pdpm.utils.SecurityUtils;
import hundsun.pdpm.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ScriptInfoMapperImpl implements ScriptInfoMapper {


    @Autowired
    private FunctionScriptService functionScriptService;

    @Override
    public ScriptInfo toEntity(ScriptInfoDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ScriptInfo scriptInfo = new ScriptInfo();

        scriptInfo.setId( dto.getId() );
        scriptInfo.setProductId(dto.getProductId());
        scriptInfo.setModuleType(dto.getModuleType());
        scriptInfo.setScriptClass( dto.getScriptClass() );
        scriptInfo.setScriptName( dto.getScriptName() );
        scriptInfo.setEffectVersion( dto.getEffectVersion() );
        scriptInfo.setUserManualName( dto.getUserManualName() );
        scriptInfo.setMemo( dto.getMemo() );

        return scriptInfo;
    }

    @Override
    public ScriptInfoDTO toDto(ScriptInfo entity,boolean haveFunc) {
        if ( entity == null ) {
            return null;
        }

        ScriptInfoDTO scriptInfoDTO = new ScriptInfoDTO();
        if(haveFunc){
            scriptInfoDTO.setFunctions(functionScriptService.getFunctionByScriptId(entity.getId()));
        }
        scriptInfoDTO.setId( entity.getId() );
        scriptInfoDTO.setProductId(entity.getProductId());
        scriptInfoDTO.setModuleType(entity.getModuleType());
        scriptInfoDTO.setScriptClass( entity.getScriptClass() );
        scriptInfoDTO.setScriptName( entity.getScriptName() );
        scriptInfoDTO.setEffectVersion( entity.getEffectVersion() );
        scriptInfoDTO.setUserManualName( entity.getUserManualName() );
        scriptInfoDTO.setMemo( entity.getMemo() );
        return scriptInfoDTO;
    }

    @Override
    public List<ScriptInfo> toEntity(List<ScriptInfoDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<ScriptInfo> list = new ArrayList<ScriptInfo>( dtoList.size() );
        for ( ScriptInfoDTO scriptInfoDTO : dtoList ) {
            list.add( toEntity( scriptInfoDTO ) );
        }

        return list;
    }

    @Override
    public List<ScriptInfoDTO> toDto(List<ScriptInfo> entityList,boolean haveFunc) {
        if ( entityList == null ) {
            return null;
        }

        List<ScriptInfoDTO> list = new ArrayList<ScriptInfoDTO>( entityList.size() );
        for ( ScriptInfo scriptInfo : entityList ) {
            list.add( toDto( scriptInfo,haveFunc ) );
        }

        return list;
    }
}
