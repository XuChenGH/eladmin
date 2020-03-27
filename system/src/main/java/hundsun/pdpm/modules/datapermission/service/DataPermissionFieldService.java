package hundsun.pdpm.modules.datapermission.service;

import hundsun.pdpm.modules.datapermission.domain.DataPermissionField;
import hundsun.pdpm.modules.datapermission.service.dto.DataPermissionFieldDTO;
import hundsun.pdpm.modules.datapermission.service.dto.DataPermissionFieldQueryCriteria;
import hundsun.pdpm.modules.system.service.dto.RoleDTO;
import hundsun.pdpm.modules.system.service.dto.RoleSmallDTO;
import hundsun.pdpm.modules.system.service.dto.UserDTO;
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
public interface DataPermissionFieldService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(DataPermissionFieldQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<DataPermissionFieldDTO>
    */
    List<DataPermissionFieldDTO> queryAll(DataPermissionFieldQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return DataPermissionFieldDTO
     */
    DataPermissionFieldDTO findById(String id);

    List<DataPermissionFieldDTO> findByPermissionId(String permissionId);

    List<DataPermissionFieldDTO> findByIdlist(List<DataPermissionFieldDTO> dataPermissionFieldList);

    List<DataPermissionFieldDTO> findByUserRoleAndTableCode(List<RoleSmallDTO> roles, List<UserDTO> user,String tableCode);

    DataPermissionFieldDTO create(DataPermissionField resources);

    void update(DataPermissionField resources);

    void delete(String id);

    void deleteByTableId(String tableId);

    void download(List<DataPermissionFieldDTO> all, HttpServletResponse response) throws IOException;

    List<DataPermissionFieldDTO> upload(MultipartFile multipartFiles) throws Exception;
}
