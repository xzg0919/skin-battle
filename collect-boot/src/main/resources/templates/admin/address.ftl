<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" type="text/css" href="/css/voucherCodeAdd.css">
    <script src="/jquery-3.1.1/jquery-3.1.1.min.js"></script>
    <title>Document</title>
    <script type="text/javascript">
	$().ready
	( 
		function() 
		{
			$("#ok").click
			(
			 	  function()
			 	  {
			 	  	    var target = $("#name").val();
			 	  	    if(target == "")
			 	  	    {
			 	  	    	return false;
			 	  	    }
			 	        $.ajax
						(
							{
								url: "/ali/getAddress",
								type:"GET",
							    data:{'id':target},
								dataType: "text",
								cache: false,
								success: function(message)
								{
									$("#show").val(message);
									 $("#name").val("");
									return false;
								}
							}
						);
			 	  }
			);
			 
			
			 
			 
			 
		}
	);
	</script>
</head>
<body>
    <div>
        <!-- <form action="" class="formContent">
          <div class="formItem">
              <span class="itemName">文本框：</span>
              <input type="text">
          </div>
          <div class="formItem">
            <span class="itemName">文本域：</span>
            <textarea name="" id="" cols="30" rows="10"></textarea>
          </div>
          <div class="formItem">
            <span class="itemName">下拉框：</span>
            <select name=""> 
                <option value="0">苹果</option> 
                <option value="1">桃子</option> 
            </select> 
          </div>
          <div class="formItem">
            <span class="itemName">单选：</span>
            <label><input name="Fruit" type="radio" value="" />苹果 </label> 
            <label><input name="Fruit" type="radio" value="" />桃子 </label> 
            <label><input name="Fruit" type="radio" value="" />香蕉 </label> 
            <label><input name="Fruit" type="radio" value="" />梨 </label> 
            <label><input name="Fruit" type="radio" value="" />其它 </label> 
          </div>
          <div class="formItem">
            <span class="itemName">多选：</span>
            <label><input name="Fruit" type="checkbox" value="" />苹果 </label> 
            <label><input name="Fruit" type="checkbox" value="" />桃子 </label> 
            <label><input name="Fruit" type="checkbox" value="" />香蕉 </label> 
            <label><input name="Fruit" type="checkbox" value="" />梨 </label> 
          </div>
        </form> -->
        <form action="" method="post" class="formContent">
            <label>
                <span>市名:</span>
                <input id="name" type="text" name="name"/>
            </label>
            <label>
                <span>省名 :</span>
                 <input id="show" type="text" name="show"/>
            </label>
	            <label>
	            <span>&nbsp;</span>
            	<input type="button" class="button" value="提交" id="ok"/>
                
                
            </label>
        </form>
    </div>
</body>


</html>