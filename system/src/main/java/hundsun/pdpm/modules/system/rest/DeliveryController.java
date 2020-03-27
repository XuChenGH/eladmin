package hundsun.pdpm.modules.system.rest;

import hundsun.pdpm.aop.log.Log;
import java.util.List;

import hundsun.pdpm.modules.execl.ExcelUtils;
import hundsun.pdpm.modules.system.domain.Delivery;
import hundsun.pdpm.modules.system.service.DeliveryService;
import hundsun.pdpm.modules.system.service.dto.DeliveryDTO;
import hundsun.pdpm.modules.system.service.dto.DeliveryQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import java.util.concurrent.*;
import javax.servlet.http.HttpServletResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
/**
* @author yantt
* @date 2019-12-25
*/
@Api(tags = "Delivery管理")
@RestController
@RequestMapping("/api/delivery")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @PostMapping(value = "/download")
    @PreAuthorize("@el.check('delivery:export')")
    public void download(HttpServletResponse response,@RequestBody List<DeliveryDTO> data) throws IOException {
        List<DeliveryDTO> deliveryDTOList;
        if(CollectionUtils.isEmpty(data)){
           deliveryDTOList = deliveryService.queryAll(new DeliveryQueryCriteria());
        }else{
           deliveryDTOList = deliveryService.findByIdlist(data);
        }
        deliveryService.download(deliveryDTOList, response);
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
                    deliveryService.upload(file,id);
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
    @Log("查询交付信息")
    @ApiOperation("查询交付信息")
    @PreAuthorize("@el.check('delivery:list')")
    public ResponseEntity getDeliverys(DeliveryQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(deliveryService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增交付信息")
    @ApiOperation("新增交付信息")
    @PreAuthorize("@el.check('delivery:add')")
    public ResponseEntity create(@Validated @RequestBody List<Delivery> resources){
           deliveryService.create(resources);
           return new ResponseEntity<>(new DeliveryDTO(),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改交付信息")
    @ApiOperation("修改交付信息")
    @PreAuthorize("@el.check('delivery:edit')")
    public ResponseEntity update(@Validated @RequestBody Delivery resources){
        deliveryService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/batchupdate")
    @Log("批量修改交付信息")
    @ApiOperation("修改交付信息")
    @PreAuthorize("@el.check('delivery:edit')")
    public ResponseEntity batchupdate(@Validated @RequestBody List<Delivery> resources){
        if(!CollectionUtils.isEmpty(resources)){
            deliveryService.batchUpdate(resources);
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    @PostMapping(value = "/delete")
    @Log("删除交付信息")
    @ApiOperation("删除交付信息")
    @PreAuthorize("@el.check('delivery:del')")
    public ResponseEntity delete(@RequestBody List<String> id){
        deliveryService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
