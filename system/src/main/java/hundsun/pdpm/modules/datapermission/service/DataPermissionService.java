package hundsun.pdpm.modules.datapermission.service;

import hundsun.pdpm.modules.datapermission.domain.DataPermission;
import hundsun.pdpm.modules.datapermission.service.dto.DataPermissionDTO;
import hundsun.pdpm.modules.datapermission.service.dto.DataPermissionFieldDTO;
import hundsun.pdpm.modules.datapermission.service.dto.DataPermissionQueryCriteria;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;
/**
* @author yantt
* @date 2019-12-13
*/
public interface DataPermissionService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(DataPermissionQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<DataPermissionDTO>
    */
    List<DataPermissionDTO> queryAll(DataPermissionQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return DataPermissionDTO
     */
    DataPermissionDTO findById(String id);

    List<DataPermissionDTO> findByIdlist(List<DataPermissionDTO> dataPermissionList);

    DataPermissionDTO create(DataPermission resources);

    void update(DataPermission resources);

    void delete(String id);

    void download(List<DataPermissionDTO> all, HttpServletResponse response) throws IOException;

    List<DataPermissionDTO> upload(MultipartFile multipartFiles) throws Exception;

    Map<String,Object> permission(Map<String,Object> data, Class clazz);

    List<DataPermissionFieldDTO> getFieldByRoleIdAndTableCode(Class clzz);

    List<DataPermissionDTO> scan(String packageName);
}
