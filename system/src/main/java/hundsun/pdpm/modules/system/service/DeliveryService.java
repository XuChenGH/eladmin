package hundsun.pdpm.modules.system.service;

import hundsun.pdpm.modules.system.domain.Delivery;
import hundsun.pdpm.modules.system.service.dto.DeliveryDTO;
import hundsun.pdpm.modules.system.service.dto.DeliveryQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;
/**
* @author yantt
* @date 2019-12-25
*/
public interface DeliveryService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(DeliveryQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<DeliveryDTO>
    */
    List<DeliveryDTO> queryAll(DeliveryQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return DeliveryDTO
     */
    DeliveryDTO findById(String id);

    List<DeliveryDTO> findByIdlist(List<DeliveryDTO> deliveryList);

    void create(List<Delivery> resources);

    void update(Delivery resources);

    void batchUpdate(List<Delivery> resources);

    void delete(String id);

    void delete(List<String> id);

    void download(List<DeliveryDTO> all, HttpServletResponse response) throws IOException;

    void upload(MultipartFile multipartFiles,String id) throws Exception;


}
