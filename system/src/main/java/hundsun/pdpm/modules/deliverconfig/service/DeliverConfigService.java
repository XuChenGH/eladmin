package hundsun.pdpm.modules.deliverconfig.service;

import hundsun.pdpm.modules.deliverconfig.domain.DeliverConfig;
import hundsun.pdpm.modules.deliverconfig.service.dto.DeliverConfigDTO;
import hundsun.pdpm.modules.deliverconfig.service.dto.DeliverConfigQueryCriteria;
import hundsun.pdpm.modules.deliverconfig.service.dto.FunctionNoDTO;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;
/**
* @author yantt
* @date 2020-02-22
*/
public interface DeliverConfigService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(DeliverConfigQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<DeliverConfigDTO>
    */
    List<DeliverConfigDTO> queryAll(DeliverConfigQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return DeliverConfigDTO
     */
    DeliverConfigDTO findById(Integer id);

    List<DeliverConfigDTO> findByIdlist(List<DeliverConfigDTO> deliverConfigList);

    DeliverConfigDTO create(DeliverConfig resources);

    void update(DeliverConfig resources);

    void delete(Integer id);

    Map<String,FunctionNoDTO> getConfigData(String productId);


}
