<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" type="text/css" href="/css/voucherCodeAdd.css">
    <title>Document</title>
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
                <span>文本框 :</span>
                <input id="name" type="text" name="name"/>
            </label>

            <label>
                <span>文本域 :</span>
                <textarea id="message" name="message"></textarea>
            </label>

            <label >
                <span>单选 :</span>
                <label  class="radioLabel"><input name="radio" type="radio" value="" />苹果</label>
                <label  class="radioLabel"><input name="radio" type="radio" value="" />桃子 </label>
                <label  class="radioLabel"><input name="radio" type="radio" value="" />香蕉 </label>
                <label  class="radioLabel"><input name="radio" type="radio" value="" />梨  </label>
                <label  class="radioLabel"><input name="radio" type="radio" value="" />其它</label>
            </label>
            <label >
                <span>多选 :</span>
                <label  class="checkboxLabel"><input name="checkbox" type="checkbox" value="" />苹果</label>
                <label  class="checkboxLabel"><input name="checkbox" type="checkbox" value="" />桃子 </label>
                <label  class="checkboxLabel"><input name="checkbox" type="checkbox" value="" />香蕉 </label>
                <label  class="checkboxLabel"><input name="checkbox" type="checkbox" value="" />梨 </label>
            </label>


            <label>
                <span>下拉框 :</span>
                <select name="selection">
                    <option value="11111111">11111111</option>
                    <option value="22222222">22222222</option>
                </select>
            </label>
            <label>
                <span>&nbsp;</span>
                <input type="button" class="button" value="提交" />
            </label>
        </form>
    </div>
</body>


</html>