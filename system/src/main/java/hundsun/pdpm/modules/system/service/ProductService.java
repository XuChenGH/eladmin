package hundsun.pdpm.modules.system.service;

import hundsun.pdpm.modules.system.domain.Product;
import hundsun.pdpm.modules.system.service.dto.ProductDTO;
import hundsun.pdpm.modules.system.service.dto.ProductQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;
/**
* @author yantt
* @date 2019-12-05
*/
public interface ProductService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(ProductQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<ProductDTO>
    */
    List<ProductDTO> queryAll(ProductQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return ProductDTO
     */
    ProductDTO findById(String id);

    List<ProductDTO> findByIdlist(List<ProductDTO> productList);

    ProductDTO create(Product resources);

    void update(Product resources);

    void delete(String id);

    void download(List<ProductDTO> all, HttpServletResponse response) throws IOException;

    List<ProductDTO> upload(MultipartFile multipartFiles) throws Exception;
}
