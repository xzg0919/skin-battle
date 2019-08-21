<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <title>中台订单数据查询后台系统</title>
    <script src="/jquery-3.1.1/jquery-3.1.1.min.js"> </script>
    <script>
        function htmlLoad() {
            if(getCookie("name_login") == undefined || "test1" != getCookie("name_login")){
                window.location.href="/lj/admin/login";
            }else{
                console.log(getCookie("name_login"));
            }
        }
        function getCookie(cname) {
            var name = cname + "=";
            var decodedCookie = decodeURIComponent(document.cookie);
            var ca = decodedCookie.split(';');
            for (var i = 0; i < ca.length; i++) {
                var c = ca[i];
                while (c.charAt(0) == ' ') {
                    c = c.substring(1);
                }
                if (c.indexOf(name) == 0) {
                    return c.substring(name.length, c.length);
                }
            }
            return "";
        }
    </script>
</head>

<frameset rows="81px,*" onload="htmlLoad()" frameborder="0">
    <frame src="/lj/admin/homeHead" />
    <frameset cols="200px,*" frameborder="0">
        <frame src="/lj/admin/leftTree" />
        <frame name="right" src="/lj/admin/ywDate" />
    </frameset>
</frameset>
</html>