<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>ywDate</title>
    <link rel="stylesheet" href="/css/lj/index.css"/>
    <link rel="stylesheet" href="/js/lj/layui-v2.5.4/layui/css/layui.css" media="all"/>
    <script src="/jquery-3.1.1/jquery-3.1.1.min.js"></script>
    <script src="/js/lj/cookie.js"></script>
</head>

<body>
<div id="Div6">
    <div class="Div4">
        <span id="Span1">选择时间:</span>
        <input type="text" class="start-layui-input" id="test2" placeholder="请选择结束时间"/>
        <form class="layui-form" action="">
            <div class="layui-form-item">
                <div class="layui-inline">
                    <label class="layui-form-label2">服务商名称:</label>
                    <div class="layui-input-inline">
                        <select name="modules" lay-verify="required" lay-search="" id="selectedCompany">
                            <option value="-1">请选择</option>
                            <#list companyList as company>
                                <option value="${company.id}">${company.name}</option>
                            </#list>
                        </select>
                    </div>
                </div>
            </div>
        </form>
        <button type="submit" name="取消订单" id="Buttown1" onclick="cancelOrder()">取消订单</button>
    </div>

    <#--<div class="Div4">
        <span id="Span1">订单号:</span>
        <input type="text" class="start-layui-input" id="orderCode" placeholder="请输入订单号"/>

        <button type="submit" name="取消订单" id="Buttown1" onclick="cancelOrder()">取消订单</button>
    </div>-->
</div>
</body>
<script src="/js/lj/layui-v2.5.4/layui/layui.all.js"></script>
<script src="/js/index.js"></script>
<script src="/js/lj/mouse.js"></script>

<script>
    $(function () {// 初始化内容
        htmlLoad();
    });

    function cancelOrder() {

        var companyId = $('#selectedCompany option:selected').val();

        var endDate = document.getElementById("test2").value;

        if (companyId == -1 || endDate == '') {
            alert("请选择条件")
            return ;
        }

        $.ajax({
            type: "POST",
            url: "/lj/admin/cancelOrderBatch",
            contentType: 'application/x-www-form-urlencoded;charset=utf-8',
            data: {companyId: companyId, endDate: endDate},
            dataType: "json",
            success: function (data) {
                alert(data);

            }
        });
    }


    //时间选择控件
    layui.use('laydate', function () {
        var laydate = layui.laydate;


        laydate.render({
            elem: '#test2', //指定元素
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