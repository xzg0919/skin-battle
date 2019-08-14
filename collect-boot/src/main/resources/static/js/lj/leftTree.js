window.onload = function() {
    var LI1 = document.getElementById("LeftItem1");
    var LI2 = document.getElementById("LeftItem2");
    var Ec1 = document.getElementById("Erc1");
    var Erc2 = document.getElementById("Erc2");
    var As1 = document.getElementById("aspan4");
    var As2 = document.getElementById("aspan5");
    var A1 = document.getElementById("A1");
    var A2 = document.getElementById("A2");

    LI1.onclick = function() {
        LI1.style.backgroundColor = "rgb(78, 84, 101)";
        LI2.style.backgroundColor = "#393D49";
        Erc2.style.display = "none";
        Ec1.style.display = "block";
        Ec1.style.display = "inline-block";
        As2.style.marginLeft = "36px";
        As1.style.marginLeft = "25px";
        A1.click();
    }
    LI2.onclick = function() {
        LI2.style.backgroundColor = "#4E5465";
        LI1.style.backgroundColor = "#393D49";
        Ec1.style.display = "none";
        As1.style.marginLeft = "36px";
        As2.style.marginLeft = "25px";
        Erc2.style.display = "block";
        Erc2.style.display = "inline-block";
        A2.click();
    }
}