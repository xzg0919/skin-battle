<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>ywDate</title>
  <link rel="stylesheet" href="/css/lj/index.css" />
  <link rel="stylesheet" href="/js/lj/layui-v2.5.4/layui/css/layui.css" media="all" />
  <script src="/jquery-3.1.1/jquery-3.1.1.min.js"> </script>
</head>

<body>
<div id="Div6">
  <div class="Div4">
    <span id="Span1">选择时间:</span>
    <input type="text" class="start-layui-input" id="test1" placeholder="请选择开始时间" />
    <input type="text" class="start-layui-input" id="test2" placeholder="请选择结束时间" />
    <form class="layui-form" action="">
      <div class="layui-form-item">
        <div class="layui-inline">
          <label class="layui-form-label1">城市:</label>
          <div class="layui-input-inline">
            <select name="modules" lay-verify="required" lay-search="" id="selectedCity"  lay-filter="selectedCity">
              <option value="-1">全部</option>
              <#list cityList as city>
                <option value="${city.id}">${city.areaName}</option>
              </#list>
            </select>
          </div>
        </div>
      </div>
    </form>
    <form class="layui-form" action="">
      <div class="layui-form-item">
        <div class="layui-inline">
          <label class="layui-form-label2">行政区:</label>
          <div class="layui-input-inline">
            <select name="modules" lay-verify="required" lay-search="" id="selectedArea"  lay-filter="selectedArea">
            </select>
          </div>
        </div>
      </div>
    </form>
    <#--<div class="layui-input-inline">-->
    <#--<select id="secetedTest" ></select>-->
    <#--</div>-->

    <form class="layui-form" action="">
      <div class="layui-form-item">
        <div class="layui-inline">
          <label class="layui-form-label2">街道:</label>
          <div class="layui-input-inline">
            <select name="modules" lay-verify="required" lay-search="" id="selectedStreet">

            </select>
          </div>
        </div>
      </div>
    </form>
    <form class="layui-form" action="">
      <div class="layui-form-item">
        <div class="layui-inline">
          <label class="layui-form-label2">服务商名称:</label>
          <div class="layui-input-inline">
            <select name="modules" lay-verify="required" lay-search="" id="selectedCompany">
              <option value="-1">全部</option>
              <#list companyList as company>
                <option value="${company.id}">${company.name}</option>
              </#list>
            </select>
          </div>
        </div>
      </div>
    </form>
    <button type="submit" name="查询" id="Buttown1" onclick="selectDetail()">查询</button>
    <button type="submit" name="重置" id="Buttown2">重置</button>
  </div>

  <div class="Div5">
    <span class="span2" id="">生活五废服务能力</span>
    <br />
    <span class="span2" id="">平均派单时长：</span>
    <span class="span3" id="avgTosendDate">0</span>
    <br />
    <span class="span2" id="">最长派单时长：</span>
    <span class="span3" id="maxTosendDate">0</span>
    <br />
    <span class="span2" id="">平均接单时长：</span>
    <span class="span3" id="avgAlreadyDate">0</span>
    <br />
    <span class="span2" id="">最长接单时长：</span>
    <span class="span3" id="maxAlreadyDate">0</span>
    <br />
    <span class="span2" id="">平均订单完成时长：</span>
    <span class="span3" id="avgCompleteDate">0</span>
    <br />
    <span class="span2" id="">最长订单完成时长：</span>
    <span class="span3" id="maxCompleteDate">0</span>
  </div>
  <div class="Div5">
    <span class="span2" id="">家电服务能力</span>
    <br />
    <span class="span2" id="">平均派单时长：</span>
    <span class="span3" id="avgTosendDateDq">0</span>
    <br />
    <span class="span2" id="">最长派单时长：</span>
    <span class="span3" id="maxTosendDateDq">0</span>
    <br />
    <span class="span2" id="">平均接单时长：</span>
    <span class="span3" id="avgAlreadyDateDq">0</span>
    <br />
    <span class="span2" id="">最长接单时长：</span>
    <span class="span3" id="maxAlreadyDateDq">0</span>
    <br />
    <span class="span2" id="">平均订单完成时长：</span>
    <span class="span3" id="avgCompleteDateDq">0</span>
    <br />
    <span class="span2" id="">最长订单完成时长：</span>
    <span class="span3" id="maxCompleteDateDq">0</span>
  </div>
  <div class="Div5">
    <span class="span2" id="">全部订单总数：</span>
    <span class="span3" id="orderCount">0</span>
    <br />
    <span class="span2" id="">全部订单取消率：</span>
    <span class="span3" id="cancelOrderCount">0</span>
    <br />
    <span class="span2" id="">全部订单驳回率：</span>
    <span class="span3" id="rejectedOrderCount">0</span>
    <br />
    <span class="span2" id="">全部订单完成率：</span>
    <span class="span3" id="completeOrderCount">0</span>
  </div>
</div>
</body>
<script src="/js/lj/layui-v2.5.4/layui/layui.all.js"></script>
<script src="/js/index.js"></script>
<script src="/js/lj/mouse.js"></script>
<script type="text/javascript">

  function selectDetail(){
    var cityId = $('#selectedCity option:selected') .val();
    var areaId = $('#selectedArea option:selected') .val();
    if ("undefined"==areaId+""){
      areaId = "-1";
    }
    var streetId = $('#selectedStreet option:selected') .val();
    if ("undefined"==streetId+""){
      streetId = "-1";
    }
    var companyId = $('#selectedCompany option:selected') .val();
    var startDate = document.getElementById("test1").value;
    var endDate = document.getElementById("test2").value;
    $.ajax({
      type: "POST",
      url: "/lj/admin/getFwsDateDetail",
      contentType: 'application/x-www-form-urlencoded;charset=utf-8',
      data: {cityId:cityId,areaId:areaId,streetId:streetId,companyId:companyId,startDate:startDate,endDate:endDate},
      dataType: "json",
      success: function(data){
        $("#avgTosendDate").html(data.avgTosendDate+"  分钟")
        $("#avgAlreadyDate").html(data.avgAlreadyDate+"  分钟")
        $("#avgCompleteDate").html(data.avgCompleteDate+"  分钟")
        $("#maxTosendDate").html(data.maxTosendDate+"  分钟")
        $("#maxAlreadyDate").html(data.maxAlreadyDate+"  分钟")
        $("#maxCompleteDate").html(data.maxCompleteDate+"  分钟")
        $("#avgTosendDateDq").html(data.avgTosendDateDq+"  分钟")
        $("#avgAlreadyDateDq").html(data.avgAlreadyDateDq+"  分钟")
        $("#avgCompleteDateDq").html(data.avgCompleteDateDq+"  分钟")
        $("#maxTosendDateDq").html(data.maxTosendDateDq+"  分钟")
        $("#maxAlreadyDateDq").html(data.maxAlreadyDateDq+"  分钟")
        $("#maxCompleteDateDq").html(data.maxCompleteDateDq+"  分钟")
        $("#orderCount").html(data.orderCount)
        $("#cancelOrderCount").html(data.cancelOrderCount)
        $("#rejectedOrderCount").html(data.rejectedOrderCount)
        $("#completeOrderCount").html(data.completeOrderCount)
      }
    })
  }

  layui.use(['layer', 'jquery', 'form'], function () {
    var layer = layui.layer,
            $ = layui.jquery,
            form = layui.form;

    form.on('select(selectedCity)', function(data){
      var cityId = data.value;
      $.ajax({
        type: "POST",
        url: "/lj/admin/getAreaList",
        contentType: 'application/x-www-form-urlencoded;charset=utf-8',
        data: {cityId:cityId},
        dataType: "json",
        success: function(data){
          var options="<option value='-1'>全部</option>";
          for (var i = 0; i < data.length; i++) {
            options += '<option value="' + data[i].id + '">' + data[i].areaName + '</option>';
          }
          $("#selectedArea").html(options);
          var areaId = $('#selectedArea option:selected') .val();
          $.ajax({
            type: "POST",
            url: "/lj/admin/getStreetList",
            contentType: 'application/x-www-form-urlencoded;charset=utf-8',
            data: {areaId:areaId},
            dataType: "json",
            success: function(data){
              var options="<option value='-1'>全部</option>";
              for (var i = 0; i < data.length; i++) {
                options += '<option value="' + data[i].id + '">' + data[i].areaName + '</option>';
              }
              $("#selectedStreet").html(options);
              form.render("select");
            }
          });
        }
      });
    });

    form.on('select(selectedArea)', function(data){
      var areaId =data.value;
      $.ajax({
        type: "POST",
        url: "/lj/admin/getStreetList",
        contentType: 'application/x-www-form-urlencoded;charset=utf-8',
        data: {areaId:areaId},
        dataType: "json",
        success: function(data){
          var options="<option value='-1'>全部</option>";
          for (var i = 0; i < data.length; i++) {
            options += '<option value="' + data[i].id + '">' + data[i].areaName + '</option>';
          }
          $("#selectedStreet").html(options);
          form.render("select");
        }
      });
    })
  });



</script>
<script>
  //时间选择控件
  layui.use('laydate', function() {
    var laydate = layui.laydate;

    //执行一个laydate实例
    laydate.render({
      elem: '#test1', //指定元素
    });

    laydate.render({
      elem: '#test2', //指定元素
    });

    laydate.render({
      elem: '#test3', //指定元素
    });

    laydate.render({
      elem: '#test4', //指定元素
    });
    //年选择器
    laydate.render({
      elem: '#test',
      type: 'year' //month/date/time/datetime
    });

    //范围
    laydate.render({
      elem: '#test',
      type: 'year', //month/  /time/datetime
      range: true //或 range: '~' 来自定义分割字符
    });

    //格式
    laydate.render({
      elem: '#test',
      format: 'yyyy年MM月dd日' //可任意组合
    });

  });
</script>

</html>