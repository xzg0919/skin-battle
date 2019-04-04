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
<form action="http://localhost:9090/ali/api" method="post" enctype="multipart/form-data">
     <input type="file" name="headImg" /></br>
     <input type="hidden"  value="util.uploadImageTwo" name="name"/></br>
      <input type="hidden"  value="1.0" name="version"/></br>
     <input type="hidden"  value="uuidssssssssss" name="nonce" id ="nonce"/></br>
     <input type="hidden"  value="1537846411782" name="timestamp" id = "timestamp"/></br>
     <input type="submit"  value="保存"/>
</form>
</body>
<script type="text/javascript">
$(document).ready(
	function() 
		{
			document.getElementById("nonce").value = new Date().getTime();
			document.getElementById("timestamp").value = new Date().getTime();
		}
)
</script>
</html>