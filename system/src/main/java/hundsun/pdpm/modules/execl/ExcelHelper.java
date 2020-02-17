package hundsun.pdpm.modules.execl;


import cn.hutool.core.util.IdUtil;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import hundsun.pdpm.annotation.Excel;
import hundsun.pdpm.modules.system.domain.DictDetail;
import hundsun.pdpm.modules.system.service.dto.BusinessInfoDTO;
import hundsun.pdpm.modules.system.service.dto.ExportDict;
import hundsun.pdpm.utils.StringUtils;
import io.swagger.models.auth.In;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by 78782 on 2018/6/30.
 */
public class ExcelHelper {
    public interface ExcelWriteListener {
        String onWriteRow(Cell cell, Excel excel, String value, Sheet sheet,int sheetIndex , int rowIndex, int cellIndex);
    }
    public interface ExcelImportListener {
        String onImportRow(Sheet sheet,int sheetIndex,Excel excel, int rowIndex, int cellIndex) throws Exception;
    }

    private Workbook workbook;
    private String filename;
    private final static String SPLIT_STR = ",";
    private static final String TYPE_XLSX = ".xlsx";
    private static final String TYPE_XLS = ".xls";
    /**
     * 第一列是否显示序号
     */
    private boolean showIndex;
    public ExcelHelper(String filename) {
        if (filename.endsWith(TYPE_XLS)) {
            this.workbook = new HSSFWorkbook();
        } else {
            this.workbook = new XSSFWorkbook();
        }
        this.filename = filename;
    }

    /**
     * excel导入初始化
     */
    public ExcelHelper(MultipartFile file, boolean showIndex) throws Exception {
        this.showIndex = showIndex;
        InputStream inputStream = file.getInputStream();
        if (file.getOriginalFilename().endsWith(TYPE_XLS)) {
            this.workbook = new HSSFWorkbook(inputStream);
        } else if (file.getOriginalFilename().endsWith(TYPE_XLSX)){
            this.workbook = new XSSFWorkbook(inputStream);
        } else {
            throw new Exception("不支持的文件类型");
        }
    }
    public ExcelHelper(String id, boolean showIndex,boolean impOrExp) {
       this(id);
       this.showIndex = showIndex;
       if(impOrExp){
           Map<String,Object> file = ExeclUtils.fileMap.get(id);
           InputStream inputStream = (InputStream)file.get(ExeclUtils.INPUT_STREAM);
           String originalFilename = (String)file.get(ExeclUtils.ORIGINAL_FILENAME);
           try {
               if (originalFilename.endsWith(TYPE_XLS)) {
                   this.workbook = new HSSFWorkbook(inputStream);
               } else if (originalFilename.endsWith(TYPE_XLSX)){
                   this.workbook = new XSSFWorkbook(inputStream);
               } else {
                   throw new Exception("不支持的文件类型");
               }
           }catch (Exception e){
               ExeclUtils.updateExeclStatus(ExeclUtils.EXECPTION_IMP,id);
           }
       }
    }
    private List<Field> getImportField(List<Field> fields,boolean multiSheet,int index,int sheetIndex,Sheet sheet,Class clazz, ExcelImportListener listener)throws Exception{
        List<Field> fieldList = new ArrayList<>();
        Map<String,Field> fieldMap = new HashMap<>();
        for(Field field:fields){
            Excel excel = field.getAnnotation(Excel.class);
            if(excel!=null){
                fieldMap.put(excel.title(),field);
            }
        }
        String value = listener.onImportRow(sheet,sheetIndex,null,0,index);
        while (value!=null){
            if(fieldMap.containsKey(value)){
                fieldList.add(fieldMap.get(value));
            }
            index++;
            value = listener.onImportRow(sheet,sheetIndex,null,0,index);
        }

        return  fieldList;
    }

    private Field getSheetFiled(Class clazz){
        Field[] fields = clazz.getDeclaredFields();
        Field sheetField = null;
        for (Field field:fields){
            Excel excel = field.getAnnotation(Excel.class);
            if(excel!=null && excel.sheet()){
                sheetField = field;
            }
        }
        return  sheetField;
    }


    private List<List<Field>> getSheetField(boolean multiSheet,Class clazz, int sheetNum){
        Field[] fields = clazz.getDeclaredFields();
        List<List<Field>> allFieldList = new ArrayList<>(sheetNum);
        if(multiSheet){
            //分组
            List<Map<Integer,Field>> fieldMapList = new ArrayList<>(sheetNum);
            for(int i = 0;i < sheetNum; i++){
                fieldMapList.add(new HashMap<>());
            }
            for(Field field : fields){
                Excel excel = field.getAnnotation(Excel.class);
                String order = excel.order();
                if(!StringUtils.isEmpty(order)){
                    String[]  orders = order.split(",");
                    if(orders.length == sheetNum){
                        for(int i = 0;i < sheetNum;i++){
                            Integer  orderNum = Integer.parseInt(orders[i]);
                            if(orderNum > 0){
                                Map<Integer,Field> fieldMap = fieldMapList.get(i);
                                fieldMap.put(orderNum,field);
                            }
                        }
                    }
                }
            }
            //排序
            for(Map<Integer,Field> fieldMap: fieldMapList){
                List<Map.Entry<Integer,Field>> list = new ArrayList<Map.Entry<Integer,Field>>(fieldMap.entrySet());
                Collections.sort(list, new Comparator<Map.Entry<Integer, Field>>() {
                    @Override
                    public int compare(Map.Entry<Integer, Field> o1, Map.Entry<Integer, Field> o2) {
                        return o1.getKey()-o2.getKey();
                    }
                });
                List<Field> fieldList = new ArrayList<>();
                for(Map.Entry<Integer,Field> entry: list){
                    fieldList.add(entry.getValue());
                }
                allFieldList.add(fieldList);
            }

        }else {
            allFieldList.add(Arrays.asList(fields));
        }
        return  allFieldList;
    }

    public static boolean isRowEmpty(Row row) {
     for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
         Cell cell = row.getCell(c);
         if (cell != null && cell.getCellTypeEnum() != CellType.BLANK){
             return false;
         }
     }
     return true;
    }

    private void importFromExcel ( Map<String,List<DictDetail>>  dictMap,List list, Class clazz, ExcelImportListener listener) throws Exception {
        int sheetNum = workbook.getNumberOfSheets();
        Field sheetField  = getSheetFiled(clazz);
        //对于没有sheet字段的默认单sheet
        boolean multiSheet = false;
        if(sheetField == null){
            sheetNum = 1;
        }else {
            multiSheet = true;
        }
        List<List<Field>> allFieldList = getSheetField(multiSheet,clazz,sheetNum);
        for(int sheetIndex = 0;sheetIndex<sheetNum;sheetIndex++){
            Sheet sheet       = workbook.getSheetAt(sheetIndex);
            int totalRowNum   = sheet.getLastRowNum();
            int showIndex     = this.showIndex ? 1 : 0;
            String sheetName  = sheet.getSheetName();
            int startRowIndex = 1;
            List<Field> fieldList = getImportField(allFieldList.get(sheetIndex),multiSheet,showIndex,sheetIndex,sheet,clazz,listener);
            for(int i = startRowIndex ; i <= totalRowNum ; i++) {
                //获得第i行对象
                int index = showIndex;
                Object object = clazz.newInstance();
                //如果空行则跳过
                if(isRowEmpty(sheet.getRow(i))){
                    continue;
                }
                //对于sheet值
                if(sheetField != null){
                    sheetField.setAccessible(true);
                    Excel excel = sheetField.getAnnotation(Excel.class);
                    String value = getCellValue(excel,sheetName,sheetIndex,dictMap);
                    if(value != null){
                        sheetField.set(object, value);
                    }
                }
                for (Field field : fieldList) {
                    field.setAccessible(true);
                    Excel excel = field.getAnnotation(Excel.class);
                    if (excel != null) {
                        if (!excel.export()) {
                            // 该字段未导出，不从excel赋值，而是由values来决定
                            if("uuid".equals(excel.values())) {
                                field.set(object, StringUtils.get32UUID());
                            }
                            continue;
                        }
                        String value = listener.onImportRow(sheet, sheetIndex, excel, i, index);
                        if (value!=null) {
                            if(!StringUtils.isEmpty(excel.tranfer())){
                                if("eightDate".equals(excel.tranfer())){
                                    value = StringUtils.eightDate(value);
                                }
                            }
                            if(excel.plain()&&!StringUtils.isEmpty(value)){
                               try {
                                   value = new BigDecimal(value).toPlainString();
                               }catch (Exception e){
                                   System.out.println("异常：\"" + value + "\"不是数字/整数...");
                               }
                            }

                            if ("java.math.BigDecimal".equals(field.getType().getName())) {
                                field.set(object, BigDecimal.valueOf(Double.valueOf(value)));
                            } else if(field.getType().getName().contains("long")||field.getType().getName().contains("Long")){
                                field.set(object, Long.valueOf(value));
                            } else if("java.sql.Timestamp".equals(field.getType().getName())){
                                if(!StringUtils.isEmpty(value)){
                                    try {
                                        field.set(object, Timestamp.valueOf(value));
                                    }catch (Exception e){
                                        throw new Exception("Execl中["+value+"]不符合规范的日期!");
                                    }
                                }
                            } else if ("java.lang.Double".equals(field.getType().getName())){
                                if(!StringUtils.isEmpty(value)){
                                    field.set(object,Double.valueOf(value));
                                }
                            } else {
                                field.set(object, value);
                            }
                        }
                        index++;
                    }
                }
                list.add(object);
            }
        }
    }




    private void exportExcel(HttpServletResponse response) throws IOException {
        String extension = TYPE_XLSX;
        String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        if (filename.endsWith(TYPE_XLSX)) {
            extension = "";
        } else if (filename.endsWith(TYPE_XLS)) {
            extension = "";
            contentType = "application/vnd.ms-excel";
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            workbook.write(os);
        }catch (IOException e){
            e.printStackTrace();
        }
        try {
            byte[] content = os.toByteArray();
            InputStream is = new ByteArrayInputStream(content);
            response.setHeader("Content-Type", contentType);
            response.setContentLength(content.length);
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename + extension, "utf-8"));
            Cookie cookie = new Cookie("fileDownload", "true");
            cookie.setPath("/");
            response.addCookie(cookie);
            ServletOutputStream outputStream = response.getOutputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            BufferedOutputStream bos = new BufferedOutputStream(outputStream);
            byte[] buff = new byte[8192];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
            bis.close();
            bos.close();
            outputStream.flush();
            outputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void setValidation (Sheet sheet, String[] explicitListValues, CellRangeAddressList addressList) {
        DataValidationHelper dvHelper;
        if (sheet instanceof XSSFSheet) {
            dvHelper = new XSSFDataValidationHelper((XSSFSheet)sheet);
        } else {
            dvHelper = new HSSFDataValidationHelper((HSSFSheet)sheet);
        }
        DataValidationConstraint dvConstraint = dvHelper
                .createExplicitListConstraint(explicitListValues);
        DataValidation validation = dvHelper.createValidation(dvConstraint, addressList);
        validation.setSuppressDropDownArrow(true);
        validation.setShowErrorBox(true);
        sheet.addValidationData(validation);
    }
    public CellStyle createCellStyle () {
        return workbook.createCellStyle();
    }
    public CellStyle getCellStyle(boolean bold, boolean hasBorder) {
        return getCellStyle(bold, hasBorder, false);
    }
    private CellStyle getCellStyle(boolean bold, boolean hasBorder, boolean islink) {
        Font font = workbook.createFont();
        font.setFontName("微软雅黑");
        font.setBold(bold);
        if (islink) {
            font.setUnderline((byte) 1);
            font.setColor(IndexedColors.BLUE.index);
        } else {
            font.setColor(IndexedColors.BLACK.index);
        }
        font.setFontHeightInPoints((short)(10)); // 字号
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setWrapText(true); // 自动换行
        // 四周边框
        if (hasBorder) {
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
        }
        return cellStyle;
    }
    private void writeTitles (Sheet sheet,List<Field> fields, ExcelWriteListener listener) {
        Row rowTitle = sheet.createRow(0);
        int cellIndex = 0;
        CellStyle cellStyle = getCellStyle(true, true);
        if (this.showIndex) {
            Cell cellOrder = rowTitle.createCell(cellIndex);
            cellOrder.setCellStyle(cellStyle);
            cellOrder.setCellValue("序号");
            cellIndex++;
        }
        for (Field field : fields) {
            Excel excel = field.getAnnotation(Excel.class);
            if (excel != null && excel.export()) {
                Cell cell = rowTitle.createCell(cellIndex);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(excel.title());
                cellIndex++;
            }
        }
    }
    private void writeRows (Sheet sheet, int sheetIndex, JSONArray  jsonArray,List<Field> fields, ExcelWriteListener listener) throws IllegalAccessException {
        int len = jsonArray.size();
        CellStyle cellStyle = getCellStyle(false, true);
        CellStyle cellLinkStyle = getCellStyle(false, true, true);
        Map<String,Excel> execlMap = new HashMap<>();
        Map<String,Integer> autoSizeMap = new HashMap<>();
        for (int i=0;i<len;i++) {
            Row row = sheet.createRow(i+1);
            int cellIndex = 0;
            if (this.showIndex) {
                // 第一列设置序号
                Cell cellOrder = row.createCell(cellIndex);
                cellOrder.setCellStyle(cellStyle);
                listener.onWriteRow(cellOrder, null, null, sheet,sheetIndex, i, cellIndex);
                cellOrder.setCellValue(i + 1);
                cellIndex++;
            }
            JSONObject jo = (JSONObject) JSONObject.toJSON(jsonArray.get(i));
            for (Field field : fields) {
                String fieldName = field.getName();
                Excel excel;
                if(execlMap.containsKey(fieldName)){
                    excel =execlMap.get(fieldName);
                }else {
                    excel = field.getAnnotation(Excel.class);
                    execlMap.put(fieldName,excel);
                }
                if (excel!=null && excel.export()) {
                    Cell cell = row.createCell(cellIndex);
                    String value =  StringUtils.nvl(jo.getString(fieldName),"");
                    String result = listener.onWriteRow(cell, excel, value, sheet,sheetIndex, i, cellIndex);
                    // 先交由外部处理，处理完毕后如果得到结果则填值，否则采用默认处理方式。
                    if (result != null) {
                        cell.setCellValue(result);
                    } else {
                        if (excel.link()) {
                            CreationHelper createHelper = workbook.getCreationHelper();
                            Hyperlink hyperlink = createHelper.createHyperlink(HyperlinkType.DOCUMENT);
                            //value表示sheet页名称  "A1"表示第几列第几行
                            hyperlink.setAddress(value + "!A1");
                            cell.setHyperlink(hyperlink);
                            cell.setCellStyle(cellLinkStyle);
                        }
                        if (!"".equals(excel.dictname())) {
                            // 此处需要查询字典获取 字典
                            cell.setCellValue(value);
                        } else if (!"".equals(value) && !"".equals(excel.values())) {
                            String[] values = excel.values().split(",");
                            String temp = "";
                            try {
                                temp = values[Integer.parseInt(value)];
                            } catch ( Exception e ) {
                                temp = value;
                            } finally {
                                cell.setCellValue(temp);
                            }
                        } else {
                            cell.setCellValue(value);
                        }
                    }
                    if (excel.autosize()) {
                        int min = autoSizeMap.get(fieldName)==null?excel.title().getBytes().length:autoSizeMap.get(fieldName);
                        int max =  value.getBytes().length;
                        if(value.indexOf('\n') != -1){
                            String[] arr = value.split("\n");
                            if(arr.length>1){
                                max = 0;
                                for(String temp: arr){
                                    if(max < temp.length()){
                                        max = temp.length();
                                    }
                                }
                            }
                        }
                        autoSizeMap.put(fieldName,Math.max(min, max));
                        if(i == len-1){
                            int colWidth = autoSizeMap.get(fieldName);
                            if(colWidth > 255){
                              colWidth = 255;
                            }
                            colWidth *= 256;
//                            if(colWidth < 255*256){
//                               if(colWidth < 3000){
//                                   colWidth = 3000;
//                               }
//                            }else {
//                                colWidth = 6000;
//                            }
                            sheet.setColumnWidth(cellIndex,colWidth);
                        }
                    }
                    if(i == len-1){
                        if(excel.colwidth()>0){
                            sheet.setColumnWidth(cellIndex,excel.colwidth());
                        }
                    }
                    cellIndex++;
                }

            }
        }
    }

    private  List<JSONArray> getSheetData(Field sheetField,boolean multiSheet ,List<DictDetail> details,JSONArray jsonArray){
        List<JSONArray> jsonArrayList  = new ArrayList<>();
        if(multiSheet){
            Map<String,Integer> sheetIndex = new HashMap<>();
            String fieldName = sheetField.getName();
            for(int i = 0;i < details.size();i++){
                jsonArrayList.add(new JSONArray());
                String sheetValue = details.get(i).getValue();
                sheetIndex.put(sheetValue,i);
            }
            for(Object obj : jsonArray){
                JSONObject jo = (JSONObject) JSONObject.toJSON(obj);
                String value =  StringUtils.nvl(jo.getString(fieldName),"");
                jsonArrayList.get(sheetIndex.get(value)).add(obj);
            }
        }else {
            jsonArrayList.add(jsonArray);
        }
        return  jsonArrayList;
    }

    public <T> void writeExcel( Map<String,List<DictDetail>> dictMap ,JSONArray  jsonArray,Class clazz, ExcelWriteListener listener) throws IllegalAccessException {
        Field sheetField  = getSheetFiled(clazz);
        //对于没有sheet字段的默认单sheet
        boolean multiSheet = false;
        int sheetNum = 1;
        String sheetname = "Sheet 1";
        List<DictDetail> dictDetails = null;

        if(sheetField != null){
            Excel excel = sheetField.getAnnotation(Excel.class);
            if(excel !=null && !StringUtils.isEmpty(excel.dictname())){
                dictDetails = dictMap.get(excel.dictname());
                if(dictDetails.size() > 1){
                    multiSheet = true;
                    sheetNum = dictDetails.size();
                }
            }
        }
        List<List<Field>> allFieldList = getSheetField(multiSheet,clazz,sheetNum);
        List<JSONArray> jsonArrayList  = getSheetData(sheetField,multiSheet,dictDetails,jsonArray);
        for(int i = 0;i< sheetNum;i++){
            List<Field> fields =  allFieldList.get(i);
            JSONArray jsons = jsonArrayList.get(i);
            if(jsons == null || jsons.size() < 1){
                continue;
            }
            if(multiSheet){
                sheetname = dictDetails.get(i).getLabel();
            }
            Sheet sheet = workbook.createSheet(sheetname);
            writeTitles(sheet, fields, listener);
            writeRows(sheet,i, jsons, fields, listener);
        }
    }
    public void close() {
        try {
            if (workbook != null) {
                workbook.close();
            }
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }
    private  static String getDictValue(List<DictDetail> dictionaryBeans, String value){
        for (DictDetail dictionaryBean1 : dictionaryBeans) {
            if (dictionaryBean1.getValue().equals(value)) {
                return dictionaryBean1.getLabel();
            }
        }
        return  value;
    }
    private  static String getDictKey(List<DictDetail> dictionaryBeans, String value){
        for (DictDetail dictionaryBean1 : dictionaryBeans) {
            if (dictionaryBean1.getLabel().equals(value)) {
                return dictionaryBean1.getValue();
            }
        }
        return  value;
    }


    public static <T> void exportExcel (HttpServletResponse response,List<T> list,Map<String,List<DictDetail>> dictMap, Class clazz,boolean showIndex) {
        ExcelHelper excelHelper = new ExcelHelper(IdUtil.fastSimpleUUID(), showIndex,false);
        JSONArray jsonArray = JSON.parseArray(JSON.toJSONString(list));
        // 设置单元格样式
        CellStyle cellStyle = excelHelper.getCellStyle(false, true);
        cellStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        try {
            // 写入到excel
            excelHelper.writeExcel(dictMap,jsonArray, clazz, new ExcelHelper.ExcelWriteListener() {
                @Override
                public String onWriteRow(Cell cell, Excel excel, String value, Sheet sheet,int sheetIndex, int rowIndex, int cellIndex) {
                    // 设置单元格样式
                    if(excel != null ){
                        if(excel.align() == Excel.Align.ALIGN_LEFT){
                            cellStyle.setAlignment(HorizontalAlignment.LEFT);
                        }else if(excel.align() == Excel.Align.ALIGN_RIGHT){
                            cellStyle.setAlignment(HorizontalAlignment.RIGHT);
                        }
                        cell.setCellStyle(cellStyle);
                        if ( !"".equals(excel.dictname())) {
                            // 字典转换
                            List<DictDetail> dictionaryBeans = null;
                            String[] dictNameArr = excel.dictname().split(",");
                            if(dictNameArr.length>1){
                                dictionaryBeans =  dictMap.get(dictNameArr[sheetIndex]);
                            }else {
                                dictionaryBeans =  dictMap.get(excel.dictname());
                            }
                            // 读取字典的 设置 数据验证
                            if (rowIndex == 0) {
                                String[] values = new String[dictionaryBeans.size()];
                                for (int i = 0; i < dictionaryBeans.size(); i++) {
                                    values[i] = dictionaryBeans.get(i).getLabel();
                                }
                                excelHelper.setValidation(sheet, values, new CellRangeAddressList(1, list.size(), cellIndex, cellIndex));
                            }
                            String dictValue = value;
                            if (dictionaryBeans != null) {
                                //对于字段有分隔符的需要分割循环获取
                                if(!StringUtils.isEmpty(value)){
                                    String[] dictArr = value.split(SPLIT_STR);
                                    dictValue = getDictValue(dictionaryBeans,dictArr[0]);
                                    for(int i = 1;i< dictArr.length;i++){
                                        dictValue += SPLIT_STR+ getDictValue(dictionaryBeans,dictArr[i]);
                                    }
                                }
                            }
                            return dictValue;
                        }
                    }
                    return null;
                }
            });
            excelHelper.exportExcel(response);
        } catch ( IllegalAccessException | IOException e) {
            e.printStackTrace();
        } finally {
            excelHelper.close();
        }
    }


    public static <T>  List<T> importExcel (String id, Class clazz, Map<String,List<DictDetail>>  dictMap, boolean showIndex) throws Exception {
        List<T> result = new ArrayList<>();

        if (!ExeclUtils.fileMap.containsKey(id)) {
            throw new Exception("未找到上传数据");
        }
        ExcelHelper excelHelper = new ExcelHelper(id, showIndex,true);
        excelHelper.importFromExcel(dictMap,result, clazz, (sheet,sheetIndex, excel, rowIndex, cellIndex) -> {
            Row row = sheet.getRow(rowIndex);
            String value;
            if(row.getCell(cellIndex) == null ){
                value = null;
            } else {
                row.getCell(cellIndex).setCellType(CellType.STRING);
                Cell cell = row.getCell(cellIndex);
                value = cell.getStringCellValue();
                if (cell.getCellTypeEnum().equals(CellType.NUMERIC)){
                    double cellValue;
                    cellValue = cell.getNumericCellValue();
                    // 百分比识别
                    if (excel!= null && excel.percent()) {
                        value =Math.round(cellValue * 100) + "%";
                    }
                }
            }
            return getCellValue(excel,value,sheetIndex,dictMap);
        });
        return result;
    }

    public static <T>  List<T> importExcel (MultipartFile file, Class clazz, Map<String,List<DictDetail>>  dictMap, boolean showIndex) throws Exception {
        List<T> result = new ArrayList<>();

        if (file == null) {
            throw new Exception("未找到上传数据");
        }
        ExcelHelper excelHelper = new ExcelHelper(file, showIndex);
        excelHelper.importFromExcel(dictMap,result, clazz, (sheet,sheetIndex, excel, rowIndex, cellIndex) -> {
            Row row = sheet.getRow(rowIndex);
            String value;
            if(row.getCell(cellIndex) == null ){
                value = null;
            } else  {
                row.getCell(cellIndex).setCellType(CellType.STRING);
                Cell cell = row.getCell(cellIndex);
                value = cell.getStringCellValue();
                if (cell.getCellTypeEnum().equals(CellType.NUMERIC)) {
                    double cellValue;
                    cellValue = cell.getNumericCellValue();
                    // 百分比识别
                    if (excel!= null && excel.percent()) {
                        value = String.valueOf(Math.round(cellValue * 100)) + "%";
                    }
                }
            }
            return getCellValue(excel,value,sheetIndex,dictMap);
        });
        return result;
    }

    private static String getCellValue(Excel excel,String value,int sheetIndex, Map<String,List<DictDetail>>  dictMap){
        String dictValue = value;
        if (excel != null && !"".equals(excel.dictname())) {
            List<DictDetail> dictionaryBeans = null;
            String[] dictNameArr = excel.dictname().split(",");
            if(dictNameArr.length>1){
                dictionaryBeans =  dictMap.get(dictNameArr[sheetIndex]);
            }else {
                dictionaryBeans =  dictMap.get(excel.dictname());
            }
            if (dictionaryBeans != null) {
                if(!StringUtils.isEmpty(value)){
                    String[] dictArr = value.split(SPLIT_STR);
                    dictValue = getDictKey(dictionaryBeans,dictArr[0]);
                    for(int i = 1;i< dictArr.length;i++){
                        dictValue += SPLIT_STR+ getDictKey(dictionaryBeans,dictArr[i]);
                    }
                }
            }
        }
        return dictValue;
    }

}
