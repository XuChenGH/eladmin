package hundsun.pdpm.modules.system.rest;

import hundsun.pdpm.aop.log.Log;
import hundsun.pdpm.exception.BadRequestException;
import hundsun.pdpm.modules.system.domain.Dict;
import hundsun.pdpm.modules.system.service.DictDetailService;
import hundsun.pdpm.modules.system.service.DictService;
import hundsun.pdpm.modules.system.service.dto.DictDTO;
import hundsun.pdpm.modules.system.service.dto.DictDetailQueryCriteria;
import hundsun.pdpm.modules.system.service.dto.DictQueryCriteria;
import hundsun.pdpm.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import hundsun.pdpm.modules.system.domain.DictDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
* @author Zheng Jie
* @date 2019-04-10
*/
@RestController
@Api(tags = "系统：字典详情管理")
@RequestMapping("/api/dictDetail")
public class DictDetailController {

    private final DictDetailService dictDetailService;

    @Autowired
    private DictService dictService;

    private static final String ENTITY_NAME = "dictDetail";

    public DictDetailController(DictDetailService dictDetailService) {
        this.dictDetailService = dictDetailService;
    }

    @Log("查询字典详情")
    @ApiOperation("查询字典详情")
    @GetMapping
    public ResponseEntity getDictDetails(DictDetailQueryCriteria criteria,
                                         @PageableDefault(sort = {"sort"}, direction = Sort.Direction.ASC) Pageable pageable){
        if(StringUtils.equals("ALL",criteria.getDictName())){
            Map<String,Object> map =new HashMap<>();
            List<DictDTO> dicts = dictService.queryAll(new DictQueryCriteria());
            List<String>  names = dicts.stream().map(DictDTO::getName).collect(Collectors.toList());
            for (String name : names) {
                criteria.setDictName(name);
                map.put(name,dictDetailService.queryAll(criteria,pageable).get("content"));
            }
            return new ResponseEntity<>(map,HttpStatus.OK);
        }else {
            return new ResponseEntity<>(dictDetailService.queryAll(criteria,pageable),HttpStatus.OK);
        }
    }

    @Log("查询多个字典详情")
    @ApiOperation("查询多个字典详情")
    @GetMapping(value = "/map")
    public ResponseEntity getDictDetailMaps(DictDetailQueryCriteria criteria,
                                         @PageableDefault(sort = {"sort"}, direction = Sort.Direction.ASC) Pageable pageable){
        Map<String,Object> map =new HashMap<>();
        List<String> names;
        if(StringUtils.equals("ALL",criteria.getDictName())){
           List<DictDTO> dicts = dictService.queryAll(new DictQueryCriteria());
           names = dicts.stream().map(DictDTO::getName).collect(Collectors.toList());
        }else {
            String[] name = criteria.getDictName().split(",");
            names= Arrays.asList(name);
        }
        for (String name : names) {
            criteria.setDictName(name);
            map.put(name,dictDetailService.queryAll(criteria,pageable).get("content"));
        }
        return new ResponseEntity<>(map,HttpStatus.OK);
    }

    @Log("新增字典详情")
    @ApiOperation("新增字典详情")
    @PostMapping
    @PreAuthorize("@el.check('dict:add')")
    public ResponseEntity create(@Validated @RequestBody DictDetail resources){
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        return new ResponseEntity<>(dictDetailService.create(resources),HttpStatus.CREATED);
    }

    @Log("修改字典详情")
    @ApiOperation("修改字典详情")
    @PutMapping
    @PreAuthorize("@el.check('dict:edit')")
    public ResponseEntity update(@Validated(DictDetail.Update.class) @RequestBody DictDetail resources){
        dictDetailService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除字典详情")
    @ApiOperation("删除字典详情")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('dict:del')")
    public ResponseEntity delete(@PathVariable Long id){
        dictDetailService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
