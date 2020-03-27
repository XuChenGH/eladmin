package hundsun.pdpm.modules.system.service.impl;

import hundsun.pdpm.modules.system.domain.FunctionInfo;
import hundsun.pdpm.modules.system.domain.ScriptInfo;
import hundsun.pdpm.modules.system.repository.ScriptInfoRepository;
import hundsun.pdpm.modules.system.service.FunctionScriptService;
import hundsun.pdpm.modules.system.service.ScriptInfoService;
import hundsun.pdpm.modules.system.service.dto.FunctionInfoDTO;
import hundsun.pdpm.modules.system.service.dto.ScriptInfoDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

import hundsun.pdpm.modules.system.service.mapper.FunctionInfoMapper;
import hundsun.pdpm.modules.system.service.mapper.ScriptInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FunctionInfoMapperImpl implements FunctionInfoMapper {

    @Autowired
    private FunctionScriptService functionScriptService;

    @Override
    public FunctionInfo toEntity(FunctionInfoDTO dto) {
        if ( dto == null ) {
            return null;
        }

        FunctionInfo functionInfo = new FunctionInfo();

        functionInfo.setId( dto.getId() );
        functionInfo.setProductId( dto.getProductId() );
        functionInfo.setFunctionName( dto.getFunctionName() );
        functionInfo.setFunctionType( dto.getFunctionType() );
        functionInfo.setFunctionMode( dto.getFunctionMode() );
        functionInfo.setMemo( dto.getMemo() );

        return functionInfo;
    }

    @Override
    public FunctionInfoDTO toDto(FunctionInfo entity,boolean haveScript) {
        if ( entity == null ) {
            return null;
        }

        FunctionInfoDTO functionInfoDTO = new FunctionInfoDTO();

        functionInfoDTO.setId( entity.getId() );
        functionInfoDTO.setProductId( entity.getProductId() );
        functionInfoDTO.setFunctionName( entity.getFunctionName() );
        functionInfoDTO.setFunctionType( entity.getFunctionType() );
        functionInfoDTO.setFunctionMode( entity.getFunctionMode() );
        functionInfoDTO.setMemo( entity.getMemo() );
        if(haveScript){
            functionInfoDTO.setScripts(functionScriptService.getScriptByFunctionId(entity.getId()));
        }
        return functionInfoDTO;
    }

    @Override
    public List<FunctionInfo> toEntity(List<FunctionInfoDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<FunctionInfo> list = new ArrayList<FunctionInfo>( dtoList.size() );
        for ( FunctionInfoDTO functionInfoDTO : dtoList ) {
            list.add( toEntity( functionInfoDTO ) );
        }

        return list;
    }

    @Override
    public List<FunctionInfoDTO> toDto(List<FunctionInfo> entityList,boolean haveScript) {
        if ( entityList == null ) {
            return null;
        }

        List<FunctionInfoDTO> list = new ArrayList<FunctionInfoDTO>( entityList.size() );
        for ( FunctionInfo functionInfo : entityList ) {
            list.add( toDto( functionInfo,haveScript ) );
        }

        return list;
    }
}
