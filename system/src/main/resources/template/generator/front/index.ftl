<#--noinspection ALL-->
<template>
  <div class="app-container">
    <!--工具栏-->
    <el-collapse>
      <el-collapse-item   title="搜索查询" name="1">
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
      </el-collapse-item>
      <el-collapse-item  v-if="checkPermission(['admin','${changeClassName}:add','${changeClassName}:edit','${changeClassName}:del','${changeClassName}:export','${changeClassName}:import'])" title="操作" name="2">
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
          <el-button
                  v-if="multipleSelection.length === 1"
                  v-permission="['admin','${changeClassName}:edit']"
                  class="filter-item"
                  type="primary"
                  size="mini"
                  icon="el-icon-edit"
                  style="margin-left: 20px;margin-bottom: 4px"
                  @click="edit(multipleSelection[0])">修改</el-button>
          <el-popover
                  v-if="multipleSelection.length !== 1"
                  v-permission="['admin','${changeClassName}:edit']"
                  :ref="multipleSelection"
                  placement="top"
                  width="180">
            <p v-if="multipleSelection.length <1">请选择一条数据!</p>
            <p v-else-if="multipleSelection.length > 1">只能选择一条数据!</p>
            <el-button slot="reference" style="margin-left: 20px" size="mini" type="primary" icon="el-icon-edit" >修改</el-button>
          </el-popover>
          <el-popover
                  v-permission="['admin','${changeClassName}:del']"
                  ref="delpopver"
                  placement="top"
                  width="200">
            <p v-if="multipleSelection.length ===1">确定删除本条数据吗？</p>
            <p v-else-if="multipleSelection.length <1">请选择一条数据!</p>
            <p v-else-if="multipleSelection.length > 1">只能选择一条数据!</p>
            <div style="text-align: right; margin: 0" v-if="multipleSelection.length ===1">
              <el-button size="mini" type="text" @click="$refs['delpopver'].doClose()">取消</el-button>
              <el-button :loading="delLoading" type="primary" size="mini" @click="subDelete(multipleSelection[0].id)">确定</el-button>
            </div>
            <el-button slot="reference"  style="margin-left: 20px" size="mini" type="danger" icon="el-icon-delete" >删除</el-button>
          </el-popover>
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
      </el-collapse-item>
    </el-collapse>


    <!--表单组件-->
    <eForm ref="form" :operate="operate" <#if isHasDict>${dictRefJoint}</#if>/>
    <!--导入表单-->
    <uploadForm ref="upform" :uploadApi="uploadApi"/>
    <!--表格渲染-->
    <el-table v-loading="loading" :data="data" size="small" style="width: 100%;"
              @selection-change="handleSelectionChange"
              @row-dblclick="rowDoubleClick"
              :max-height="clientHeight"
              @contextmenu.prevent.native="$refs.rightMenu.openMenu($event,checkPermission(['admin','${changeClassName}:add']))"
              @row-contextmenu="rowContextMenu">
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
          <div>{{getDictCaption(scope.row.${column.changeColumnName},dict.${column.dictName})}}</div>
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
    </el-table>
    <!--分页组件-->
    <el-pagination
      :total="total"
      :current-page="page + 1"
      style="margin-top: 8px;"
      layout="total, prev, pager, next, sizes"
      @size-change="sizeChange"
      @current-change="pageChange"/>
    <right-menu ref="rightMenu" :menu="menu"/>
  </div>
</template>

<script>
import checkPermission from '@/utils/permission'
import initData from '@/mixins/initData'
import { del, download${className} } from '@/api/${changeClassName}'
import uploadForm from  '@/views/business/upload/form'
import { parseTime, downloadFile, deepClone<#if isHasDict>, getDictCaption </#if>} from '@/utils/index'
import rightMenu from '@/views/business/rightmenu/index'
import eForm from './form'
export default {
  components: { eForm, uploadForm, rightMenu },
  mixins: [initData],
  <#if isHasDict>
  dicts:[${dictJoint}],
  </#if>
  data() {
    return {
      delLoading: false,
      uploadApi:'api/${changeClassName}',
      multipleSelection:[],
      form: {
        <#if columns??>
        <#list columns as column>
        ${column.changeColumnName}: ''<#if column_has_next>,</#if>
        </#list>
        </#if>
      },
      menu:[
        {
          title:'复制新增',
          click:this.copyClick
        },
      ],
    }
  },
  created() {
    this.clientHeight = document.body.clientHeight -200;
    this.$nextTick(() => {
      this.init()
    })
  },
  methods: {
  <#if hasTimestamp>
    parseTime,
  </#if>
    checkPermission,
    <#if isHasDict>
    getDictCaption,
    </#if>
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
        this.$refs['delpopver'].doClose()
        this.dleChangePage()
        this.init()
        this.$notify({
          title: '删除成功',
          type: 'success',
          duration: 2500
        })
      }).catch(err => {
        this.delLoading = false
        this.$refs['delpopver'].doClose()
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
      _this.form = deepClone(data)
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
        downloadFile(result, '${tableRealName}列表', 'xlsx')
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
    },
    rowContextMenu(row){
      this.rightClickRow = row;
    },
    copyClick(){
      const  _this = this.$refs.form;
      _this.form = deepClone(this.rightClickRow);
      this.operate = '新增'
      _this.dialog = true
    }
  }
}
</script>

<style scoped>

</style>
