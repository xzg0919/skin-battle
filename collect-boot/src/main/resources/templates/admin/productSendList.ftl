<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>商品兑换平台</title>
    <script src="/jquery-3.1.1/jquery-3.1.1.min.js"> </script>
    <script src="/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="/bootstrap-3.3.7-dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="/css/index.css">
    <script src="/js/index.js"></script>
</head>
<body>
    <section id="warp">
        <!-- 头部 -->
        <header class="head">
                <h3 class="title">商品兑换平台</h3>
                <ul class="nav nav-pills">
                    <li role="presentation" class="activea"><a href="#" onclick = "init()">待发货订单</a></li>
                    <li role="presentation"><a href="#" >已完成订单</a></li>
                </ul>

                <div class="searchBox">
                    <form class="bs-example bs-example-form  clearfix"   role="form">
                        <div class="input-group input-group-sm fl">
                            <span class="input-group-addon">商品名称：</span>
                            <input style="width:200px" type="text" class="form-control" placeholder="" id="productName">
                        </div>
                        <div class="input-group input-group-sm fl">
                            <span class="input-group-addon">手机号：</span>
                            <input style="width:200px" type="text" class="form-control" placeholder="" id="mobile">
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
                    <th>下单时间</th>
                    <th>商品名称</th>
                    <th>用户姓名</th>
                    <th>手机号</th>
                    <th>地址</th>
                    <th>快递公司</th>
                    <th class="greenC">单号</th>
                    </tr>
                </thead>
                <tbody>
                   <#list ProductOrderList as order>
                    <tr>
                    <td>${order.createDate?string("yyyy-MM-dd HH:mm:ss")}</td>
                    <td>${order.productName}</td>
                    <td>${order.userName}</td>
                    <td>${order.mobile}</td>
                    <td id="address" >${order.address}</td>
                    <td> ${order.orderCompany}</td>
                    <td class="xq">${order.orderNum} </td>
                    </tr>
                    </#list>
                 
                   
                </tbody>
            </table>
            <!-- 分页 
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
            </div>-->
        </div>
        <!-- 弹出层 -->
        <div class="tBox">
            <!-- 详情的时候 -->
            <section id="details">
                <h4 class="title">订单详情 <span id="close1"  >关闭</span></h4>
                <div class="tcontent clearfix">
                    <ul class="shopDetails  fl">
                        <li class="clearfix">
                            <strong class="fl">商品名称：</strong>
                            <span class="fl">撒旦大神</span>
                        </li>
                        <li class="clearfix">
                            <strong class="fl">兑换能量值：</strong>
                            <span class="fl">100</span>
                        </li>
                        <li class="clearfix">
                            <strong class="fl">商品描述：</strong>
                            <span class="fl">撒旦大神了空间阿斯利康大家啊；昆仑世界了；大；苏联空军的；拉萨孔家店；卢卡斯吉多啊</span>
                        </li>
                        <li class="clearfix">
                            <strong class="fl">商品图片：</strong>
                            <img  class="fl shopImg" src="http://pic.58pic.com/58pic/16/19/64/13R58PICpEr_1024.jpg" alt="">
                        </li>
                    </ul>
                    <ul class="shopDetails userDetails fl">
                            <li class="clearfix">
                                <strong class="fl">下单时间：</strong>
                                <span class="fl">2018-8-24</span>
                            </li>
                            <li class="clearfix">
                                <strong class="fl">姓名：</strong>
                                <span class="fl">妹妹</span>
                            </li>
                            <li class="clearfix">
                                <strong class="fl">手机号：</strong>
                                <span class="fl">123456789012</span>
                            </li>
                            <li class="clearfix">
                                <strong class="fl">收货地址：</strong>
                                <span class="fl">了肯定还是尽快恢复撒电力华发大厦</span>
                            </li>
                            <li class="clearfix">
                                <strong class="fl">发货时间：</strong>
                                <span class="fl">2018-8-24</span>
                            </li>
                            <li class="clearfix">
                                <strong class="fl">快递公司：</strong>
                                <span class="fl">顺丰快递</span>
                            </li>
                            <li class="clearfix">
                                <strong class="fl">快递单号：</strong>
                                <span class="fl">655656565651232313</span>
                            </li>
                        </ul>
                </div>
            </section>
        </div>
    </section>
</body>

<script type="text/javascript">
//跳转到未发货列表
function init(){
	window.location = "/product/order/getProduct/orderLists"
}
//条件模糊查询事件
function search(){
	var mobile = document.getElementById("mobile").value
	var productName = document.getElementById("productName").value
	window.location = "/product/order/sendProductOrderList?mobile="+mobile+"&productName="+productName
}
</script>
</html>