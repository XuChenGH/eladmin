package hundsun.pdpm.modules.ftpfile.repository;

import hundsun.pdpm.modules.ftpfile.domain.FtpUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
* @author yantt
* @date 2019-12-31
*/
public interface FtpUserRepository extends JpaRepository<FtpUser, Integer>, JpaSpecificationExecutor<FtpUser> {
List<FtpUser> findAllByIdIn(List<Integer> idlist);

void  deleteAllByIdIn(List<String> idlist);
}
