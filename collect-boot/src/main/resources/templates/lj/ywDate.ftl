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
    <script src="/js/lj/cookie.js"></script>
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
            <span class="span2" id="">当天当前时间的新增注册会员数量：</span
            >
            <span class="span3" id="memberCountToDay">0</span>
            <br />
            <span class="span2" id="">截至当天当前时间的平台注册总用户数量：</span
            >
            <span class="span3" id="memberCount">0</span>
            <br />
            <span class="span2" id="">截至当天当前时间的平台总订单数（如勾选区域，按区域条件适用）：</span
            >
            <span class="span3" id="orderCount">0</span>
            <br />
            <span class="span2" id="">截至当前时间的家电订单量（含未派单含在途不含取消驳回）：</span
            >
            <span class="span3" id="digitalCount">0</span>
            <br />
            <span class="span2" id="">截至当前时间的大件垃圾订单量（含未派单含在途不含取消驳回）：</span
            >
            <span class="span3" id="bigCount">0</span>
            <br />
            <span class="span2" id="">截至当前时间的五废订单量（含未派单含在途不含取消驳回）：</span
            >
            <span class="span3" id="houseCount">0</span>
            <br />
            <span class="span2" id="">截至当前时间的五公斤订单量（含未派单含在途不含取消驳回）：</span
            >
            <span class="span3" id="fiveKgCount">0</span>
            <br />
            <span class="span2" id="">截至当前时间的五废订单下单选择10倍积分的订单总量（含未派单含在途不含取消驳回）：</span
            >
            <span class="span3" id="greenOrderCount">0</span>
            <br />
        </div>
        <div class="Div5">
            <span class="span2" id="">截至当天当前时间的五废开通街道数量：</span>
            <span class="span3" id="communityCount">0</span>
            <br />
            <span class="span2" id="">截至当天当前时间的五废服务商回收人员数量：</span
            >
            <span class="span3" id="recyclersCount">0</span>
            <br />
            <span class="span2" id="">截至当天当前时间的未派订单总量：</span>
            <span class="span3" id="initOrderCount">0</span>
            <br />
            <span class="span2" id="">截止当天当前时间的已派但未完成订单总量：</span
            >
            <span class="span3" id="tosendOrderCount">0</span>
            <br />
        </div>
        <div class="Div5">
            <span class="span2" id="">截至当前时间的家电订单总量（含未派单含在途不含取消驳回）：</span
            >
            <span class="span3" id="digitalCountByLj">0</span>
            <br />
            <span class="span2" id="">截至当前时间的大件垃圾订单总量（含未派单含在途不含取消驳回）：</span
            >
            <span class="span3" id="bigCountByLj">0</span>
            <br />
            <span class="span2" id="">截至当前时间的五废订单总量（含未派单含在途不含取消驳回）：</span
            >
            <span class="span3" id="houseCountByLj">0</span>
            <br />
            <span class="span2" id="">截至到当前时间的五废订单下单选择10倍积分的订单总量（含未派单含在途不含取消驳回）：</span
            >
            <span class="span3" id="greenOrderCountByLj">0</span>
            <br />
            <span class="span2" id="">截至当前时间的五公斤订单总量（含未派单含在途不含取消驳回）：</span
            >
            <span class="span3" id="fiveKgCountByLj">0</span>
            <br />
            <span class="span2" id="">截至当前时间的大件垃圾平均每单支付金额：</span
            >
            <span class="span3" id="greenBigPaymentOrderPrice">0</span>
            <br />

        </div>
        <div class="Div5" id="categoryNumDiv">
        </div>
        <div class="Div5" id="categoryHouseAsCash">
        </div>
        <div class="Div5" id="categoryHouseAsGreen">
        </div>
    </div>
</body>
<script src="/js/lj/layui-v2.5.4/layui/layui.all.js"></script>
<script src="/js/index.js"></script>
<script src="/js/lj/mouse.js"></script>
<script type="text/javascript">
    $(function() {// 初始化内容
        htmlLoad();
    });
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
            url: "/lj/admin/getYwDateDetail",
            contentType: 'application/x-www-form-urlencoded;charset=utf-8',
            data: {cityId:cityId,areaId:areaId,streetId:streetId,companyId:companyId,startDate:startDate,endDate:endDate},
            dataType: "json",
            success: function(data){
                $("#memberCount").html(data.memberCount)
                $("#orderCount").html(data.orderCount)
                $("#digitalCount").html(data.digitalCount)
                $("#bigCount").html(data.bigCount)
                $("#houseCount").html(data.houseCount)
                $("#fiveKgCount").html(data.fiveKgCount)
                $("#greenOrderCount").html(data.greenOrderCount)
                $("#memberCountToDay").html(data.memberCountToDay)
                $("#communityCount").html(data.communityCount)
                $("#recyclersCount").html(data.recyclersCount)
                $("#initOrderCount").html(data.initOrderCount)
                $("#tosendOrderCount").html(data.tosendOrderCount)
                $("#digitalCountByLj").html(data.digitalCountByLj)
                $("#bigCountByLj").html(data.bigCountByLj)
                $("#houseCountByLj").html(data.houseCountByLj)
                $("#fiveKgCountByLj").html(data.fiveKgCountByLj)
                $("#greenOrderCountByLj").html(data.greenOrderCountByLj)
                $("#greenBigPaymentOrderPrice").html(data.greenBigPaymentOrderPrice)
                var categoryNumDiv = "<span class=\"span2\" id=\"\">家电大件数量：</span> <br />";
                var categoryHouseAsCash = "<span class=\"span2\" id=\"\">要钱五废重量(kg)：</span> <br />"
                var categoryHouseAsGreen = "<span class=\"span2\" id=\"\">不要钱五废重量(kg)：</span> <br />";
                if ( data.orderCategoryByLj != null){
                    var  date = data.orderCategoryByLj;
                    for (var i = 0; i < date.length; i++) {
                        categoryNumDiv += '<span class="span2" id="">' + date[i].categoryName + '：</span>'+'<span class="span3" id="">'+date[i].amount+'</span><br />';
                    }
                }else {
                    categoryNumDiv += "<span class=\"span2\" id=\"\">电视机（台）：</span>\n" +
                        "            <span class=\"span3\" id=\"\">0</span>\n" +
                        "            <br />\n" +
                        "            <span class=\"span2\" id=\"\">电冰箱（台）：</span>\n" +
                        "            <span class=\"span3\" id=\"\">0</span>\n" +
                        "            <br />\n" +
                        "            <span class=\"span2\" id=\"\">洗衣机（台）：</span>\n" +
                        "            <span class=\"span3\" id=\"\">0</span>\n" +
                        "            <br />\n" +
                        "            <span class=\"span2\" id=\"\">电冰箱（台）：</span>\n" +
                        "            <span class=\"span3\" id=\"\">0</span>\n" +
                        "            <br />\n" +
                        "            <span class=\"span2\" id=\"\">电脑（台）：</span>\n" +
                        "            <span class=\"span3\" id=\"\">0</span>\n" +
                        "            <br />\n" +
                        "            <span class=\"span2\" id=\"\">空调（台）：</span>\n" +
                        "            <span class=\"span3\" id=\"\">0</span>\n" +
                        "            <br />\n" +
                        "            <span class=\"span2\" id=\"\">废纸（公斤）：</span>\n" +
                        "            <span class=\"span3\" id=\"\">0</span>\n" +
                        "            <br />\n" +
                        "            <span class=\"span2\" id=\"\">废塑料（公斤）：</span>\n" +
                        "            <span class=\"span3\" id=\"\">0</span>\n" +
                        "            <br />\n" +
                        "            <span class=\"span2\" id=\"\">废金属（公斤）：</span>\n" +
                        "            <span class=\"span3\" id=\"\">0</span>\n" +
                        "            <br />\n" +
                        "            <span class=\"span2\" id=\"\">废玻璃（公斤）：</span>\n" +
                        "            <span class=\"span3\" id=\"\">0</span>\n" +
                        "            <br />\n" +
                        "            <span class=\"span2\" id=\"\">废纺（公斤）：</span>\n" +
                        "            <span class=\"span3\" id=\"\">0</span>\n" +
                        "            <br />\n" +
                        "            <span class=\"span2\" id=\"\">柜子（个）：</span>\n" +
                        "            <span class=\"span3\" id=\"\">0</span>\n" +
                        "            <br />\n" +
                        "            <span class=\"span2\" id=\"\">桌子（个）：</span>\n" +
                        "            <span class=\"span3\" id=\"\">0</span>\n" +
                        "            <br />\n" +
                        "            <span class=\"span2\" id=\"\">床（个）：</span>\n" +
                        "            <span class=\"span3\" id=\"\">0</span>\n" +
                        "            <br />\n" +
                        "            <span class=\"span2\" id=\"\">床垫（个）：</span>\n" +
                        "            <span class=\"span3\" id=\"\">0</span>\n" +
                        "            <br />\n" +
                        "            <span class=\"span2\" id=\"\">茶几（个）：</span>\n" +
                        "            <span class=\"span3\" id=\"\">0</span>\n" +
                        "            <br />\n" +
                        "            <span class=\"span2\" id=\"\">健身器材（个）：</span>\n" +
                        "            <span class=\"span3\" id=\"\">0</span>\n" +
                        "            <br />"
                }
                if ( data.houseOrderCategoryByLjAsCash != null){
                    var  date = data.houseOrderCategoryByLjAsCash;
                    for (var i = 0; i < date.length; i++) {
                        categoryHouseAsCash += '<span class="span2" id="">' + date[i].categoryName + '：</span>'+'<span class="span3" id="">'+date[i].amount+'</span><br />';
                    }
                }else {
                    categoryHouseAsCash +=  "            <span class=\"span2\" id=\"\">废纸（公斤）：</span>\n" +
                        "            <span class=\"span3\" id=\"\">0</span>\n" +
                        "            <br />\n" +
                        "            <span class=\"span2\" id=\"\">废塑料（公斤）：</span>\n" +
                        "            <span class=\"span3\" id=\"\">0</span>\n" +
                        "            <br />\n" +
                        "            <span class=\"span2\" id=\"\">废金属（公斤）：</span>\n" +
                        "            <span class=\"span3\" id=\"\">0</span>\n" +
                        "            <br />\n" +
                        "            <span class=\"span2\" id=\"\">废玻璃（公斤）：</span>\n" +
                        "            <span class=\"span3\" id=\"\">0</span>\n" +
                        "            <br />\n" +
                        "            <span class=\"span2\" id=\"\">废纺（公斤）：</span>\n" +
                        "            <span class=\"span3\" id=\"\">0</span>\n" +
                        "            <br />"
                }
                if ( data.houseOrderCategoryByLjAsGreen != null){
                    var  date = data.houseOrderCategoryByLjAsGreen;
                    for (var i = 0; i < date.length; i++) {
                        categoryHouseAsGreen += '<span class="span2" id="">' + date[i].categoryName + '：</span>'+'<span class="span3" id="">'+date[i].amount+'</span><br />';
                    }
                }else {
                    categoryHouseAsGreen +=  "            <span class=\"span2\" id=\"\">废纸（公斤）：</span>\n" +
                        "            <span class=\"span3\" id=\"\">0</span>\n" +
                        "            <br />\n" +
                        "            <span class=\"span2\" id=\"\">废塑料（公斤）：</span>\n" +
                        "            <span class=\"span3\" id=\"\">0</span>\n" +
                        "            <br />\n" +
                        "            <span class=\"span2\" id=\"\">废金属（公斤）：</span>\n" +
                        "            <span class=\"span3\" id=\"\">0</span>\n" +
                        "            <br />\n" +
                        "            <span class=\"span2\" id=\"\">废玻璃（公斤）：</span>\n" +
                        "            <span class=\"span3\" id=\"\">0</span>\n" +
                        "            <br />\n" +
                        "            <span class=\"span2\" id=\"\">废纺（公斤）：</span>\n" +
                        "            <span class=\"span3\" id=\"\">0</span>\n" +
                        "            <br />"
                }
                $("#categoryNumDiv").html(categoryNumDiv)
                $("#categoryHouseAsCash").html(categoryHouseAsCash)
                $("#categoryHouseAsGreen").html(categoryHouseAsGreen)
            }
        });

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