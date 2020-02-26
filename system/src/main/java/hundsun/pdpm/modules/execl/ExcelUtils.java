package hundsun.pdpm.modules.execl;

import hundsun.pdpm.utils.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ：yantt21019
 * @date ：Created in 2020/02/15 13:49
 * @description：
 * @version:
 */

public class ExcelUtils {

    public  static ConcurrentHashMap<String, Map<String,Object>> execlMap = new ConcurrentHashMap<>();

    public  static ConcurrentHashMap<String, Map<String,Object>> fileMap = new ConcurrentHashMap<>();

    public static  final  String INPUT_STREAM = "inputStream";

    public static  final  String ORIGINAL_FILENAME = "originalFilename";

    public static  final  String START_IMP = "开始准备导入数据";

    public static  final  String EXECPTION_IMP = "导入异常";

    public static  final  String FINISH_IMP = "导入完成";

    public static  final  String INSERT_IMP = "插入数据库";


    public static  final  String START_EXP = "开始准备导出数据";

    public static  final  String EXECPTION_EXP = "导出异常";

    public static  final  String FINISH_EXP = "导入完成";


    public static  final  String  STATUS_FIELD = "status";

    public static  final  String CODE_FIELD = "code";

    public static  final  String COUNT_FIELD = "count";

    public static  final  String HAVE_INSERT_FIELD = "haveInsert";

    public static  final  String EXECL_FIELD = "execl";
    //进行中
    public static  final  String CODE_0 = "0";
    //完成
    public static  final  String CODE_1= "1";
    //异常
    public static  final  String CODE_2 = "2";
    //插入数据库
    public static  final  String CODE_3 = "3";



    public  static  Map<String,Object> getExeclMap(String id){
        return  execlMap.get(id);
    }

    public  static  String getExeclCode(String id){
        return  (String) execlMap.get(id).get(STATUS_FIELD);
    }

    public static  void saveFile(String id, MultipartFile file){
        try {
            InputStream inputStream = file.getInputStream();
            String originalFilename = file.getOriginalFilename();
            Map<String,Object> map = new HashMap<>();
            map.put(INPUT_STREAM,inputStream);
            map.put(ORIGINAL_FILENAME,originalFilename);
            fileMap.put(id,map);
        }catch (Exception e){
            e.printStackTrace();
            updateExeclStatus(EXECPTION_IMP,id);
        }
    }

    public  static void updateExeclStatus(String status, String id ){
        Map<String,Object> statusMap;
        if(!execlMap.containsKey(id)){
            statusMap = new HashMap<>();
            execlMap.put(id,statusMap);
        }else {
            statusMap = execlMap.get(id);
        }
        statusMap.put(STATUS_FIELD,status);
        if(StringUtils.equals(status,START_IMP)){
            statusMap.put(CODE_FIELD,CODE_0);
        }else  if(StringUtils.equals(status,FINISH_IMP)){
            statusMap.put(CODE_FIELD,CODE_1);
        }else  if(StringUtils.equals(status,EXECPTION_IMP)){
            statusMap.put(CODE_FIELD,CODE_2);
        }else  if(StringUtils.equals(status,INSERT_IMP)){
            statusMap.put(CODE_FIELD,CODE_0);
        }
    }

    public  static  void updateExeclInserNum(int count,int num,String id){
        if(execlMap.containsKey(id)){
            execlMap.get(id).put(COUNT_FIELD,count);
            execlMap.get(id).put(HAVE_INSERT_FIELD,num);
        }
    }



}
