package hundsun.pdpm.modules.system.rest;

import hundsun.pdpm.aop.log.Log;
import java.util.List;
import hundsun.pdpm.modules.system.domain.ScriptInfo;
import hundsun.pdpm.modules.system.service.ScriptInfoService;
import hundsun.pdpm.modules.system.service.dto.ScriptInfoDTO;
import hundsun.pdpm.modules.system.service.dto.ScriptInfoQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
/**
* @author yantt
* @date 2019-12-18
*/
@Api(tags = "ScriptInfo管理")
@RestController
@RequestMapping("/api/scriptInfo")
public class ScriptInfoController {

    private final ScriptInfoService scriptInfoService;

    public ScriptInfoController(ScriptInfoService scriptInfoService) {
        this.scriptInfoService = scriptInfoService;
    }

    @Log("导出脚本信息数据")
    @ApiOperation("导出脚本信息数据")
    @PostMapping(value = "/download")
    @PreAuthorize("@el.check('scriptInfo:export')")
    public void download(HttpServletResponse response,@RequestBody List<ScriptInfoDTO> data) throws IOException {
        List<ScriptInfoDTO> scriptInfoDTOList;
        if(CollectionUtils.isEmpty(data)){
           scriptInfoDTOList = scriptInfoService.queryAll(new ScriptInfoQueryCriteria());
        }else{
           scriptInfoDTOList = scriptInfoService.findByIdlist(data);
        }
        scriptInfoService.download(scriptInfoDTOList, response);
    }

    @Log("导入脚本信息数据")
    @ApiOperation("导入脚本信息数据")
    @PostMapping(value = "/upload")
    @PreAuthorize("@el.check('scriptInfo:import')")
    public  ResponseEntity upload(HttpServletResponse response,@RequestParam("file") MultipartFile file)throws Exception{
       return new ResponseEntity<>(scriptInfoService.upload(file),HttpStatus.CREATED);
    }

    @GetMapping
    @Log("查询脚本信息")
    @ApiOperation("查询脚本信息")
    @PreAuthorize("@el.check('scriptInfo:list')")
    public ResponseEntity getScriptInfos(ScriptInfoQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(scriptInfoService.queryAll(criteria,pageable,true),HttpStatus.OK);
    }
    @GetMapping(value = "/nofunc")
    @Log("查询脚本信息")
    @ApiOperation("查询脚本信息")
    @PreAuthorize("@el.check('scriptInfo:list')")
    public ResponseEntity getScriptInfosNoFunction(ScriptInfoQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(scriptInfoService.queryAll(criteria,pageable,false),HttpStatus.OK);
    }
    @PostMapping
    @Log("新增脚本信息")
    @ApiOperation("新增脚本信息")
    @PreAuthorize("@el.check('scriptInfo:add')")
    public ResponseEntity create(@Validated @RequestBody ScriptInfo resources){
        return new ResponseEntity<>(scriptInfoService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改脚本信息")
    @ApiOperation("修改脚本信息")
    @PreAuthorize("@el.check('scriptInfo:edit')")
    public ResponseEntity update(@Validated @RequestBody ScriptInfo resources){
        scriptInfoService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除脚本信息")
    @ApiOperation("删除脚本信息")
    @PreAuthorize("@el.check('scriptInfo:del')")
    public ResponseEntity delete(@PathVariable String id){
        scriptInfoService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
