package hundsun.pdpm.modules.ftpfile.repository;

import hundsun.pdpm.modules.ftpfile.domain.FtpFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;
/**
* @author yantt
* @date 2019-12-31
*/
public interface FtpFileRepository extends JpaRepository<FtpFile, Integer>, JpaSpecificationExecutor<FtpFile> {
List<FtpFile> findAllByIdIn(List<Integer> idlist);

void  deleteAllByIdIn(List<String> idlist);
}
