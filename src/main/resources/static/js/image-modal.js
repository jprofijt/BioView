function openDynamicModal(id){
    $("#myModal"+ id).modal({backdrop: 'static', keyboard: false});
}

function zoomin(id) {
    var myImg = document.getElementById("image-to-zoom" + id);
    var currWidth = myImg.clientWidth;
    if (currWidth >= 2500) return false;
    else {
        myImg.style.width = (currWidth + 250) + "px";
        myImg.style.height = (currWidth + 250) + "px";
    }
}

function zoomout(id) {
    var myImg = document.getElementById("image-to-zoom" + id);
    var currWidth = myImg.clientWidth;
    // alert(currWidth);
    if (currWidth <= 512 ) return false;
    else {
        myImg.style.width = (currWidth - 250) + "px";
        myImg.style.height = (currWidth - 250) + "px";

    }
}