window.onload = function() {
    var LI1 = document.getElementById("LeftItem1");
    var LI2 = document.getElementById("LeftItem2");
    var LI3 = document.getElementById("LeftItem3");
    var Ec1 = document.getElementById("Erc1");
    var Erc2 = document.getElementById("Erc2");
    var Erc3 = document.getElementById("Erc3");
    var As1 = document.getElementById("aspan4");
    var As2 = document.getElementById("aspan5");
    var As3 = document.getElementById("aspan6");
    var A1 = document.getElementById("A1");
    var A2 = document.getElementById("A2");
    var A3 = document.getElementById("A3");

    LI1.onclick = function() {
        LI1.style.backgroundColor = "rgb(78, 84, 101)";
        LI2.style.backgroundColor = "#393D49";
        LI3.style.backgroundColor = "#393D49";
        Erc2.style.display = "none";
        Ec1.style.display = "inline-block";
        Erc3.style.display = "none";
        As2.style.marginLeft = "36px";
        As3.style.marginLeft = "36px";
        As1.style.marginLeft = "25px";
        A1.click();
    }
    LI2.onclick = function() {
        LI2.style.backgroundColor = "#4E5465";
        LI3.style.backgroundColor = "#393D49";
        LI1.style.backgroundColor = "#393D49";
        Ec1.style.display = "none";
        Erc3.style.display = "none";
        As1.style.marginLeft = "36px";
        As3.style.marginLeft = "36px";
        As2.style.marginLeft = "25px";
        Erc2.style.display = "inline-block";
        A2.click();
    }
    LI3.onclick = function() {
        LI3.style.backgroundColor = "#4E5465";
        LI1.style.backgroundColor = "#393D49";
        LI2.style.backgroundColor = "#393D49";
        Ec1.style.display = "none";
        As1.style.marginLeft = "36px";
        As3.style.marginLeft = "25px";
        As2.style.marginLeft = "36px";
        Erc2.style.display = "none";
        Erc3.style.display = "inline-block";
        A3.click();
    }
}