package ${package}.service.dto;

import lombok.Data;
import hundsun.pdpm.annotation.Excel;
<#if hasTimestamp>
import java.sql.Timestamp;
</#if>
<#if hasBigDecimal>
import java.math.BigDecimal;
</#if>
import java.io.Serializable;
<#if !auto && pkColumnType = 'Long'>
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
</#if>


/**
* @author ${author}
* @date ${date}
*/
@Data
public class ${className}DTO implements Serializable {
<#if columns??>
    <#list columns as column>

    <#if column.columnComment != ''>
    // ${column.columnComment}
    </#if>
    <#if column.columnKey = 'PRI'>
    <#if !auto && pkColumnType = 'Long'>
    // 处理精度丢失问题
    @JsonSerialize(using= ToStringSerializer.class)
    </#if>
    </#if>
    <#if column.dictName??>
    @Excel(title = "${column.columnComment}", dictname = "${column.dictName}")
    <#else>
    @Excel(title = "${column.columnComment}")
    </#if>
    private ${column.columnType} ${column.changeColumnName};
    </#list>
</#if>
}
