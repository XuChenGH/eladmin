package hundsun.pdpm.modules.datapermission.rest;

import hundsun.pdpm.aop.log.Log;
import java.util.List;
import hundsun.pdpm.modules.datapermission.domain.DataPermission;
import hundsun.pdpm.modules.datapermission.service.DataPermissionService;
import hundsun.pdpm.modules.datapermission.service.dto.DataPermissionDTO;
import hundsun.pdpm.modules.datapermission.service.dto.DataPermissionQueryCriteria;
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
@Api(tags = "DataPermission管理")
@RestController
@RequestMapping("/api/dataPermission")
public class DataPermissionController {

    private final DataPermissionService dataPermissionService;

    public DataPermissionController(DataPermissionService dataPermissionService) {
        this.dataPermissionService = dataPermissionService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @PostMapping(value = "/download")
    @PreAuthorize("@el.check('dataPermission:export')")
    public void download(HttpServletResponse response,@RequestBody List<DataPermissionDTO> data) throws IOException {
        List<DataPermissionDTO> dataPermissionDTOList;
        if(CollectionUtils.isEmpty(data)){
           dataPermissionDTOList = dataPermissionService.queryAll(new DataPermissionQueryCriteria());
        }else{
           dataPermissionDTOList = dataPermissionService.findByIdlist(data);
        }
        dataPermissionService.download(dataPermissionDTOList, response);
    }

    @Log("导入数据")
    @ApiOperation("导入数据")
    @PostMapping(value = "/upload")
    @PreAuthorize("@el.check('dataPermission:import')")
    public  ResponseEntity upload(HttpServletResponse response,@RequestParam("file") MultipartFile file)throws Exception{
       return new ResponseEntity<>(dataPermissionService.upload(file),HttpStatus.CREATED);
    }

    @GetMapping
    @Log("查询DataPermission")
    @ApiOperation("查询DataPermission")
    @PreAuthorize("@el.check('dataPermission:list')")
    public ResponseEntity getDataPermissions(DataPermissionQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(dataPermissionService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @GetMapping(value = "/allset")
    @Log("查询DataPermission配置")
    @ApiOperation("查询DataPermission配置")
    @PreAuthorize("@el.check('dataPermission:list')")
    public ResponseEntity getAllDataPermissions(DataPermissionQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(dataPermissionService.scan("hundsun.pdpm.modules.system.service.dto"),HttpStatus.OK);
    }


    @PostMapping
    @Log("新增DataPermission")
    @ApiOperation("新增DataPermission")
    @PreAuthorize("@el.check('dataPermission:add')")
    public ResponseEntity create(@Validated @RequestBody DataPermission resources){
        return new ResponseEntity<>(dataPermissionService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改DataPermission")
    @ApiOperation("修改DataPermission")
    @PreAuthorize("@el.check('dataPermission:edit')")
    public ResponseEntity update(@Validated @RequestBody DataPermission resources){
        dataPermissionService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除DataPermission")
    @ApiOperation("删除DataPermission")
    @PreAuthorize("@el.check('dataPermission:del')")
    public ResponseEntity delete(@PathVariable String id){
        dataPermissionService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
