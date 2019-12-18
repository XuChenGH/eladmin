package hundsun.pdpm.modules.datapermission.rest;

import hundsun.pdpm.aop.log.Log;
import java.util.List;
import hundsun.pdpm.modules.datapermission.domain.DataPermissionField;
import hundsun.pdpm.modules.datapermission.service.DataPermissionFieldService;
import hundsun.pdpm.modules.datapermission.service.dto.DataPermissionFieldDTO;
import hundsun.pdpm.modules.datapermission.service.dto.DataPermissionFieldQueryCriteria;
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
* @date 2019-12-13
*/
@Api(tags = "DataPermissionField管理")
@RestController
@RequestMapping("/api/dataPermissionField")
public class DataPermissionFieldController {

    private final DataPermissionFieldService dataPermissionFieldService;

    public DataPermissionFieldController(DataPermissionFieldService dataPermissionFieldService) {
        this.dataPermissionFieldService = dataPermissionFieldService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @PostMapping(value = "/download")
    @PreAuthorize("@el.check('dataPermissionField:export')")
    public void download(HttpServletResponse response,@RequestBody List<DataPermissionFieldDTO> data) throws IOException {
        List<DataPermissionFieldDTO> dataPermissionFieldDTOList;
        if(CollectionUtils.isEmpty(data)){
           dataPermissionFieldDTOList = dataPermissionFieldService.queryAll(new DataPermissionFieldQueryCriteria());
        }else{
           dataPermissionFieldDTOList = dataPermissionFieldService.findByIdlist(data);
        }
        dataPermissionFieldService.download(dataPermissionFieldDTOList, response);
    }

    @Log("导入数据")
    @ApiOperation("导入数据")
    @PostMapping(value = "/upload")
    @PreAuthorize("@el.check('dataPermissionField:import')")
    public  ResponseEntity upload(HttpServletResponse response,@RequestParam("file") MultipartFile file)throws Exception{
       return new ResponseEntity<>(dataPermissionFieldService.upload(file),HttpStatus.CREATED);
    }

    @GetMapping
    @Log("查询DataPermissionField")
    @ApiOperation("查询DataPermissionField")
    @PreAuthorize("@el.check('dataPermissionField:list')")
    public ResponseEntity getDataPermissionFields(DataPermissionFieldQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(dataPermissionFieldService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    @Log("查询DataPermissionField")
    @ApiOperation("查询DataPermissionField")
    @PreAuthorize("@el.check('dataPermissionField:list')")
    public ResponseEntity getDataPermissionFieldsById(@PathVariable String id){
        return new ResponseEntity<>(dataPermissionFieldService.findByPermissionId(id),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增DataPermissionField")
    @ApiOperation("新增DataPermissionField")
    @PreAuthorize("@el.check('dataPermissionField:add')")
    public ResponseEntity create(@Validated @RequestBody DataPermissionField resources){
        return new ResponseEntity<>(dataPermissionFieldService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改DataPermissionField")
    @ApiOperation("修改DataPermissionField")
    @PreAuthorize("@el.check('dataPermissionField:edit')")
    public ResponseEntity update(@Validated @RequestBody DataPermissionField resources){
        dataPermissionFieldService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除DataPermissionField")
    @ApiOperation("删除DataPermissionField")
    @PreAuthorize("@el.check('dataPermissionField:del')")
    public ResponseEntity delete(@PathVariable String id){
        dataPermissionFieldService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
