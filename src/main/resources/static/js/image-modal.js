function loadDynamicModal(id, path){
    var modalImage = $(this).attr('id');
    alert(modalImage);
    $("#myModal"+ id).modal({backdrop: 'static', keyboard: false});
    loadImageIntoCanvas(id, path)
}


function loadImageIntoCanvas(id, path){
    var canvas = new fabric.Canvas("canvas"+id);
    canvas.setHeight(window.innerHeight/2);
    canvas.setWidth(window.innerWidth/2);
    var image = new Image();
    image.src = 'files/' + path;
    image.onload = () => {
        var imgInstance = new fabric.Image(image,{
            left: 0,
            top: 0,
            height: image.height,
            width: image.width
        });
        canvas.add(imgInstance);
    }

        canvasAnimation(canvas)
}

function canvasAnimation(canvas){
    canvas.on('mouse:down', function(opt) {
        var evt = opt.e;
        if (evt.altKey === true) {
            this.isDragging = true;
            this.selection = false;
            this.lastPosX = evt.clientX;
            this.lastPosY = evt.clientY;
        }
    });
    canvas.on('mouse:move', function(opt) {
        if (this.isDragging) {
            var e = opt.e;
            this.viewportTransform[4] += e.clientX - this.lastPosX;
            this.viewportTransform[5] += e.clientY - this.lastPosY;
            this.requestRenderAll();
            this.lastPosX = e.clientX;
            this.lastPosY = e.clientY;
        }
    });
    canvas.on('mouse:up', function() {
        this.isDragging = false;
        this.selection = true;
    });
    canvas.on('mouse:wheel', function(opt) {
        var delta = -opt.e.deltaY;
        var pointer = canvas.getPointer(opt.e);
        var zoom = canvas.getZoom();
        zoom = zoom + delta/500;
        if (zoom > 15) zoom = 15;
        if (zoom < 0.075) zoom = 0.075;
        canvas.zoomToPoint({ x: opt.e.offsetX, y: opt.e.offsetY }, zoom);
        opt.e.preventDefault();
        opt.e.stopPropagation();
    });

}
