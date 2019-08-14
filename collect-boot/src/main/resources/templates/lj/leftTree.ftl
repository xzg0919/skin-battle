<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Left</title>
    <link rel="stylesheet" href="/css/lj/index.css" />
    <link rel="stylesheet" href="/js/lj/layui-v2.5.4/layui/css/layui.css" media="all" />
</head>

<body>
    <div id="Div7">
        <ul class="layui-nav layui-nav-tree" lay-filter="test">
            <li class="layui-nav-item" id="LeftItem1">
                <span class="erc" id="Erc1"></span>
                <span class="span4" id="aspan4">业务数据统计</span>
                <a href="/lj/admin/ywDate" target="right" id="A1"></a>

            </li>
            <li class="layui-nav-item" id="LeftItem2">
                <span class="erc1" id="Erc2"></span>
                <span class="span5" id="aspan5">服务商管理</span>
                <a href="/lj/admin/fwsDate" target="right" id="A2"></a>
            </li>
        </ul>
    </div>
</body>
<script src="/js/lj/leftTree.js"></script>

</html>