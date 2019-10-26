<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <script src="/jquery-3.1.1/jquery-3.1.1.min.js"></script>
    <link rel="stylesheet" type="text/css" href="/css/voucherCodeList.css">
    <title>Document</title>
    <script type="text/javascript">
	$().ready
	( 
		function() 
		{
			$("[id^=make_]").click
			(
			 	  function()
			 	  {
			 	      var target = $(this).attr("target");
			 	      var truthBeTold = window.confirm("券多的话可能贼慢哦!")
					  if (truthBeTold) 
					  {
							$.ajax
							(
								{
									url: "/ali/makeCode",
									type:"GET",
								    data:{'id':target},
									dataType: "text",
									cache: false,
									success: function(message)
									{
										alert(message);
										return false;
									}
								}
							);
					  } 
			 	      return false;
			 	  }
			);
			 
			$("[id^=expo_]").click
			(
			 	  function()
			 	  {
			 	      var target = $(this).attr("target");
			 	      var vname = $("#n_"+target).html();
			 	      //location.href="/ali/expoCode?target="+target+"&vname="+vname;
			 	      
			 	      window.open("/ali/expoCode?id="+target+"&vname="+vname);
			 	  }
			);
			 
			 
			 
		}
	);
	</script>
</head>
<body>
    <div class="content">
        <button class="addButton">新增</button>
        <table class="default-table">  
            <tr>  
                <th>序号</th>  
                <th>名称</th>  
                <th>折扣(面额)</th>  
                <th>最低使用金额</th>  
                <th>数量</th>  
                <th>领取日期</th>  
                <th>有效日期</th>  
                <th>总数量</th>  
                <th>领取数量</th>  
                <th>核销数量</th>  
                <th>操作</th>  
            </tr>  
            <tr>  
                    ${page} 
            </tr>  
            </c:forEach>  
        </table>
    </div>
   
</body>
</html>