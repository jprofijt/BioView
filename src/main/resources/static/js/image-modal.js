function loadDynamicModal(id, path){
    var modalImage = $(this).attr('id');
    $("#myModal"+ id).modal({backdrop: 'static', keyboard: false});
    loadImageIntoCanvas(id, path)
}


function loadImageIntoCanvas(id, path){
    var canvas;
    if ($("#image-canvas-holder"+id ).find(".canvas-container").length <= 0){
        canvas = new fabric.Canvas('canvas' + id);
        document.getElementById('canvas' + id).fabric = canvas;
        var image = new Image();
        image.src = 'files/' + path;
        canvas.setHeight(window.innerHeight/2);
        canvas.setWidth(window.innerWidth/2);

        image.onload = () => {
            var imgInstance = new fabric.Image(image,{
                left: 0,
                top: 0,
                height: image.height,
                width: image.width,
                hasBorders: false
                // stateful: true
            });
            canvas.add(imgInstance);
            imgInstance.lockRotation=true;
            // imgInstance.lockUniScaling=true;
            // imgInstance.lockScalingY=true;
            // imgInstance.lockScalingX=true;
            imgInstance.hasControls=false;
        }

    }
    else{
        canvas = document.getElementById("canvas" + id).fabric;
    }
    canvasAnimation(canvas)
}

function canvasAnimation(canvas){
    canvas.on('mouse:down', function(opt) {
        var element = opt.e;
        if (element.altKey === true) {
            this.isDragging = true;
            this.selection = false;
            this.lastPosX = element.clientX;
            this.lastPosY = element.clientY;

        }
    });
    canvas.on('mouse:wheel', function(opt) {
        var element = opt.e;
        var delta = -element.deltaY;
        var pointer = canvas.getPointer(element);
        var zoom = canvas.getZoom();
        zoom = zoom + delta/200;

        if (zoom > 15) zoom = 15;
        if (zoom < 0.1) zoom = 0.1;
        canvas.zoomToPoint({ x: element.offsetX, y: element.offsetY }, zoom);
        element.preventDefault();
        element.stopPropagation();
    });
    // canvas.on('object:modified', function (opt) {
    //     var object = opt.target;
    //     var boundingRect = object.getBoundingRect(true);
    //     if (boundingRect.left < 0
    //         || boundingRect.top < 0
    //         || boundingRect.left + boundingRect.width > canvas.getWidth()
    //         || boundingRect.top + boundingRect.height > canvas.getHeight()) {
    //         object.top = object._stateProperties.top;
    //         object.left = object._stateProperties.left;
    //         object.angle = object._stateProperties.angle;
    //         object.scaleX = object._stateProperties.scaleX;
    //         object.scaleY = object._stateProperties.scaleY;
    //         object.setCoords();
    //         object.saveState();
    //     }
    // });
    canvas.on('mouse:up', function() {
        this.isDragging = false;
        this.selection = true;
    });

    canvas.on('mouse:move', function(opt) {
        if (this.isDragging) {
            var element = opt.e;
            this.viewportTransform[4] += element.clientX - this.lastPosX;
            this.viewportTransform[5] += element.clientY - this.lastPosY;
            this.requestRenderAll();
            this.lastPosX = element.clientX;
            this.lastPosY = element.clientY;

        }});

}
//
//
//there is a bug in fabric that causes bounding rects to not be transformed by viewport matrix
//this code should compensate for the bug for now
//
//
// // this.viewportTransform[4] += e.clientX - this.lastPosX;
// // this.viewportTransform[5] += e.clientY - this.lastPosY;
// // this.requestRenderAll();
// // this.lastPosX = e.clientX;
// // this.lastPosY = e.clientY;
// boundingRect.top = (boundingRect.top - viewportMatrix[5]) / zoom;
// boundingRect.left = (boundingRect.left - viewportMatrix[4]) / zoom;
// boundingRect.width /= zoom;
// boundingRect.height /= zoom;
//
//
// // if object is too big ignore
//
// // if (object.currentHeight * zoom > object.canvas.height * zoom || object.currentWidth * zoom > object.canvas.width * zoom) {
// //     return;
// // }
//
// var canvasHeight = object.canvas.height / zoom,
//     canvasWidth = object.canvas.width / zoom,
//     rTop = boundingRect.top + boundingRect.height,
//     rLeft = boundingRect.left + boundingRect.width;
//
// // top-left  corner
// if (rTop < canvasHeight || rLeft < canvasWidth) {
//     object.top = Math.max(object.top, canvasHeight - boundingRect.height);
//     object.left = Math.max(object.left, canvasWidth - boundingRect.width);
// }
//
//
// // bot-right corner
// if (boundingRect.top + boundingRect.height > object.canvas.height || boundingRect.left + boundingRect.width > object.canvas.width) {
//
//     object.top = Math.min(object.top, object.canvas.height - boundingRect.height + object.top - boundingRect.top);
//     object.left = Math.min(object.left, object.canvas.width - boundingRect.width + object.left - boundingRect.left);
// }

function drawPolygons(name, id){
    var canvas;
    if ($("#image-canvas-holder"+id ).find(".canvas-container").length > 0){
        canvas = document.getElementById(name + id).fabric
    }
    var roof = null;
    var roofPoints = [];
    var lines = [];
    var lineCounter = 0;
    var drawingObject = {};
    drawingObject.type = "";
    drawingObject.background = "";
    drawingObject.border = "";

    function Point(x, y) {
        this.x = x;
        this.y = y;
    }


    if (drawingObject.type == "roof") {
        drawingObject.type = "";
        lines.forEach(function(value, index, ar){
            canvas.remove(value);
        });
        //canvas.remove(lines[lineCounter - 1]);
        roof = makeRoof(roofPoints);
        canvas.add(roof);
        canvas.renderAll();
    } else {
        drawingObject.type = "roof"; // roof type
    }

    canvas.on('mouse:down', function (options) {
        if (drawingObject.type == "roof") {
            canvas.selection = false;
            setStartingPoint(options); // set x,y
            roofPoints.push(new Point(x, y));
            var points = [x, y, x, y];
            lines.push(new fabric.Line(points, {
                strokeWidth: 3,
                selectable: false,
                stroke: 'red'
            }).setOriginX(x).setOriginY(y));
            canvas.add(lines[lineCounter]);
            lineCounter++;
            canvas.on('mouse:up', function (options) {
                canvas.selection = true;
            });
        }
    });
    fabric.util.addListener(window,'dblclick', function(){
        drawingObject.type = "";
        lines.forEach(function(value, index, ar){
            canvas.remove(value);
        });
        //canvas.remove(lines[lineCounter - 1]);
        roof = makeRoof(roofPoints);
        canvas.add(roof);
        canvas.renderAll();

        console.log("double click");
        //clear arrays
        roofPoints = [];
        lines = [];
        lineCounter = 0;
        canvas.__eventListeners["mouse:dblclick"] = [];
    });
    canvas.on('mouse:down', function (options) {
        if (drawingObject.type == "roof") {
            canvas.selection = false;
            setStartingPoint(options); // set x,y
            roofPoints.push(new Point(x, y));
            var points = [x, y, x, y];
            lines.push(new fabric.Line(points, {
                strokeWidth: 3,
                selectable: false,
                stroke: 'red'
            }).setOriginX(x).setOriginY(y));
            canvas.add(lines[lineCounter]);
            lineCounter++;
            canvas.on('mouse:up', function (options) {
                canvas.selection = true;
            });
        }
    });
    canvas.on('mouse:move', function (options) {
        if (lines[0] !== null && lines[0] !== undefined && drawingObject.type == "roof") {
            setStartingPoint(options);
            lines[lineCounter - 1].set({
                x2: x,
                y2: y
            });
            canvas.renderAll();
        }
    });

    function setStartingPoint(options) {
        var offset = $(name + id).offset();
        x = options.e.pageX - offset.left;
        y = options.e.pageY - offset.top;
    }

    function makeRoof(roofPoints) {

        var left = findLeftPaddingForRoof(roofPoints);
        var top = findTopPaddingForRoof(roofPoints);
        roofPoints.push(new Point(roofPoints[0].x,roofPoints[0].y));
        var roof = new fabric.Polyline(roofPoints, {
            fill: 'rgba(0,0,0,0)',
            stroke:'#58c'
        });
        roof.set({

            left: left,
            top: top

        });
        return roof;
    }

    function findTopPaddingForRoof(roofPoints) {
        var result = 999999;
        for (var f = 0; f < lineCounter; f++) {
            if (roofPoints[f].y < result) {
                result = roofPoints[f].y;
            }
        }
        return Math.abs(result);
    }

    function findLeftPaddingForRoof(roofPoints) {
        var result = 999999;
        for (var i = 0; i < lineCounter; i++) {
            if (roofPoints[i].x < result) {
                result = roofPoints[i].x;
            }
        }
        return Math.abs(result);
    }

}
