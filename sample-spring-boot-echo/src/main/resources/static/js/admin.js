var elt = $("#tb_suggestion");
var formSearch = $("#formSearch");
var url = elt.data("url");
var getTagUrl = formSearch.data("url");
var delUrl = elt.data("del");
var getUrl = elt.data("get");
var height = elt.parent().height() - elt.siblings(".panel").height();
var g_result = [];
function displayTag(result,value){
    var str = "";
    $.each(value.split(','),function(i,v){
         var val = v;
         $.each(result,function(j,k){
            if(k.value === v){
                val = k.text;
                return false;
            }
         });
         if(i===0){
             str = val;
         }else{
            str += "," + val;
         }
    });
    return str;
}

function getQueryParam(){
    return {
       tag:$("#tag_select").val(),
       createdAt_start:$("#createdAt_start").val(),
       createdAt_end:$("#createdAt_end").val()
    }
}
function doQuery(){
    elt.bootstrapTable('refresh', {
        silent: true,
        url:url,
        query:getQueryParam()
    });
}

$.get(getTagUrl, function(result){
    g_result = result;
    var selectData = $.map(result,function(item){
        return {
            id:item.value,
            text:item.text
        }
    });
    selectData.splice(0,0,{id:"",text:"---请选择---"});
    $("#tag_select").select2({
        data:selectData
    });
    $('.form_datetime').datetimepicker({
         format: 'YYYY-MM-DD HH:mm',
         locale:  'zh-cn'
    });

    $("#createdAt_start").on("dp.change", function (e) {
        $('#createdAt_end').data("DateTimePicker").minDate(e.date);
    });
    $("#createdAt_end").on("dp.change", function (e) {
        $('#createdAt_start').data("DateTimePicker").maxDate(e.date);
    });

    elt.bootstrapTable({ // 对应table标签的id
          height: height,
          url: url, // 获取表格数据的url
          cache: false, // 设置为 false 禁用 AJAX 数据缓存， 默认为true
          striped: true,  //表格显示条纹，默认为false
          pagination: true, // 在表格底部显示分页组件，默认false
          pageList: [5, 10, 20], // 设置页面可以显示的数据条数
          pageSize: 5, // 页面数据条数
          pageNumber: 1, // 首页页码
          sidePagination: 'server', // 设置为服务器端分页
          queryParams: function (params) { // 请求服务器数据时发送的参数，可以在这里添加额外的查询参数，返回false则终止请求
              params = $.extend(true, params, getQueryParam());
              return params;
          },
          sortName: 'id', // 要排序的字段
          sortOrder: 'desc', // 排序规则
          columns: [
              {
                  checkbox: true, // 显示一个勾选框
                  align: 'center' // 居中显示
              }, {
                  field: 'title', // 返回json数据中的name
                  title: '标题', // 表格表头显示文字
                  align: 'center', // 左右居中
                  valign: 'middle', // 上下居中
                  formatter: function (value, row, index){
                     return '<a href="javascript:;" onclick="showContent(\'' + row.id + '\')">'+ value + '</a>';
                  }
              }, {
                  field: 'tag',
                  title: '分类',
                  align: 'center',
                  valign: 'middle',
                  formatter: function (value, row, index){
                     if(value === null || "" === value){
                        return "";
                     }
                     return displayTag(result,value);
                  }
              }, {
                  field: 'content',
                  title: '内容',
                  align: 'center',
                  valign: 'middle',
                  formatter: function (value, row, index){ // 单元格格式化函数
                      value = value || "";
                      if(value.length > 15){
                          return value.substring(0,15) + "...";
                      }
                      return value;
                  }
              },{
                  field: 'createdAt',
                  title: '提交时间',
                  align: 'center',
                  valign: 'middle'
              }, {
                  title: "操作",
                  align: 'center',
                  valign: 'middle',
                  width: 160, // 定义列的宽度，单位为像素px
                  formatter: function (value, row, index) {
                      return '<button class="btn btn-primary btn-sm" onclick="del(\'' + row.id + '\')">删除</button>';
                  }
              }
          ],
          onLoadSuccess: function(){  //加载成功时执行
                console.info("加载成功");
          },
          onLoadError: function(){  //加载失败时执行
                console.info("加载数据失败");
          }
    });

    $("#btn_query").on("click",function(){
        doQuery();
    });

     $("#btn_rest").on("click",function(){
         $("select,input",formSearch).val("").trigger("change");
         doQuery();
     });

    $("#btn_excel").on("click",function(){
        var delUrl = $(this).data("url");
        var data = getQueryParam();
        data["offset"] = 0;
        data["limit"] = 100000;
        delUrl = delUrl + "?" + $.param(data);
        window.open(delUrl);
    });
})

function del(id){
    $.ajax({
        url: delUrl+"?id="+ id,
        dataType:'json',
        success: function(data){
             if(data.code === 1){
               toastr.success(data.data, '系统提示', {timeOut: 5000});
             }else{
               toastr.error(data.data, '系统提示');
             }
             doQuery();
        },
        error:function(XmlHttpRequest, textStatus, errorThrown){
          var json = XmlHttpRequest.responseJSON;
          toastr.error(json.message, '系统提示');
          doQuery();
        }
    });
}
function showContent(id){
      $.ajax({
          url: getUrl + "?id=" + id,
          dataType:'json',
          success: function(data){
               if(data.code === 1){
                    var title = data.data.title;
                    var tag = displayTag(g_result,data.data.tag);
                    var content = data.data.content;
                    $("#title").html(title);
                    $("#tag").html("分类：【"+tag+"】");
                    $("#content").html(content);
                    $("#showContent").modal("show");
               }else{
                    toastr.error(data.data, '系统提示');
                    doQuery();
               }
          },
          error:function(XmlHttpRequest, textStatus, errorThrown){
               var json = XmlHttpRequest.responseJSON;
               toastr.error(json.message, '系统提示');
               doQuery();
          }
      });
}
