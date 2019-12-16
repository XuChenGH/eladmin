<#--noinspection ALL-->
<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
    <#if hasQuery>
      <!-- 搜索 -->
      <el-form ref="queryForm" :model="form"  size="small" label-width="100px">
        <#if columns??>
          <#list columns as column>
            <#if column.changeColumnName != '${pkChangeColName}'>
              <el-form-item label="<#if column.columnComment != ''>${column.columnComment}<#else>${column.changeColumnName}</#if>"  prop="${column.changeColumnName}">
                <#if column.dictName??>
                  <el-select v-model="form.${column.changeColumnName}" filterable  placeholder="请选择">
                    <el-option
                            v-for="item in  dict.${column.dictName}"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value" ></el-option>
                  </el-select>
                <#elseif column.columnType != 'Timestamp'>
                  <el-input v-model="form.${column.changeColumnName}" style="width: 200px;"/>
                <#else>
                  <el-date-picker v-model="form.${column.changeColumnName}" type="datetime" style="width: 200px;"/>
                </#if>
              </el-form-item>
            </#if>
          </#list>
        </#if>
      </el-form>
      <el-button class="filter-item" size="mini" type="success" icon="el-icon-search" @click="toQuery">搜索</el-button>
      <el-button class="filter-item" size="mini" type="primary" icon="el-icon-refresh-right" @click="resetQuery">重置</el-button>
    </#if>
     </div>
     <div class="head-container">
        <!-- 新增 -->
         <el-button
                v-permission="['admin','${changeClassName}:add']"
                class="filter-item"
                type="primary"
                size="mini"
                icon="el-icon-plus"
                style="margin-left: 20px"
                @click="add">新增</el-button>
        <!-- 导出 -->
         <el-dropdown
                 class="filter-item"
                 type="primary"
                 icon="el-icon-download"
                 style="margin-left: 20px"
                 v-permission="['admin','${changeClassName}:export']"
                 @command="download">
           <el-button type="primary" size="mini">
             导出EXECL<i class="el-icon-arrow-down el-icon--right"></i>
           </el-button>
           <el-dropdown-menu slot="dropdown">
             <el-dropdown-item command="导出全部">导出全部</el-dropdown-item>
             <el-dropdown-item command="导出选择">导出选择</el-dropdown-item>
           </el-dropdown-menu>
         </el-dropdown>
        <!--导入-->
          <el-button
                  v-permission="['admin','${changeClassName}:import']"
                  class="filter-item"
                  type="primary"
                  size="mini"
                  style="margin-left: 20px"
                  icon="el-icon-upload"
                  @click="upload">导入EXECL
          </el-button>
     </div>
    <!--表单组件-->
    <eForm ref="form" :operate="operate" <#if isHasDict>${dictRefJoint}</#if>/>
    <!--导入表单-->
    <uploadForm ref="upform" :uploadApi="uploadApi"/>
    <!--表格渲染-->
    <el-table v-loading="loading" :data="data" size="small" style="width: 100%;" @selection-change="handleSelectionChange" @row-dblclick="rowDoubleClick">
      <el-table-column
              type="selection"
              width="55">
      </el-table-column>
      <#if columns??>
          <#list columns as column>
          <#if column.columnShow = 'true'>
              <#if column.dictName??>
      <el-table-column prop="${column.changeColumnName}" label="<#if column.columnComment != ''>${column.columnComment}<#else>${column.changeColumnName}</#if>">
        <template slot-scope="scope">
          <div v-if=" scope.row.${column.changeColumnName} in dict.${column.dictName}">{{dict.${column.dictName}[scope.row.${column.changeColumnName}].label}}</div>
          <div v-else-if=" !scope.row.${column.changeColumnName} in dict.${column.dictName}">{{scope.row.${column.changeColumnName}}}</div>
        </template>
      </el-table-column>
              <#elseif column.columnType != 'Timestamp'>
      <el-table-column prop="${column.changeColumnName}" label="<#if column.columnComment != ''>${column.columnComment}<#else>${column.changeColumnName}</#if>"/>
              <#else>
      <el-table-column prop="${column.changeColumnName}" label="<#if column.columnComment != ''>${column.columnComment}<#else>${column.changeColumnName}</#if>">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.${column.changeColumnName}) }}</span>
        </template>
      </el-table-column>
              </#if>
          </#if>
          </#list>
      </#if>
      <el-table-column v-if="checkPermission(['admin','${changeClassName}:edit','${changeClassName}:del'])" label="操作" width="150px" align="center">
        <template slot-scope="scope">
          <el-button v-permission="['admin','${changeClassName}:edit']" size="mini" type="primary" icon="el-icon-edit" @click="edit(scope.row)"/>
          <el-popover
            v-permission="['admin','${changeClassName}:del']"
            :ref="scope.row.${pkChangeColName}"
            placement="top"
            width="180">
            <p>确定删除本条数据吗？</p>
            <div style="text-align: right; margin: 0">
              <el-button size="mini" type="text" @click="$refs[scope.row.${pkChangeColName}].doClose()">取消</el-button>
              <el-button :loading="delLoading" type="primary" size="mini" @click="subDelete(scope.row.${pkChangeColName})">确定</el-button>
            </div>
            <el-button slot="reference" type="danger" icon="el-icon-delete" size="mini"/>
          </el-popover>
        </template>
      </el-table-column>
    </el-table>
    <!--分页组件-->
    <el-pagination
      :total="total"
      :current-page="page + 1"
      style="margin-top: 8px;"
      layout="total, prev, pager, next, sizes"
      @size-change="sizeChange"
      @current-change="pageChange"/>
  </div>
</template>

<script>
import checkPermission from '@/utils/permission'
import initData from '@/mixins/initData'
import { del, download${className} } from '@/api/${changeClassName}'
import uploadForm from  '@/views/business/upload/form'
import { parseTime, downloadFile, deepClone } from '@/utils/index'
import eForm from './form'
export default {
  components: { eForm, uploadForm },
  mixins: [initData],
  <#if isHasDict>
  dicts:[${dictJoint}],
  </#if>
  data() {
    return {
      delLoading: false,
      uploadApi:'api/${changeClassName}/upload',
      multipleSelection:[],
      form: {
        <#if columns??>
        <#list columns as column>
        ${column.changeColumnName}: ''<#if column_has_next>,</#if>
        </#list>
        </#if>
      }
    }
  },
  created() {
    this.$nextTick(() => {
      this.init()
    })
  },
  methods: {
  <#if hasTimestamp>
    parseTime,
  </#if>
    checkPermission,
    beforeInit() {
      this.url = 'api/${changeClassName}'
      const sort = '${pkChangeColName},desc'
      this.params = { page: this.page, size: this.size, sort: sort }
      <#if hasQuery>
      Object.assign(this.params,this.form);
      </#if>
      return true
    },
    subDelete(${pkChangeColName}) {
      this.delLoading = true
      del(${pkChangeColName}).then(res => {
        this.delLoading = false
        this.$refs[${pkChangeColName}].doClose()
        this.dleChangePage()
        this.init()
        this.$notify({
          title: '删除成功',
          type: 'success',
          duration: 2500
        })
      }).catch(err => {
        this.delLoading = false
        this.$refs[${pkChangeColName}].doClose()
        console.log(err.response.data.message)
      })
    },
    add() {
      this.operate = '新增';
      this.$refs.form.dialog = true
    },
    edit(data) {
      this.operate = '修改'
      const _this = this.$refs.form
      _this.form = {
        <#if columns??>
        <#list columns as column>
        ${column.changeColumnName}: data.${column.changeColumnName}<#if column_has_next>,</#if>
        </#list>
        </#if>
      }
      _this.dialog = true
    },
    handleSelectionChange(val){
      this.multipleSelection = val;
    },
    rowDoubleClick(row){
      const _this = this.$refs.form;
      _this.form = deepClone(row);
      this.operate = '详情';
      _this.dialog = true
    },
    // 导出
    download(command) {
      var data = [];
      if(command ==='导出选择'){
        if(!this.multipleSelection.length){
          this.$message({
            message: '请选择要导出的数据',
            type: 'warning'
          });
          return;
        }
        data = this.multipleSelection ;
      }
      this.downloadLoading = true
      download${className}(data).then(result => {
        downloadFile(result, '${className}列表', 'xlsx')
      this.downloadLoading = false
    }).catch(() => {
        this.downloadLoading = false
      })
    },
    upload(){
      this.$refs.upform.dialog=true;
    },
    resetQuery(){
      this.$refs['queryForm'].resetFields();
    }
  }
}
</script>

<style scoped>

</style>
