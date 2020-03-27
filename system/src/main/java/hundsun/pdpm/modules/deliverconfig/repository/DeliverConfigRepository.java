package hundsun.pdpm.modules.deliverconfig.repository;

import hundsun.pdpm.modules.deliverconfig.domain.DeliverConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;
/**
* @author yantt
* @date 2020-02-22
*/
public interface DeliverConfigRepository extends JpaRepository<DeliverConfig, Integer>, JpaSpecificationExecutor<DeliverConfig> {
List<DeliverConfig> findAllByIdIn(List<Integer> idlist);

void  deleteAllByIdIn(List<String> idlist);
}
