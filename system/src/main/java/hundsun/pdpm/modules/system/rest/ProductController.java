package hundsun.pdpm.modules.system.rest;

import hundsun.pdpm.aop.log.Log;
import java.util.List;
import hundsun.pdpm.modules.system.domain.Product;
import hundsun.pdpm.modules.system.service.ProductService;
import hundsun.pdpm.modules.system.service.dto.ProductDTO;
import hundsun.pdpm.modules.system.service.dto.ProductQueryCriteria;
import hundsun.pdpm.modules.system.service.dto.UserQueryCriteria;
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
* @date 2019-12-05
*/
@Api(tags = "Product管理")
@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @PostMapping(value = "/download")
    @PreAuthorize("@el.check('product:export')")
    public void download(HttpServletResponse response,@RequestBody List<ProductDTO> data) throws IOException {
        List<ProductDTO> productDTOList;
        if(CollectionUtils.isEmpty(data)){
           productDTOList = productService.queryAll(new ProductQueryCriteria());
        }else{
           productDTOList = productService.findByIdlist(data);
        }
        productService.download(productDTOList, response);
    }

    @Log("导入数据")
    @ApiOperation("导入数据")
    @PostMapping(value = "/upload")
    @PreAuthorize("@el.check('product:import')")
    public  ResponseEntity upload(HttpServletResponse response,@RequestParam("file") MultipartFile file)throws Exception{
       return new ResponseEntity<>(productService.upload(file),HttpStatus.CREATED);
    }
    @ApiOperation("返回全部的用户")
    @GetMapping(value = "/all")
    @PreAuthorize("@el.check('custUser:list','custProduct:add')")
    public ResponseEntity getAll(ProductQueryCriteria criteria){
        return new ResponseEntity<>(productService.queryAll(criteria),HttpStatus.OK);
    }
    @GetMapping
    @Log("查询产品信息")
    @ApiOperation("查询产品信息")
    @PreAuthorize("@el.check('product:list')")
    public ResponseEntity getProducts(ProductQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(productService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增产品信息")
    @ApiOperation("新增产品信息")
    @PreAuthorize("@el.check('product:add')")
    public ResponseEntity create(@Validated @RequestBody Product resources){
        return new ResponseEntity<>(productService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改产品信息")
    @ApiOperation("修改产品信息")
    @PreAuthorize("@el.check('product:edit')")
    public ResponseEntity update(@Validated @RequestBody Product resources){
        productService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除产品信息")
    @ApiOperation("删除产品信息")
    @PreAuthorize("@el.check('product:del')")
    public ResponseEntity delete(@PathVariable String id){
        productService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
