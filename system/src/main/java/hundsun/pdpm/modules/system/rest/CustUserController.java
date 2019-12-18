package hundsun.pdpm.modules.system.rest;


import hundsun.pdpm.aop.log.Log;
import hundsun.pdpm.modules.system.domain.CustUser;
import hundsun.pdpm.modules.system.service.CustUserService;
import hundsun.pdpm.modules.system.service.dto.CustUserQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author yantt
* @date 2019-11-30
*/
@Api(tags = "CustUser管理")
@RestController
@RequestMapping("/api/custUser")
public class CustUserController {

    private final CustUserService custUserService;

    public CustUserController(CustUserService custUserService) {
        this.custUserService = custUserService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('custUser:list')")
    public void download(HttpServletResponse response, CustUserQueryCriteria criteria) throws IOException {
        custUserService.download(custUserService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询客户用户信息")
    @ApiOperation("查询客户用户信息")
    @PreAuthorize("@el.check('custUser:list')")
    public ResponseEntity getCustUsers(CustUserQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(custUserService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增客户用户信息")
    @ApiOperation("新增客户用户信息")
    @PreAuthorize("@el.check('custUser:add')")
    public ResponseEntity create(@Validated @RequestBody CustUser resources){
        return new ResponseEntity<>(custUserService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改客户用户信息")
    @ApiOperation("修改客户用户信息")
    @PreAuthorize("@el.check('custUser:edit')")
    public ResponseEntity update(@Validated @RequestBody CustUser resources){
        custUserService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除客户用户信息")
    @ApiOperation("删除客户用户信息")
    @PreAuthorize("@el.check('custUser:del')")
    public ResponseEntity delete(@PathVariable Long id){
        custUserService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
