package hundsun.pdpm.modules.system.domain;

import hundsun.pdpm.modules.system.service.dto.BusinessInfoDTO;
import hundsun.pdpm.modules.system.service.dto.BusinessInfoQueryCriteria;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author ：yantt21019
 * @date ：Created in 2020/01/15 17:28
 * @description：
 * @version:
 */
@Data
public class BusinessInfoDL implements Serializable {
    BusinessInfoQueryCriteria criteria;

    List<BusinessInfoDTO> data;

    String id;
}
