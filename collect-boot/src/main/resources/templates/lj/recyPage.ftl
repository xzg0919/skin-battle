<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>回收人员平台</title>
  <script src="/jquery-3.1.1/jquery-3.1.1.min.js"> </script>
  <script src="/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
  <link rel="stylesheet" href="/bootstrap-3.3.7-dist/css/bootstrap.min.css">
  <link rel="stylesheet" href="/css/index.css">
  <script src="/js/index.js"></script>
</head>
<style type="text/css">
 #save{
   position: absolute;
   left: 30%;
 }

</style>
<body>
<section id="warp">
  <!-- 头部 -->
  <header class="head">
    <h3 class="title">回收人员额度管理平台</h3>

    <div class="searchBox">
      <form class="bs-example bs-example-form  clearfix"   role="form">
        <div class="input-group input-group-sm fl">
          <span class="input-group-addon">姓名：</span>
          <input style="width:200px" type="text" class="form-control" placeholder="" id="recyclerName" value="${recyclerName}">
        </div>
        <div class="input-group input-group-sm fl">
          <span class="input-group-addon">手机号：</span>
          <input style="width:200px" type="text" class="form-control" placeholder="" id="tel" value="${tel}">
        </div>

        <button type="button" class="btn btn-primary" onclick="search()">搜索</button>
        <!-- <div class="input-group input-group-sm fl">
            <span class="input-group-addon">时间选择：</span>
            <input style="width:200px" type="date" class="form-control" placeholder="">
        </div> -->
      </form>
    </div>
  </header>
  <!-- 列表 -->
  <div class="content">
    <table class="table table-hover">
      <thead>
      <tr>
        <th>姓名</th>
        <th>手机号</th>
        <th>分配额度</th>
        <th>当日已使用</th>
        <th class="greenC">操作</th>
      </tr>
      </thead>
      <tbody>
      <#list recyclersList as recycler>
        <tr>

          <td>${recycler.name}</td>
          <td>${recycler.tel}</td>
          <td>${recycler.allowTimes?c}</td>
          <td>${recycler.companyCount?c}</td>
          <td><a id="info" onclick="editRecy(${recycler.id?c})">编辑</a></td>
          <td style="display: none" id="${recycler.id?c}">${recycler.id?c}</td>
        </tr>
      </#list>


      </tbody>
    </table>

  </div>
  <!-- 弹出层 -->
  <div class="tBox" style="display:none">
    <!-- 详情的时候 -->
    <section id="details">
      <h4 class="title">额度详情 <span id="close1"  >关闭</span></h4>
      <input type="hidden" class="recyclerId" value=""/>
      <div class="tcontent clearfix">
        <ul class="shopDetails userDetails fl">
          <li class="clearfix">
            <strong class="fl" >姓名：</strong>
            <span class="fl name"></span>
          </li>
          <li class="clearfix">
            <strong class="fl">手机号：</strong>
            <span class="fl tel"></span>
          </li>
          <li class="clearfix">
            <strong class="fl">额度：</strong>
            <input class="fl allowTimes" value="0"/>
          </li>
          <li class="clearfix">
            <strong class="fl">当日已下单数：</strong>
            <span class="fl companyCount">0</span>
          </li>
        </ul>
      </div>
      <button type="button" class="btn btn-primary" id="save">保存</button>
    </section>
  </div>
</section>

</body>
<script type="text/javascript">
 $(function () {
   $('#save').on('click',function(){
     let allowTimes = $(".allowTimes").val();
     let recyclerId = $(".recyclerId").val();
     $.ajax({
       type: "POST",
       url: "/lj/admin/editRcy",
       contentType: 'application/x-www-form-urlencoded;charset=utf-8',
       data: {recyclerId: recyclerId, allowTimes: allowTimes},
       dataType: "text",
       success: function (data) {
         if(data=='success'){
           alert("修改成功！！");
           let a = $("#"+recyclerId).siblings();
           a.eq(2).html(allowTimes);
         }else{
           alert("修改失败！！");
         }
       },
       error: function () {
         alert("修改失败！！");
       }

     });

     $('.tBox').css({"display":"none"});
     $('#details').css({"display":"none"});
   });
 });
 function editRecy(v){
   $('.tBox').css({"display":"block"});
   $('#details').css({"display":"block"});
   // 获取列表中的值并复写到弹窗里
   let a = $("#"+v).siblings();
   $(".name").html(a.eq(0).html());
   $(".tel").html(a.eq(1).html());
   $(".allowTimes").val(a.eq(2).html());
   $(".companyCount").html(a.eq(3).html());
   $(".recyclerId").val(v);
 }
 function search(){
   // 获取列表中的值并复写到弹窗里
  let recyclerName = $("#recyclerName").val();
  let recyclerTel = $("#tel").val();
   window.location = "/lj/admin/recyPage?recyclerTel="+recyclerTel+"&recyclerName="+recyclerName
 }

</script>

</html>