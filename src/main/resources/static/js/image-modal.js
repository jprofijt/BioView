$(document).on('dblclick', '.images li .image-surrounding a', function(){
    let count = $(this).siblings('.image-iter').val();
    let path = $(this).siblings('.image-path').val();
    loadDynamicModal(count, path);
});

var roof = null;
var roofPoints = [];
var lines = [];
var lineCounter = 0;
var drawingObject = {};
drawingObject.type = "";
drawingObject.border = "";



function loadDynamicModal(id, path){
    var modalImage = $(this).attr('id');
    $("#myModal"+ id).modal({backdrop: 'static', keyboard: false});
    loadImageIntoCanvas(id, path)
}


function loadImageIntoCanvas(id, path){
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
            window.canvas = canvas;
            imgInstance.lockRotation=true;
            // imgInstance.lockUniScaling=true;
            // imgInstance.lockScalingY=true;
            // imgInstance.lockScalingX=true;
            imgInstance.hasControls=false;
        }

    }
    else{
        canvas = document.getElementById("canvas" + id).fabric;
        window.canvas = canvas
    }
    canvasAnimation()
}

function canvasAnimation(){
    window.canvas.on('mouse:down', function(opt) {
        var element = opt.e;
        if (element.altKey === true) {
            this.isDragging = true;
            this.selection = false;
            this.lastPosX = element.clientX;
            this.lastPosY = element.clientY;

        }
    });
    window.canvas.on('mouse:wheel', function(opt) {
        var delta = opt.e.deltaY;
        var pointer = window.canvas.getPointer(opt.e);
        var zoom = window.canvas.getZoom();
        zoom = zoom + delta/200;
        if (zoom > 20) zoom = 20;
        if (zoom < 0.01) zoom = 0.01;
        window.canvas.zoomToPoint({ x: opt.e.offsetX, y: opt.e.offsetY }, zoom);
        opt.e.preventDefault();
        opt.e.stopPropagation();
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
    window.canvas.on('mouse:up', function() {
        this.isDragging = false;
        this.selection = true;
    });

    window.canvas.on('mouse:move', function(opt) {
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
    var roiPointsList = [];

    function Point(x, y) {
        this.x = x;
        this.y = y;
    }

    if (drawingObject.type == "roof") {
        drawingObject.type = "";
        lines.forEach(function(value, index, ar){
            window.canvas.remove(value);
        });
        //canvas.remove(lines[lineCounter - 1]);
        roof = makeRoof(roofPoints);
        window.canvas.add(roof);
        canvas.renderAll();
    } else {
        drawingObject.type = "roof"; // roof type
    }
    window.canvas.on('mouse:down', function (options) {
        if (drawingObject.type == "roof") {
            window.canvas.selection = false;
            setStartingPoint(options); // set x,y
            roofPoints.push(new Point(x, y));
            roiPointsList.push([x,y]);
            var points = [x, y, x, y];
            lines.push(new fabric.Line(points, {
                strokeWidth: 3,
                selectable: false,
                stroke: 'red'
            }).setOriginX(x).setOriginY(y));
            window.canvas.add(lines[lineCounter]);
            lineCounter++;
            window.canvas.on('mouse:up', function (options) {
                window.canvas.selection = true;
            });
        }
    });
    fabric.util.addListener(window,'dblclick', function(){
        drawingObject.type = "";
        lines.forEach(function(value, index, ar){
            window.canvas.remove(value);
        });
        //canvas.remove(lines[lineCounter - 1]);
        roiPointsList.pop();
        roiPointsList.push(roiPointsList[0]);
        console.log(roiPointsList);
        roiPointsList = [];
        roof = makeRoof(roofPoints);
        window.canvas.add(roof);
        window.canvas.renderAll();
        // var objs = [];
        // // get all the objects into an array
        // objs = canvas._objects.filter(function(obj){
        //     return obj;
        // });
        //
        // //group all the objects
        // var alltogetherObj = new fabric.Group(objs);
        //
        //
        // //clear previous objects
        // canvas._objects.forEach(function(obj){
        //     obj.remove();
        // });
        //
        // canvas.add(alltogetherObj);
        // alltogether.setCoords();
        // canvas.renderAll();

        console.log("double click");
        //clear arrays
        roofPoints = [];
        lines = [];
        lineCounter = 0;
    });
    window.canvas.on('mouse:down', function (options) {
        if (drawingObject.type == "roof") {
            window.canvas.selection = false;
            setStartingPoint(options); // set x,y
            roofPoints.push(new Point(x, y));
            var points = [x, y, x, y];
            lines.push(new fabric.Line(points, {
                strokeWidth: 3,
                selectable: false,
                stroke: 'red'
            }).setOriginX(x).setOriginY(y));
            window.canvas.add(lines[lineCounter]);
            lineCounter++;
            window.canvas.on('mouse:up', function (options) {
                window.canvas.selection = true;
            });
        }
    });
    window.canvas.on('mouse:move', function (options) {
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
        var offset = $('#'+name + id).offset();
        console.log(offset);
        console.log($('canvas1').offset());
        x = options.e.pageX - offset.left;
        y = options.e.pageY - offset.top;
    }

    function makeRoof(roofPoints) {

        var left = findLeftPaddingForRoof(roofPoints);
        var top = findTopPaddingForRoof(roofPoints);
        roofPoints.push(new Point(roofPoints[0].x,roofPoints[0].y));
        var roof = new fabric.Polyline(roofPoints, {
            fill: 'rgba(0,0,0,0)',
            stroke:'rgba(252, 185, 65, 1)',
            strokeWidth: 2
        });
        roof.set({

            left: left,
            top: top,

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



















//     var roof = null;
//     var roofPoints = [];
//     var lines = [];
//     var lineCounter = 0;
//     var drawingObject = {};
//     drawingObject.type = "";
//     drawingObject.background = "";
//     drawingObject.border = "";

//     function Point(x, y) {
//         this.x = x;
//         this.y = y;
//     }


//     $("#poly").click(function () {
//         if (drawingObject.type == "roof") {
//             drawingObject.type = "";
//             lines.forEach(function(value, index, ar){
//                  canvas.remove(value);
//             });
//             //canvas.remove(lines[lineCounter - 1]);
//             roof = makeRoof(roofPoints);
//             canvas.add(roof);
//             canvas.renderAll();
//         } else {
//             drawingObject.type = "roof"; // roof type
//         }
//     });


//     // canvas Drawing

//     fabric.util.addListener(window,'dblclick', function(){
//             drawingObject.type = "";
//             lines.forEach(function(value, index, ar){
//                  canvas.remove(value);
//             });
//             //canvas.remove(lines[lineCounter - 1]);
//             roof = makeRoof(roofPoints);
//             canvas.add(roof);
//             canvas.renderAll();

//         console.log("double click");
//         //clear arrays
//          roofPoints = [];
//          lines = [];
//          lineCounter = 0;

//     });

//     canvas.on('mouse:down', function (options) {
//         if (drawingObject.type == "roof") {
//             canvas.selection = false;
//             setStartingPoint(options); // set x,y
//             roofPoints.push(new Point(x, y));
//             var points = [x, y, x, y];
//             lines.push(new fabric.Line(points, {
//                 strokeWidth: 3,
//                 selectable: false,
//                 stroke: 'red'
//             }).setOriginX(x).setOriginY(y));
//             canvas.add(lines[lineCounter]);
//             lineCounter++;
//             canvas.on('mouse:up', function (options) {
//                 canvas.selection = true;
//             });
//         }
//     });
//     canvas.on('mouse:move', function (options) {
//         if (lines[0] !== null && lines[0] !== undefined && drawingObject.type == "roof") {
//             setStartingPoint(options);
//             lines[lineCounter - 1].set({
//                 x2: x,
//                 y2: y
//             });
//             canvas.renderAll();
//         }
//     });

//     function setStartingPoint(options) {
//         var offset = $('#canvas-tools').offset();
//         x = options.e.pageX - offset.left;
//         y = options.e.pageY - offset.top;
//     }

//     function makeRoof(roofPoints) {

//         var left = findLeftPaddingForRoof(roofPoints);
//         var top = findTopPaddingForRoof(roofPoints);
//         roofPoints.push(new Point(roofPoints[0].x,roofPoints[0].y))
//         var roof = new fabric.Polyline(roofPoints, {
//         fill: 'rgba(0,0,0,0)',
//         stroke:'#58c'
//         });
//         roof.set({

//             left: left,
//             top: top,

//         });


//         return roof;
//     }

//     function findTopPaddingForRoof(roofPoints) {
//         var result = 999999;
//         for (var f = 0; f < lineCounter; f++) {
//             if (roofPoints[f].y < result) {
//                 result = roofPoints[f].y;
//             }
//         }
//         return Math.abs(result);
//     }

//     function findLeftPaddingForRoof(roofPoints) {
//         var result = 999999;
//         for (var i = 0; i < lineCounter; i++) {
//             if (roofPoints[i].x < result) {
//                 result = roofPoints[i].x;
//             }
//         }
//         return Math.abs(result);
//     }
