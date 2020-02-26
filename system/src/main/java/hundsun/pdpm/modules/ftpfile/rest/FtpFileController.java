package hundsun.pdpm.modules.ftpfile.rest;

import hundsun.pdpm.aop.log.Log;
import java.util.List;
import hundsun.pdpm.modules.ftpfile.domain.FtpFile;
import hundsun.pdpm.modules.ftpfile.domain.FtpUser;
import hundsun.pdpm.modules.ftpfile.service.FtpFileService;
import hundsun.pdpm.modules.ftpfile.service.dto.FtpFileDTO;
import hundsun.pdpm.modules.ftpfile.service.dto.FtpFileQueryCriteria;
import hundsun.pdpm.modules.ftpfile.service.dto.RefreshDTO;
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
@Api(tags = "FtpFile管理")
@RestController
@RequestMapping("/api/ftpFile")
public class FtpFileController {

    private final FtpFileService ftpFileService;

    public FtpFileController(FtpFileService ftpFileService) {
        this.ftpFileService = ftpFileService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @PostMapping(value = "/download")
    @PreAuthorize("@el.check('ftpFile:export')")
    public void download(HttpServletResponse response,@RequestBody List<FtpFileDTO> data) throws IOException {
        List<FtpFileDTO> ftpFileDTOList;
        if(CollectionUtils.isEmpty(data)){
           ftpFileDTOList = ftpFileService.queryAll(new FtpFileQueryCriteria());
        }else{
           ftpFileDTOList = ftpFileService.findByIdlist(data);
        }
        ftpFileService.download(ftpFileDTOList, response);
    }

    @Log("导入数据")
    @ApiOperation("导入数据")
    @PostMapping(value = "/upload")
    @PreAuthorize("@el.check('ftpFile:import')")
    public  ResponseEntity upload(HttpServletResponse response,@RequestParam("file") MultipartFile file)throws Exception{
       return new ResponseEntity<>(ftpFileService.upload(file),HttpStatus.CREATED);
    }

    @GetMapping
    @Log("查询ftp文件")
    @ApiOperation("查询ftp文件")
    @PreAuthorize("@el.check('ftpFile:list')")
    public ResponseEntity getFtpFiles(FtpFileQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(ftpFileService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增ftp文件")
    @ApiOperation("新增ftp文件")
    @PreAuthorize("@el.check('ftpFile:add')")
    public ResponseEntity create(@Validated @RequestBody FtpFile resources){
        return new ResponseEntity<>(ftpFileService.create(resources),HttpStatus.CREATED);
    }
    @PostMapping(value = "/refresh")
    @Log("刷新ftp文件")
    @ApiOperation("刷新ftp文件")
    @PreAuthorize("@el.check('ftpFile:list')")
    public void refresh(@Validated @RequestBody RefreshDTO refreshDTO) throws Exception{
        ftpFileService.refresh(refreshDTO.getUser(),refreshDTO.getPathName(),refreshDTO.getParentId());
    }
    @PutMapping
    @Log("修改ftp文件")
    @ApiOperation("修改ftp文件")
    @PreAuthorize("@el.check('ftpFile:edit')")
    public ResponseEntity update(@Validated @RequestBody FtpFile resources){
        ftpFileService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除ftp文件")
    @ApiOperation("删除ftp文件")
    @PreAuthorize("@el.check('ftpFile:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        ftpFileService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
