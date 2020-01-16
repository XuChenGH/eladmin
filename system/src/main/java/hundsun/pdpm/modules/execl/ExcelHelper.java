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
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.hssf.usermodel.*;
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
        String onWriteRow(Cell cell, Excel excel, String value, Sheet sheet, int rowIndex, int cellIndex);
    }
    public interface ExcelImportListener {
        String onImportRow(Sheet sheet, Excel excel, int rowIndex, int cellIndex) throws Exception;
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
     * excel导出
     * @param filename 导出的excel名称
     * @param showIndex 是否含有序号
     */
    public ExcelHelper(String filename, boolean showIndex) {
        this(filename);
        this.showIndex = showIndex;
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

    private void importFromExcel (List list, Class clazz, ExcelImportListener listener) throws Exception {
        Sheet sheet = workbook.getSheetAt(0);
        int totalRowNum = sheet.getLastRowNum();
        //获得所有数据
        int startRowIndex = 1;
        for(int i = startRowIndex ; i <= totalRowNum ; i++) {
            //获得第i行对象
            Field[] fields = clazz.getDeclaredFields();
            int index = this.showIndex ? 1 : 0;
            Object object = clazz.newInstance();
            for (Field field : fields) {
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
                    String value = listener.onImportRow(sheet, excel, i, index);
                    if (value!=null) {
                        if(!StringUtils.isEmpty(excel.tranfer())){
                            if("eightDate".equals(excel.tranfer())){
                                value = StringUtils.eightDate(value);
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
    private void writeTitles (Sheet sheet, Class clazz, ExcelWriteListener listener) {
        Row rowTitle = sheet.createRow(0);
        Field[] fields = clazz.getDeclaredFields();
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
    private void writeRows (Sheet sheet,  JSONArray  jsonArray, Class clazz, ExcelWriteListener listener) throws IllegalAccessException {
        int len = jsonArray.size();
        CellStyle cellStyle = getCellStyle(false, true);
        CellStyle cellLinkStyle = getCellStyle(false, true, true);
        Map<String,Excel> execlMap = new HashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        Map<String,Integer> autoSizeMap = new HashMap<>();
        for (int i=0;i<len;i++) {
            Row row = sheet.createRow(i+1);
            int cellIndex = 0;
            if (this.showIndex) {
                // 第一列设置序号
                Cell cellOrder = row.createCell(cellIndex);
                cellOrder.setCellStyle(cellStyle);
                listener.onWriteRow(cellOrder, null, null, sheet, i, cellIndex);
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
                    String result = listener.onWriteRow(cell, excel, value, sheet, i, cellIndex);
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
                        autoSizeMap.put(fieldName,Math.max(autoSizeMap.get(fieldName)==null?0:autoSizeMap.get(fieldName), value.getBytes().length));
                        if(i == len-1){
                            int colWidth = autoSizeMap.get(fieldName)*256;
                            if(colWidth < 255*256){
                               if(colWidth < 3000){
                                   colWidth = 3000;
                               }
                            }else {
                                colWidth = 6000;
                            }
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
    public <T> void writeExcel(String sheetname,  JSONArray  jsonArray, Class clazz, ExcelWriteListener listener) throws IllegalAccessException {
        Sheet sheet = workbook.createSheet(sheetname);
        writeTitles(sheet, clazz, listener);
        writeRows(sheet, jsonArray, clazz, listener);
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

    private static ExportDict getExportDict(Map<String,List<DictDetail>> dictMap){
        ExportDict exportDict = new ExportDict();
        Map<String, Map<String,String>> dict = new HashMap<>();
        Map<String,String[]> dictArray = new HashMap<>();
        exportDict.setDictMap(dict);
        exportDict.setValues(dictArray);
        for(Map.Entry<String,List<DictDetail>> entry:dictMap.entrySet()){
            Map<String,String> dictValue = new HashMap<>();
            dict.put(entry.getKey(),dictValue);
            String[] values = new String[entry.getValue().size()];
            dictArray.put(entry.getKey(),values);
            List<DictDetail> details = entry.getValue();
            for (int i = 0; i< details.size();i++){
                dictValue.put(details.get(i).getValue(),details.get(i).getLabel());
                values[i] = details.get(i).getLabel();
            }

        }
        return  exportDict;
    }

    public static <T> void exportExcel (HttpServletResponse response,List<T> list,Map<String,List<DictDetail>> dictMap, Class clazz,boolean showIndex) {
        ExcelHelper excelHelper = new ExcelHelper(IdUtil.fastSimpleUUID(), showIndex);
        JSONArray jsonArray = JSON.parseArray(JSON.toJSONString(list));
        ExportDict exportDict = getExportDict(dictMap);
        Map<String, Map<String,String>> dicts = exportDict.getDictMap();
        Map<String,String[]> dictLabels = exportDict.getValues();
        // 设置单元格样式
        CellStyle cellStyle = excelHelper.getCellStyle(false, true);
        cellStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        int listLength = list.size();
        try {
            // 写入到excel
            excelHelper.writeExcel("Sheet1", jsonArray, clazz, new ExcelHelper.ExcelWriteListener() {
                @Override
                public String onWriteRow(Cell cell, Excel excel, String value, Sheet sheet, int rowIndex, int cellIndex) {
                    // 设置单元格样式
                    CellStyle cellStyle = excelHelper.getCellStyle(false, true);
                    cellStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
                    cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    cell.setCellStyle(cellStyle);
                    if (excel != null && !"".equals(excel.dictname())) {
                        // 字典转换
                        List<DictDetail> dictionaryBeans = dictMap.get(excel.dictname());
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

    public static <T>  List<T> importExcel (MultipartFile file, Class clazz, Map<String,List<DictDetail>>  dictMap, boolean showIndex) throws Exception {
        List<T> result = new ArrayList<>();

        if (file == null) {
            throw new Exception("未找到上传数据");
        }
        ExcelHelper excelHelper = new ExcelHelper(file, showIndex);
        excelHelper.importFromExcel(result, clazz, (sheet, excel, rowIndex, cellIndex) -> {
            Row row = sheet.getRow(rowIndex);
            Cell cell = row.getCell(cellIndex);
            String value;
            if(cell == null ){
                value = null;
            } else  if (cell.getCellTypeEnum().equals(CellType.NUMERIC)) {
                double cellValue;
                cellValue = cell.getNumericCellValue();
                // 百分比识别
                if (excel!= null && excel.percent()) {
                    value = String.valueOf(Math.round(cellValue * 100)) + "%";
                } else {
                    value = String.valueOf(cellValue);
                }
            } else {
                value = cell.getStringCellValue();
            }
            String dictValue = value;
            if (excel != null && !"".equals(excel.dictname())) {
                List<DictDetail> dictionaryBeans = dictMap.get(excel.dictname());
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
        });
        return result;
    }

}
