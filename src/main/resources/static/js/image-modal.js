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
        imgInstance.lockRotation=true;
    }
        canvasAnimation(canvas)
}

function canvasAnimation(canvas){
    // canvas.on('mouse:down', function(opt) {
    //     var evt = opt.e;
    //     if (evt.altKey === true) {
    //         this.isDragging = true;
    //         this.selection = false;
    //         this.lastPosX = evt.clientX;
    //         this.lastPosY = evt.clientY;
    //
    //     }
    // });
    canvas.on('object:moving', function(e) {

        var object = e.target;

        object.setCoords();



        var boundingRect = object.getBoundingRect();
        var zoom = canvas.getZoom();
        var viewportMatrix = canvas.viewportTransform;

        //there is a bug in fabric that causes bounding rects to not be transformed by viewport matrix
        //this code should compensate for the bug for now


        //TODO: viewport veranderen zodat hij niet overal heen schiet als je img aanklikt.
        this.viewportTransform[4] += e.clientX - this.lastPosX;
        this.viewportTransform[5] += e.clientY - this.lastPosY;
        this.requestRenderAll();
        this.lastPosX = e.clientX;
        this.lastPosY = e.clientY;
        boundingRect.top = (boundingRect.top - viewportMatrix[5]) / zoom;
        boundingRect.left = (boundingRect.left - viewportMatrix[4]) / zoom;
        boundingRect.width /= zoom;
        boundingRect.height /= zoom;


        // if object is too big ignore

        if (object.currentHeight * zoom > object.canvas.height * zoom || object.currentWidth * zoom > object.canvas.width * zoom) {
            return;
        }

        var canvasHeight = object.canvas.height / zoom,
            canvasWidth = object.canvas.width / zoom,
            rTop = boundingRect.top + boundingRect.height,
            rLeft = boundingRect.left + boundingRect.width;

        // top-left  corner
        if (rTop < canvasHeight || rLeft < canvasWidth) {
            object.top = Math.max(object.top, canvasHeight - boundingRect.height);
            object.left = Math.max(object.left, canvasWidth - boundingRect.width);
        }


        // bot-right corner
        if (boundingRect.top + boundingRect.height > object.canvas.height || boundingRect.left + boundingRect.width > object.canvas.width) {

            object.top = Math.min(object.top, object.canvas.height - boundingRect.height + object.top - boundingRect.top);
            object.left = Math.min(object.left, object.canvas.width - boundingRect.width + object.left - boundingRect.left);
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
        zoom = zoom + delta/200;
        if (zoom > 15) zoom = 15;
        if (zoom < 0.075) zoom = 0.075;
        canvas.zoomToPoint({ x: opt.e.offsetX, y: opt.e.offsetY }, zoom);
        opt.e.preventDefault();
        opt.e.stopPropagation();
    });




    // canvas.on('mouse:move', function(opt) {
    //     if (this.isDragging) {
    //         var e = opt.e;
    //         this.viewportTransform[4] += e.clientX - this.lastPosX;
    //         this.viewportTransform[5] += e.clientY - this.lastPosY;
    //         this.requestRenderAll();
    //         this.lastPosX = e.clientX;
    //         this.lastPosY = e.clientY;
    //
    //     }});
}
