function loadDynamicModal(id, path){
    $("#myModal"+ id).modal({backdrop: 'static', keyboard: false});
    loadImageIntoCanvas(id, path)
}


function loadImageIntoCanvas(id, path){
    var canvas = document.getElementById("canvas"+ id);
    var context = canvas.getContext("2d");
    var image = new Image();
    image.src = 'files/' + path;
    alert(image.src);
    image.onload = () => {
    context.drawImage(image, 0, 0, image.width, image.height, 0, 0, canvas.width, canvas.height);}
}
