function openDynamicModal(id){
    $("#myModal"+ id).modal({backdrop: 'static', keyboard: false});
}

function zoomin() {
    var myImg = document.getElementById("image-to-zoom");
    var currWidth = myImg.clientWidth;
    if (currWidth == 250000) return false;
    else {
        myImg.style.width = (currWidth + 250) + "px";
    }
}

function zoomout() {
    var myImg = document.getElementById("image-to-zoom");
    var currWidth = myImg.clientWidth;
    if (currWidth == 100000) return false;
    else {
        myImg.style.width = (currWidth - 250) + "px";
    }
}