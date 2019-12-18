package hundsun.pdpm.modules.system.service;

import hundsun.pdpm.modules.system.domain.BusinessInfo;
import hundsun.pdpm.modules.system.service.dto.BusinessInfoDTO;
import hundsun.pdpm.modules.system.service.dto.BusinessInfoQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;
/**
* @author yantt
* @date 2019-12-09
*/
public interface BusinessInfoService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(BusinessInfoQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<BusinessInfoDTO>
    */
    List<BusinessInfoDTO> queryAll(BusinessInfoQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return BusinessInfoDTO
     */
    BusinessInfoDTO findById(String id);

    List<BusinessInfoDTO> findByIdlist(List<BusinessInfoDTO> businessInfoList);

    BusinessInfoDTO create(BusinessInfo resources);

    void update(BusinessInfo resources);

    void delete(String id);

    void download(List<BusinessInfoDTO> all, HttpServletResponse response) throws IOException;

    List<BusinessInfoDTO> upload(MultipartFile multipartFiles) throws Exception;
}
