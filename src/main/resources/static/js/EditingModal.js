/**
 * this script is for loading and editing images in the editing modal
 *
 * @author Wietse Reitsma
 */



$(document).on('dblclick', '.images li .image-surrounding a', function(){
    let count = $(this).attr('data-image-iter');
    let path = $(this).attr('data-image-path');
    loadDynamicModal(count, path);
});

var roof = null;
var roofPoints = [];
var lines = [];
var lineCounter = 0;
var drawingObject = {};


/**
 * creates the editing modal for the selected image.
 * @param id image id
 * @param path path of the image
 *
 * @author Wietse Reitsma
 */
function loadDynamicModal(id, path){
    var modalImage = $(this).attr('id');
    $("#myModal"+ id).modal({backdrop: 'static', keyboard: false});
    loadImageIntoCanvas(id, path)
}


/**
 * loads the image into the canvas.
 * @param id image id
 * @param path path of the image
 *
 * @author Wietse Reitsma
 */
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
            //fixes bug where cursor is offset on the canvas
            window.canvas.on("after:render", function(){ window.canvas.calcOffset() });
            imgInstance.lockRotation=true;
            // imgInstance.lockUniScaling=true;
            // imgInstance.lockScalingY=true;
            // imgInstance.lockScalingX=true;
            imgInstance.hasControls=false;
        }

    }
    else{
        canvas = document.getElementById("canvas" + id).fabric;
        window.canvas = canvas;
        window.canvas.on("after:render", function(){ window.canvas.calcOffset() });
    }
    canvasAnimation()
}

/**
 * Functions for moving and pan zooming the image
 * @author Wietse Reitsma
 */
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
/**
 * a collection of functions for drawing polygons, by getting coordinates from the cursor on a canvas element.
 *
 * @param id
 *
 * @author Wietse Reitsma
 */
function drawPolygons(id){

    var roiPointsList = [];

    function Point(x, y) {
        this.x = x;
        this.y = y;
    }

    if (drawingObject.type == "roof") {
        drawingObject.type = "";
        lines.forEach(function(value){
            window.canvas.remove(value);
        });
        roof = makeRoof(roofPoints);
        window.canvas.add(roof);
        canvas.renderAll();
    } else {
        drawingObject.type = "roof";
    }
    // creates and fixes a line to the cursor, creates static line between last point and cursor if clicked again.
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
    // finishes the polygon with double click
    fabric.util.addListener(window,'dblclick', function(){
        drawingObject.type = "";
        lines.forEach(function(value){
            window.canvas.remove(value);
        });
        roiPointsList.pop();
        roiPointsList.push(roiPointsList[0]);
        console.log(roiPointsList);
        roiPointsList = [];
        roof = makeRoof(roofPoints);
        window.canvas.add(roof);

        selectAllObjects();
        var activegroup = window.canvas.getActiveGroup();
        var objectsInGroup = activegroup.getObjects();
        activegroup.clone(function(newgroup) {
            window.canvas.discardActiveGroup();
            objectsInGroup.forEach(function (object) {
                window.canvas.remove(object);
            });
            window.canvas.add(newgroup);
            newgroup.hasControls=false;
        });
        window.canvas.renderAll();

        console.log("double click");
        //clear arrays
        roofPoints = [];
        lines = [];
        lineCounter = 0;
    });

    /**
     * groups the region of interest to the image
     */
    function selectAllObjects() {
        var objects = window.canvas.getObjects().map(function(object) {
            return object.set('active', true);
        });
        var group = new fabric.Group(objects, {
            originX: 'center',
            originY: 'center'
        });
        window.canvas._activeObject = null;
        window.canvas.setActiveGroup(group.setCoords()).renderAll();
    }
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
            window.canvas.renderAll();
        }
    });

    /**
     * calculates and sets the startpoint for the region of interest drawing.
     * @param options
     */
    function setStartingPoint(options) {
        var offset = $('#'+'canvas' + id).offset();
        x = options.e.pageX - offset.left;
        y = options.e.pageY - offset.top;
    }

    /**
     * Finishes the polygon by drawing the lines between all the points.
     *
     * @param roofPoints the possitions between clicks
     * @returns {*}
     */

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
            top: top
        });
        return roof;
    }

    /**
     * this function is nessesary for preventing the drawn region of interest to be offset
     * @param roofPoints
     * @returns {number}
     */
    function findTopPaddingForRoof(roofPoints) {
        var result = 999999;
        for (var f = 0; f < lineCounter; f++) {
            if (roofPoints[f].y < result) {
                result = roofPoints[f].y;
            }
        }
        return Math.abs(result);
    }
    /**
     * this function is nessesary for preventing the drawn region of interest to be offset
     * @param roofPoints
     * @returns {number}
     */

    function findLeftPaddingForRoof(roofPoints) {
        var result = 999999;
        for (var i = 0; i < lineCounter; i++) {
            if (roofPoints[i].x < result) {
                result = roofPoints[i].x;
            }
        }
        return Math.abs(result);
    }
    //Need to check the JSON properly
    // var save = $('.save-button');
    //
    // save.on("click", function () {
    //     var EditingId = $(this).parent().attr('id');
    //     var editingRow = $('#editing-row');
    //     var inputs = editingRow.find('input');
    //     var InputData = {
    //         id: id,
    //         pointlist: roiPointsList
    //     };
    //
    //     var url = "http://"+document.location.hostname + ":8081/api/image/roi/add/";
    //     $.ajax({
    //         url: url,
    //         type: "POST",
    //         data: JSON.stringify(InputData),
    //         contentType: "application/json; charset=utf-8",
    //         dataType: "json",
    //         success: function () {
    //             ReloadTable(EditingId);
    //         }
    //     });
    // });
}