package hundsun.pdpm.modules.system.rest;

import hundsun.pdpm.aop.log.Log;
import java.util.List;
import hundsun.pdpm.modules.system.domain.FunctionInfo;
import hundsun.pdpm.modules.system.service.FunctionInfoService;
import hundsun.pdpm.modules.system.service.dto.FunctionInfoDTO;
import hundsun.pdpm.modules.system.service.dto.FunctionInfoQueryCriteria;
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
* @date 2019-12-17
*/
@Api(tags = "FunctionInfo管理")
@RestController
@RequestMapping("/api/functionInfo")
public class FunctionInfoController {

    private final FunctionInfoService functionInfoService;

    public FunctionInfoController(FunctionInfoService functionInfoService) {
        this.functionInfoService = functionInfoService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @PostMapping(value = "/download")
    @PreAuthorize("@el.check('functionInfo:export')")
    public void download(HttpServletResponse response,@RequestBody List<FunctionInfoDTO> data) throws IOException {
        List<FunctionInfoDTO> functionInfoDTOList;
        if(CollectionUtils.isEmpty(data)){
           functionInfoDTOList = functionInfoService.queryAll(new FunctionInfoQueryCriteria());
        }else{
           functionInfoDTOList = functionInfoService.findByIdlist(data);
        }
        functionInfoService.download(functionInfoDTOList, response);
    }

    @Log("导入数据")
    @ApiOperation("导入数据")
    @PostMapping(value = "/upload")
    @PreAuthorize("@el.check('functionInfo:import')")
    public  ResponseEntity upload(HttpServletResponse response,@RequestParam("file") MultipartFile file)throws Exception{
       return new ResponseEntity<>(functionInfoService.upload(file),HttpStatus.CREATED);
    }

    @GetMapping
    @Log("查询功能信息")
    @ApiOperation("查询功能信息")
    @PreAuthorize("@el.check('functionInfo:list')")
    public ResponseEntity getFunctionInfos(FunctionInfoQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(functionInfoService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增功能信息")
    @ApiOperation("新增功能信息")
    @PreAuthorize("@el.check('functionInfo:add')")
    public ResponseEntity create(@Validated @RequestBody FunctionInfo resources){
        return new ResponseEntity<>(functionInfoService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改功能信息")
    @ApiOperation("修改功能信息")
    @PreAuthorize("@el.check('functionInfo:edit')")
    public ResponseEntity update(@Validated @RequestBody FunctionInfo resources){
        functionInfoService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除功能信息")
    @ApiOperation("删除功能信息")
    @PreAuthorize("@el.check('functionInfo:del')")
    public ResponseEntity delete(@PathVariable String id){
        functionInfoService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
