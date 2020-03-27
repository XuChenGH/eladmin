package hundsun.pdpm.modules.deliverconfig.rest;

import hundsun.pdpm.aop.log.Log;
import java.util.List;
import hundsun.pdpm.modules.deliverconfig.domain.FunIdAccount;
import hundsun.pdpm.modules.deliverconfig.service.FunIdAccountService;
import hundsun.pdpm.modules.deliverconfig.service.dto.FunIdAccountDTO;
import hundsun.pdpm.modules.deliverconfig.service.dto.FunIdAccountQueryCriteria;
import hundsun.pdpm.modules.execl.ExcelUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.servlet.http.HttpServletResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
/**
* @author yantt
* @date 2020-02-23
*/
@Api(tags = "FunIdAccount管理")
@RestController
@RequestMapping("/api/funIdAccount")
public class FunIdAccountController {

    private final FunIdAccountService funIdAccountService;

    public FunIdAccountController(FunIdAccountService funIdAccountService) {
        this.funIdAccountService = funIdAccountService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @PostMapping(value = "/download")
    @PreAuthorize("@el.check('funIdAccount:export')")
    public void download(HttpServletResponse response,@RequestBody List<FunIdAccountDTO> data) throws IOException {
        List<FunIdAccountDTO> funIdAccountDTOList;
        if(CollectionUtils.isEmpty(data)){
           funIdAccountDTOList = funIdAccountService.queryAll(new FunIdAccountQueryCriteria());
        }else{
           funIdAccountDTOList = funIdAccountService.findByIdlist(data);
        }
        funIdAccountService.download(funIdAccountDTOList, response);
    }

    @Log("导入数据")
    @ApiOperation("导入数据")
    @PostMapping(value = "/upload")
    @PreAuthorize("@el.check('delivery:import')")
    public  ResponseEntity upload(HttpServletResponse response,@RequestParam("file") MultipartFile file,@RequestParam("id")String id)throws Exception{
        if(CollectionUtils.isEmpty(ExcelUtils.getExeclMap(id))){
            ExcelUtils.updateExeclStatus(ExcelUtils.START_IMP,id);
            ExcelUtils.saveFile(id,file);
            ExecutorService executor = Executors.newFixedThreadPool(1);
            CompletableFuture.runAsync(() ->{
                try {
                    funIdAccountService.upload(file,id);
                    ExcelUtils.updateExeclStatus(ExcelUtils.FINISH_IMP,id);
                }catch (Exception e){
                    e.printStackTrace();
                    ExcelUtils.updateExeclStatus(ExcelUtils.EXECPTION_IMP,id);
                }
            },executor);
        }
        Thread.sleep(1000);
        return new ResponseEntity<>(ExcelUtils.getExeclMap(id),HttpStatus.CREATED);
    }

    @GetMapping
    @Log("查询功能编号清单")
    @ApiOperation("查询功能编号清单")
    @PreAuthorize("@el.check('delivery:list')")
    public ResponseEntity getFunIdAccounts(FunIdAccountQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(funIdAccountService.queryAll(criteria,pageable),HttpStatus.OK);
    }
    @GetMapping(value = "/getfuncno")
    @Log("查询功能编号配置")
    @ApiOperation("查询功能编号配置")
    @PreAuthorize("@el.check('delivery:list')")
    public ResponseEntity getFunctionNoList(FunIdAccountQueryCriteria criteria){
        return new ResponseEntity<>(funIdAccountService.getFunctionNoList(criteria),HttpStatus.OK);
    }
    @PostMapping
    @Log("新增功能编号清单")
    @ApiOperation("新增功能编号清单")
    @PreAuthorize("@el.check('delivery:add')")
    public ResponseEntity create(@Validated @RequestBody FunIdAccount resources){
        return new ResponseEntity<>(funIdAccountService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改功能编号清单")
    @ApiOperation("修改功能编号清单")
    @PreAuthorize("@el.check('delivery:edit')")
    public ResponseEntity update(@Validated @RequestBody FunIdAccount resources){
        funIdAccountService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除功能编号清单")
    @ApiOperation("删除功能编号清单")
    @PreAuthorize("@el.check('delivery:del')")
    public ResponseEntity delete(@PathVariable String id){
        funIdAccountService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
