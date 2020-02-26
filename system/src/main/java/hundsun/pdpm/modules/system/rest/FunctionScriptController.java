package hundsun.pdpm.modules.system.rest;

import hundsun.pdpm.aop.log.Log;
import java.util.List;
import hundsun.pdpm.modules.system.domain.FunctionScript;
import hundsun.pdpm.modules.system.service.FunctionScriptService;
import hundsun.pdpm.modules.system.service.dto.FunctionScriptDTO;
import hundsun.pdpm.modules.system.service.dto.FunctionScriptQueryCriteria;
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
* @date 2019-12-27
*/
@Api(tags = "FunctionScript管理")
@RestController
@RequestMapping("/api/functionScript")
public class FunctionScriptController {

    private final FunctionScriptService functionScriptService;

    public FunctionScriptController(FunctionScriptService functionScriptService) {
        this.functionScriptService = functionScriptService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @PostMapping(value = "/download")
    @PreAuthorize("@el.check('functionInfo:export')")
    public void download(HttpServletResponse response,@RequestBody List<FunctionScriptDTO> data) throws IOException {
        List<FunctionScriptDTO> functionScriptDTOList;
        if(CollectionUtils.isEmpty(data)){
           functionScriptDTOList = functionScriptService.queryAll(new FunctionScriptQueryCriteria());
        }else{
           functionScriptDTOList = functionScriptService.findByIdlist(data);
        }
        functionScriptService.download(functionScriptDTOList, response);
    }

    @Log("导入数据")
    @ApiOperation("导入数据")
    @PostMapping(value = "/upload")
    @PreAuthorize("@el.check('functionInfo:import')")
    public  ResponseEntity upload(HttpServletResponse response,@RequestParam("file") MultipartFile file)throws Exception{
       return new ResponseEntity<>(functionScriptService.upload(file),HttpStatus.CREATED);
    }

    @GetMapping
    @Log("查询功能脚本关联表")
    @ApiOperation("查询功能脚本关联表")
    @PreAuthorize("@el.check('functionInfo:list')")
    public ResponseEntity getFunctionScripts(FunctionScriptQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(functionScriptService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增功能脚本关联表")
    @ApiOperation("新增功能脚本关联表")
    @PreAuthorize("@el.check('functionInfo:add')")
    public ResponseEntity create(@Validated @RequestBody FunctionScript resources){
        return new ResponseEntity<>(functionScriptService.create(resources),HttpStatus.CREATED);
    }
    @PostMapping(value = "/addlist")
    @Log("新增功能脚本关联表")
    @ApiOperation("新增功能脚本关联表")
    @PreAuthorize("@el.check('functionInfo:add')")
    public void createBatch(@Validated @RequestBody List<FunctionScript> resources){
        functionScriptService.create(resources);
    }
    @PutMapping
    @Log("修改功能脚本关联表")
    @ApiOperation("修改功能脚本关联表")
    @PreAuthorize("@el.check('functionInfo:edit')")
    public ResponseEntity update(@Validated @RequestBody FunctionScript resources){
        functionScriptService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{functionId}/{scriptId}")
    @Log("删除功能脚本关联表")
    @ApiOperation("删除功能脚本关联表")
    @PreAuthorize("@el.check('functionInfo:del')")
    public ResponseEntity delete(@PathVariable(value = "functionId") String functionId,@PathVariable(value = "scriptId") String scriptId){
        functionScriptService.delete(functionId,scriptId);
        return new ResponseEntity(HttpStatus.OK);
    }
}
