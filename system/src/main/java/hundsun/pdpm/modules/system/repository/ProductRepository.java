package hundsun.pdpm.modules.system.repository;

import hundsun.pdpm.modules.system.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;
/**
* @author yantt
* @date 2019-12-05
*/
public interface ProductRepository extends JpaRepository<Product, String>, JpaSpecificationExecutor<Product> {
List<Product> findAllByIdIn(List<String> idlist);

void  deleteAllByIdIn(List<String> idlist);
}
