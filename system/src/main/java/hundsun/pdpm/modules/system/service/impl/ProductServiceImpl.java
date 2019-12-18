package hundsun.pdpm.modules.system.service.impl;

import hundsun.pdpm.modules.datapermission.utils.PermissionUtils;
import hundsun.pdpm.modules.system.domain.Product;
import hundsun.pdpm.modules.system.service.DictDetailService;
import org.springframework.util.CollectionUtils;
import hundsun.pdpm.utils.ValidationUtil;
import hundsun.pdpm.utils.FileUtil;
import hundsun.pdpm.modules.system.repository.ProductRepository;
import hundsun.pdpm.modules.system.service.ProductService;
import hundsun.pdpm.modules.system.service.dto.ProductDTO;
import hundsun.pdpm.modules.system.service.dto.ProductQueryCriteria;
import hundsun.pdpm.modules.system.service.mapper.ProductMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import cn.hutool.core.util.IdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import hundsun.pdpm.modules.execl.ExcelHelper;
import hundsun.pdpm.modules.system.domain.DictDetail;
import org.springframework.web.multipart.MultipartFile;
import hundsun.pdpm.utils.PageUtil;
import hundsun.pdpm.utils.QueryHelp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;
import hundsun.pdpm.utils.*;
/**
* @author yantt
* @date 2019-12-05
*/
@Service
@CacheConfig(cacheNames = "product")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    @Autowired
    private DictDetailService dictDetailService;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(ProductQueryCriteria criteria, Pageable pageable){
        Page<Product> page = productRepository.findAll((root, criteriaQuery, criteriaBuilder) ->
                PermissionUtils.getPredicate(root,
                        QueryHelp.getPredicate(root,criteria,criteriaBuilder),
                        criteriaBuilder,ProductDTO.class),pageable);
        return PageUtil.toPage(page.map(productMapper::toDto));
    }

    @Override
    @Cacheable
    public List<ProductDTO> queryAll(ProductQueryCriteria criteria){
        return productMapper.toDto(productRepository.findAll((root, criteriaQuery, criteriaBuilder) ->
                PermissionUtils.getPredicate(root,
                        QueryHelp.getPredicate(root,criteria,criteriaBuilder),
                        criteriaBuilder,ProductDTO.class)));
    }

    @Override
    @Cacheable(key = "#p0")
    public ProductDTO findById(String id) {
        Product product = productRepository.findById(id).orElseGet(Product::new);
        ValidationUtil.isNull(product.getId(),"Product","id",id);
        return productMapper.toDto(product);
    }
    @Override
    @Cacheable
    public List<ProductDTO> findByIdlist(List<ProductDTO> productList) {
        if (CollectionUtils.isEmpty(productList)){
        return  new ArrayList<>();
        }
        List<String> idlist = productList.stream().map(ProductDTO::getId).collect(Collectors.toList());
        return productMapper.toDto(productRepository.findAllByIdIn(idlist));
    }
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public ProductDTO create(Product resources) {
        resources.setId(StringUtils.get32UUID());
        return productMapper.toDto(productRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(Product resources) {
        Product product = productRepository.findById(resources.getId()).orElseGet(Product::new);
        ValidationUtil.isNull( product.getId(),"Product","id",resources.getId());
        product.copy(resources);
        productRepository.save(product);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        productRepository.deleteById(id);
    }


    @Override
    public void download(List<ProductDTO> all, HttpServletResponse response) throws IOException {
        Map<String, List<DictDetail>> dictMap = dictDetailService.queryAll(ProductDTO.class);
        ExcelHelper.exportExcel(all,dictMap,ProductDTO.class,false);
    }
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public List<ProductDTO> upload(MultipartFile multipartFiles) throws Exception {
       Map<String, List<DictDetail>> dictMap = dictDetailService.queryAll(ProductDTO.class);
       List<ProductDTO> data = ExcelHelper.importExcel(multipartFiles,ProductDTO.class,dictMap,false);
       if(!CollectionUtils.isEmpty(data)){
          List<Product> savelist =  new ArrayList<>();
          List<String> idlist = new ArrayList<>();
          for(ProductDTO productDTO:data){
             if(!StringUtils.isEmpty(productDTO.getId())){
                    //删除库中
                 idlist.add(productDTO.getId());
             }else {
                 productDTO.setId(StringUtils.get32UUID());
             }
             savelist.add(productMapper.toEntity(productDTO));
          }
       productRepository.deleteAllByIdIn(idlist);
       productRepository.saveAll(savelist);
       }
        return  data;
     }
}
