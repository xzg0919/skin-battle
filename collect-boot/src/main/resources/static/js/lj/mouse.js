window.onload = function() {
    var bt1 = document.getElementById("Buttown1");
    var bt2 = document.getElementById("Buttown2");
    bt1.onmousemove = function() {
        bt1.style.backgroundColor = '#64B5F6';
    }
    bt1.onmouseout = function() {
        bt1.style.backgroundColor = "#409eff";
    }
    bt2.onmousemove = function() {
        bt2.style.color = "#1890ff";
        bt2.style.borderColor = "#1890ff";
    }
    bt2.onmouseout = function() {
        bt2.style.color = "rgba(0, 0, 0, .65)";
        bt2.style.borderColor = "#d9d9d9";
    }
}