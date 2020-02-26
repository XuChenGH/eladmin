package hundsun.pdpm.modules.system.repository;

import hundsun.pdpm.modules.system.domain.Delivery;
import hundsun.pdpm.modules.system.service.dto.DeliveryQueryCriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
/**
* @author yantt
* @date 2019-12-25
*/
public interface DeliveryRepository extends JpaRepository<Delivery, String>, JpaSpecificationExecutor<Delivery> {
List<Delivery> findAllByIdIn(List<String> idlist);

void  deleteAllByIdIn(List<String> idlist);

@Query(value = "SELECT NEXTVAL('deliveryTaskId')",nativeQuery = true )
String getDeliveryTaskId();
}
