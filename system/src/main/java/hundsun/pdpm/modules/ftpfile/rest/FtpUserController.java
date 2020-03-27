package hundsun.pdpm.modules.ftpfile.rest;

import hundsun.pdpm.aop.log.Log;
import java.util.List;
import hundsun.pdpm.modules.ftpfile.domain.FtpUser;
import hundsun.pdpm.modules.ftpfile.service.FtpUserService;
import hundsun.pdpm.modules.ftpfile.service.dto.FtpUserDTO;
import hundsun.pdpm.modules.ftpfile.service.dto.FtpUserQueryCriteria;
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
* @date 2019-12-31
*/
@Api(tags = "FtpUser管理")
@RestController
@RequestMapping("/api/ftpUser")
public class FtpUserController {

    private final FtpUserService ftpUserService;

    public FtpUserController(FtpUserService ftpUserService) {
        this.ftpUserService = ftpUserService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @PostMapping(value = "/download")
    @PreAuthorize("@el.check('ftpUser:export')")
    public void download(HttpServletResponse response,@RequestBody List<FtpUserDTO> data) throws IOException {
        List<FtpUserDTO> ftpUserDTOList;
        if(CollectionUtils.isEmpty(data)){
           ftpUserDTOList = ftpUserService.queryAll(new FtpUserQueryCriteria());
        }else{
           ftpUserDTOList = ftpUserService.findByIdlist(data);
        }
        ftpUserService.download(ftpUserDTOList, response);
    }

    @Log("导入数据")
    @ApiOperation("导入数据")
    @PostMapping(value = "/upload")
    @PreAuthorize("@el.check('ftpUser:import')")
    public  ResponseEntity upload(HttpServletResponse response,@RequestParam("file") MultipartFile file)throws Exception{
       return new ResponseEntity<>(ftpUserService.upload(file),HttpStatus.CREATED);
    }

    @GetMapping
    @Log("查询ftp配置")
    @ApiOperation("查询ftp配置")
    @PreAuthorize("@el.check('ftpUser:list')")
    public ResponseEntity getFtpUsers(FtpUserQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(ftpUserService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增ftp配置")
    @ApiOperation("新增ftp配置")
    @PreAuthorize("@el.check('ftpUser:add')")
    public ResponseEntity create(@Validated @RequestBody FtpUser resources){
        return new ResponseEntity<>(ftpUserService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改ftp配置")
    @ApiOperation("修改ftp配置")
    @PreAuthorize("@el.check('ftpUser:edit')")
    public ResponseEntity update(@Validated @RequestBody FtpUser resources){
        ftpUserService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除ftp配置")
    @ApiOperation("删除ftp配置")
    @PreAuthorize("@el.check('ftpUser:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        ftpUserService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
