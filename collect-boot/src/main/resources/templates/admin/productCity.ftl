<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>商品修改平台</title>
    <script src="/jquery-3.1.1/jquery-3.1.1.min.js"> </script>
    <script src="/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="/bootstrap-3.3.7-dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="/css/index.css">
    <script src="/js/index.js"></script>
</head>
<style>
.pc_shadow_bg{width:100%;height:100%;background:rgba(0,0,0,0.5);position:fixed;top:0;left:0;display:none;}
.pc_shadow{width:700px;position:fixed;top:50%;left:50%;transform:translate(-50%,-50%);background:#fff;display:none;}
.pc_shadow textarea{width:90%;height:300px;outline:0;margin:50px auto;display:block;}
.pc_shadow .button{width:220px;margin:50px auto;overflow:hidden;}
.pc_shadow .btn_confirm, .pc_shadow .btn_cancel{cursor:pointer;width:80px;height:34px;line-height:34px;text-align:center;border:1px rgba(169,169,169) solid;float:left;}
.pc_shadow .btn_cancel{margin-left:30px;}
.pc_proinput{width:84px;height:28px;}
.title{position:relative;}
.btn-group.lllll{position:absolute;top:-3px;right:0;}
.btn-group.lllll .pc-button{background:#e6e6e6;width:100px!important;outline:0!important;}
.btn-group.lllll .dropdown-toggle{background:#ccc;}
</style>
<body>
    <section id="warp">
        <!-- 头部 -->
        <header class="head">
                <h3 class="title">商品信息修改平台
                <div  class="btn-group  lllll">
                            <button type="button" class="btn  names btn-default pc-button" id = "cityName">${cityName}</button>
                            <button type="button" class="btn btn-default dropdown-toggle" 
                                data-toggle="dropdown">
                                <span class="caret"></span>
                            </button>
                            <ul class="dropdown-menu" role="menu">
                              <#list cityList as CityType>
                             	   <li><a href="#" onclick="city('${CityType.nameCN}')" >${CityType.nameCN}</a></li>
                              </#list>
                            </ul>
                     </div>
                </h3>
               
                <div class="searchBox">
                    <form class="bs-example bs-example-form  clearfix"   role="form">
                        <div class="input-group input-group-sm fl">
                            <span class="input-group-addon">商品名称：</span>
                            <input style="width:200px;z-index:-1;" type="text" class="form-control" placeholder="" id="productName">
                        </div>
                        <div class="input-group input-group-sm fl">
                            <span class="input-group-addon">手机号：</span>
                            <input style="width:200px;z-index:-1;" type="text" class="form-control" placeholder="" id="mobile">
                        </div>

                        <button type="button" class="btn btn-primary" onclick="search()">搜索</button>
                        <button type="button" class="btn btn-primary">重置</button>
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
                    <th>活动开始时间</th>
                    <th>商品优惠名称</th>
                    <th>商品名称</th>
                    <th>兑换所需积分</th>
                    <th>适用门店</th>
                    <th>所属城市Id</th>
                    <th class="greenC">领取数量</th>
                    </tr>
                </thead>
                <tbody>
                  <#list productList as product>
                    <tr>
                    <td>${product.pickStartDate}</td>
                    <td>${product.name}</td>
                    <td>${product.brand}</td>
                    <td><input type="test"/ class="pc_proinput" value="${product.bindingPoint}" onblur = "updatePoint('${product.id}',this.value)" ></td>
                    <td class="xq"  >${product.address} <span  class="greenC" onclick="updateShopName('${product.id}','${product.address}')">修改</span></td>
                    <td> ${product.districtsId}</td>
                    <td class="xq">${product.bindingQuantity} </td>
                    </tr>
                  </#list>
                </tbody>
            </table>
            <div class="pc_shadow_bg"></div>
            <div class="pc_shadow">
            	<input id="productId" value="" type="hidden"/>
            	<textarea id="shopName"></textarea>
            	<div class="button">
            		<div class="btn_confirm" id="submits">确定</div>
            		<div class="btn_cancel">取消</div>
            	</div>
            </div>
            <div class="page">
                <ul class="pagination">
                    <li><a href="#">&laquo;</a></li>
                    <li><a href="#">1</a></li>
                    <li><a href="#">2</a></li>
                    <li><a href="#">3</a></li>
                    <li><a href="#">4</a></li>
                    <li><a href="#">5</a></li>
                    <li><a href="#">&raquo;</a></li>
                </ul>
            </div>
        </div>
    </section>
</body>
<script type="text/javascript">
//修改适用门店名称
function updateShopName(id,shopName){
	document.getElementById("productId").value = id;
	document.getElementById("shopName").value = shopName;
	  $(".pc_shadow_bg,.pc_shadow").show();
}
//提交修改的使用门店内容
$("#submits").click(function(){
	var id = document.getElementById("productId").value;
	var shopName = document.getElementById("shopName").value;
	window.location = "/product/order/updateProductShopName?id="+id+"&shopName="+shopName;
});
//获取选中的城市的商品列表
function city(cityName){
	document.getElementById("cityName").value=cityName;
	document.getElementById("cityName").innerHTML=cityName;
	window.location = "/product/order/getProductList?cityName="+cityName;
}
//修改兑换所需积分
function updatePoint(id,point){
	$.ajax({
	    url: "/product/order/updateProductPoint", 
	    type:"GET",
	    dataType:"json",
	    data:{'id':id,'point':point},
	    success: function(data){
	    }
	});
}
	$(".btn_cancel").click(function(){
	  $(".pc_shadow_bg,.pc_shadow").hide();
	});
	
</script>
</html>