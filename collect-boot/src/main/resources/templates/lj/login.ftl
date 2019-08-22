<!DOCTYPE html>
<html>

<head>
    <title>中台订单数据查询后台系统</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1">


    <!--可无视-->
    <link rel="stylesheet" href="/css/lj/bootstrap.min.css">

    <!--主要样式-->
    <link type="text/css" href="/css/lj/style.css" rel="stylesheet" />
    <script src="/js/lj/login.js"></script>

</head>

<body>

    <div class="container" align="center">
        <div class="col-md-6" style="margin-top: 20%;">
            <div class="inset">
                <form name="login" id="login" method="post" action="/lj/admin/home"
                    onSubmit="return CheckLogin(document.login);">
                    <input type="hidden" name="enews" value="login">
                    <div>
                        <h2>后台管理系统</h2>
                        <span style="text-align: left;text-indent: 0.4em;"><label>用户名</label></span>
                        <span><input type="text" name="username" class="textbox"></span>
                    </div>
                    <div>
                        <span style="text-align: left;text-indent: 0.4em;"><label>密码</label></span>
                        <span><input name="password" type="password" class="password"></span>
                    </div>
                    <div class="sign">
                        <input type="reset" class="submit" value="重置" />
                        <input type="submit" value="登录" class="submit" />
                    </div>
                </form>
            </div>
        </div>
    </div>

</body>

</html>