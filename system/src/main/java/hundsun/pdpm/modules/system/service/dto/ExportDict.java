package hundsun.pdpm.modules.system.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author ：yantt21019
 * @date ：Created in 2019/12/20 17:46
 * @description：
 * @version:
 */
@Data
public class ExportDict implements Serializable {

    private  Map<String, Map<String,String>> dictMap;

    private  Map<String,String[]>  values;

}
