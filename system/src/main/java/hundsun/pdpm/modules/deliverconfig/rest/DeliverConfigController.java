package hundsun.pdpm.modules.deliverconfig.rest;

import hundsun.pdpm.aop.log.Log;
import java.util.List;
import hundsun.pdpm.modules.deliverconfig.domain.DeliverConfig;
import hundsun.pdpm.modules.deliverconfig.service.DeliverConfigService;
import hundsun.pdpm.modules.deliverconfig.service.dto.DeliverConfigDTO;
import hundsun.pdpm.modules.deliverconfig.service.dto.DeliverConfigQueryCriteria;
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
* @date 2020-02-22
*/
@Api(tags = "DeliverConfig管理")
@RestController
@RequestMapping("/api/deliverConfig")
public class DeliverConfigController {

    private final DeliverConfigService deliverConfigService;

    public DeliverConfigController(DeliverConfigService deliverConfigService) {
        this.deliverConfigService = deliverConfigService;
    }

    @GetMapping
    @Log("查询交付物配置")
    @ApiOperation("查询交付物配置")
    @PreAuthorize("@el.check('deliverConfig:list')")
    public ResponseEntity getDeliverConfigs(DeliverConfigQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(deliverConfigService.queryAll(criteria,pageable),HttpStatus.OK);
    }


    @GetMapping(value = "/getfuncno")
    @Log("查询功能编号配置")
    @ApiOperation("查询功能编号配置")
    @PreAuthorize("@el.check('deliverConfig:list')")
    public ResponseEntity getDeliverConfigs(String productId){
        return new ResponseEntity<>(deliverConfigService.getConfigData(productId),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增交付物配置")
    @ApiOperation("新增交付物配置")
    @PreAuthorize("@el.check('deliverConfig:add')")
    public ResponseEntity create(@Validated @RequestBody DeliverConfig resources){
        return new ResponseEntity<>(deliverConfigService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改交付物配置")
    @ApiOperation("修改交付物配置")
    @PreAuthorize("@el.check('deliverConfig:edit')")
    public ResponseEntity update(@Validated @RequestBody DeliverConfig resources){
        deliverConfigService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除交付物配置")
    @ApiOperation("删除交付物配置")
    @PreAuthorize("@el.check('deliverConfig:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        deliverConfigService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
