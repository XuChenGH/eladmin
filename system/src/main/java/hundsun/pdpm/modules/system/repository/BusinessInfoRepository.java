package hundsun.pdpm.modules.system.repository;

import hundsun.pdpm.modules.system.domain.BusinessInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;
/**
* @author yantt
* @date 2019-12-09
*/
public interface BusinessInfoRepository extends JpaRepository<BusinessInfo, String>, JpaSpecificationExecutor<BusinessInfo> {
List<BusinessInfo> findAllByIdIn(List<String> idlist);

void  deleteAllByIdIn(List<String> idlist);
}
