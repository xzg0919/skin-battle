<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>excel导出</title>
    <script src="/jquery-3.1.1/jquery-3.1.1.min.js"> </script>
    <script src="/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="/bootstrap-3.3.7-dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="/css/index.css">
    <script src="/js/index.js"></script>
</head>
<body>
<form action="http://localhost:9000/admin/index" method="post" enctype="multipart/form-data">
     <input type="file" name="headImg" /></br>
     <input type="hidden"  value="util.uploadImageTwo" name="name"/></br>
      <input type="hidden"  value="1.0" name="version"/></br>
     <input type="hidden"  value="uuidssssssssss" name="nonce" id ="nonce"/></br>
     <input type="hidden"  value="1537846411782" name="timestamp" id = "timestamp"/></br>
     <input type="submit"  value="保存"/>
</form>
<input type="button" value="点击导出企业excel" id="excel" onclick="excelo()" /></br>
<input type="button" value="点击导出终端excel" id="excel" onclick="excel()" /></br>
<input type="button" value="点击导出picc的excel" id="excel" onclick="piccexcel()" /></br>
<input type="button" value="点击导出完成订单excel" id="excel" onclick="orderExcel()" /></br>
</body>
<script type="text/javascript">
    function orderExcel() {
        window.location = "http://localhost:9000/out/excel/outOrderExcel.jhtml?id=2&startTime=&endTime=&memberName=";
    }
    function piccexcel() {
        window.location = "http://localhost:9000/out/excel/outPiccOrderExcel.jhtml?piccCompanyId=1&startTime=&endTime=&memberName=";
    }

    function excel() {
        window.location = "http://localhost:9000/out/excel/outEnterpriseTerminalCodeExcel.jhtml?id=1";
    }


    var postDownLoadFile = function (options) {
        var config = $.extend(true, { method: 'post' }, options);
        var $iframe = $('<iframe id="down-file-iframe" />');
        var $form = $('<form target="down-file-iframe" method="' + config.method + '" />');
        $form.attr('action', config.url);
        for (var key in config.data) {
            $form.append('<input type="hidden" name="' + key + '" value="' + config.data[key] + '" />');
        }
        $iframe.append($form);
        $(document.body).append($iframe);
        $form[0].submit();
        $iframe.remove();
    }

function excelo() {
    postDownLoadFile({
        type: 'POST',
        url: "http://localhost:9000/out/excel/outEnterpriseCodeExcel.jhtml",
        data: {id : "1"},
        success: function(result) {
            console("success")
        },
        dataType: "json"
    });
}
</script>
</html>