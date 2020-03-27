package hundsun.pdpm.modules.system.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author ：yantt21019
 * @date ：Created in 2020/03/26 13:51
 * @description：
 * @version:
 */
@Data
public class FunctionInfoCust implements Serializable {

    private  String productId;

    private List<String>  functionNames;
}
