package hundsun.pdpm.modules.system.rest;

import hundsun.pdpm.aop.log.Log;
import java.util.List;
import hundsun.pdpm.modules.system.domain.BusinessInfo;
import hundsun.pdpm.modules.system.service.BusinessInfoService;
import hundsun.pdpm.modules.system.service.dto.BusinessInfoDTO;
import hundsun.pdpm.modules.system.service.dto.BusinessInfoQueryCriteria;
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
* @date 2019-12-09
*/
@Api(tags = "BusinessInfo管理")
@RestController
@RequestMapping("/api/businessInfo")
public class BusinessInfoController {

    private final BusinessInfoService businessInfoService;

    public BusinessInfoController(BusinessInfoService businessInfoService) {
        this.businessInfoService = businessInfoService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @PostMapping(value = "/download")
    @PreAuthorize("@el.check('businessInfo:export')")
    public void download(HttpServletResponse response,@RequestBody List<BusinessInfoDTO> data) throws IOException {
        List<BusinessInfoDTO> businessInfoDTOList;
        if(CollectionUtils.isEmpty(data)){
           businessInfoDTOList = businessInfoService.queryAll(new BusinessInfoQueryCriteria());
        }else{
           businessInfoDTOList = businessInfoService.findByIdlist(data);
        }
        businessInfoService.download(businessInfoDTOList, response);
    }

    @Log("导入数据")
    @ApiOperation("导入数据")
    @PostMapping(value = "/upload")
    @PreAuthorize("@el.check('businessInfo:import')")
    public  ResponseEntity upload(HttpServletResponse response,@RequestParam("file") MultipartFile file)throws Exception{
       return new ResponseEntity<>(businessInfoService.upload(file),HttpStatus.CREATED);
    }

    @GetMapping
    @Log("查询TA商机信息")
    @ApiOperation("查询TA商机信息")
    @PreAuthorize("@el.check('businessInfo:list')")
    public ResponseEntity getBusinessInfos(BusinessInfoQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(businessInfoService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增TA商机信息")
    @ApiOperation("新增TA商机信息")
    @PreAuthorize("@el.check('businessInfo:add')")
    public ResponseEntity create(@Validated @RequestBody BusinessInfo resources){
        return new ResponseEntity<>(businessInfoService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改TA商机信息")
    @ApiOperation("修改TA商机信息")
    @PreAuthorize("@el.check('businessInfo:edit')")
    public ResponseEntity update(@Validated @RequestBody BusinessInfo resources){
        businessInfoService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除TA商机信息")
    @ApiOperation("删除TA商机信息")
    @PreAuthorize("@el.check('businessInfo:del')")
    public ResponseEntity delete(@PathVariable String id){
        businessInfoService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
