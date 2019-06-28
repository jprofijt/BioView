/**
 * Scripts that manage the directory trees
 *
 * @author Kim Chau Duong
 * @version 1.0
 */

// Adds folder and lazy true by default and changes fields to match the FancyTree settings
function renameTreeJsonData(response) {
    response = $.map(response, function (n) {
        n.key = n.path;
        delete n.path;
        n.title = n.name;
        delete n.name;
        n.folder = true;
        n.lazy = true;

        return n;
    });

    return response;
}

// Fancy 'Move to' directory tree
$(function () {
    $("#moveFolderTree").fancytree({
        source: [
            { name: "HeadDirectory", path: "HeadDirectory", folder: true, lazy: true }
        ],
        lazyLoad: function(event, data){
            var node = data.node;
            data.result = {
                url: "/api/folder/branch",
                data: {parent: node.key},
                cache: false
            };
        },
        postProcess: function(event, data){
            data.result = renameTreeJsonData(data.response);
        },
        loadChildren: function(event, data) {

            data.node.visit(function(subNode){
                // Loads all lazy/unloaded child nodes and triggers loadChildren recursively
                if( subNode.isUndefined() && subNode.isExpanded() ) {
                    subNode.load();
                }
            });
        }
    });
    $(".fancytree-container").toggleClass("fancytree-connectors");

    $("#moveFolder").submit(function() {
        // Render hidden <input> elements for active and selected nodes
        var directoryArray = [];
        $('.select').each(function(){
            var directory = $(this).siblings('[name = "location"]').val();
            directoryArray.push(directory);
        });
        $('input[name="movedFolders"]').val(directoryArray);

        $("#moveFolderTree").fancytree("getTree").generateFormElements();
    });
});

// Fancy 'Copy to' directory tree
$(function () {
    $("#copyFolderTree").fancytree({
        source: [
            { name: "HeadDirectory", path: "HeadDirectory", folder: true, lazy: true }
        ],
        lazyLoad: function(event, data){
            var node = data.node;
            data.result = {
                url: "/api/folder/branch",
                data: {parent: node.key},
                cache: false
            };
        },
        postProcess: function(event, data){
            data.result = renameTreeJsonData(data.response);
        },
        loadChildren: function(event, data) {

            data.node.visit(function(subNode){
                // Loads all lazy/unloaded child nodes and triggers loadChildren recursively
                if( subNode.isUndefined() && subNode.isExpanded() ) {
                    subNode.load();
                }
            });
        }
    });
    $(".fancytree-container").toggleClass("fancytree-connectors");

    $("#copyFolder").submit(function() {
        // Render hidden <input> elements for active and selected nodes
        var directoryArray = [];
        $('.select').each(function(){
            var directory = $(this).siblings('[name = "location"]').val();
            directoryArray.push(directory);
        });
        $('input[name="copiedFolders"]').val(directoryArray);

        $("#copyFolderTree").fancytree("getTree").generateFormElements();
    });
});

$(function () {
    $("#moveImageTree").fancytree({
        source: [
            { name: "HeadDirectory", path: "HeadDirectory", folder: true, lazy: true }
        ],
        lazyLoad: function(event, data){
            var node = data.node;
            data.result = {
                url: "/api/folder/branch",
                data: {parent: node.key},
                cache: false
            };
        },
        postProcess: function(event, data){
            data.result = renameTreeJsonData(data.response);
        },
        loadChildren: function(event, data) {

            data.node.visit(function(subNode){
                // Loads all lazy/unloaded child nodes and triggers loadChildren recursively
                if( subNode.isUndefined() && subNode.isExpanded() ) {
                    subNode.load();
                }
            });
        }
    });
    $(".fancytree-container").toggleClass("fancytree-connectors");

    $("#moveImage").submit(function() {
        // Render hidden <input> elements for active and selected nodes
        var imagePathArray = [];
        $('.pic-select').each(function(){
            var imagePath = $(this).parent().attr('data-image-path').replace(/\\/g, "/");
            imagePathArray.push(imagePath);
        });
        $('input[name="movedImages"]').val(imagePathArray);

        $("#moveImageTree").fancytree("getTree").generateFormElements();
    });
});

$(function () {
    $("#copyImageTree").fancytree({
        source: [
            { name: "HeadDirectory", path: "HeadDirectory", folder: true, lazy: true }
        ],
        lazyLoad: function(event, data){
            var node = data.node;
            data.result = {
                url: "/api/folder/branch",
                data: {parent: node.key},
                cache: false
            };
        },
        postProcess: function(event, data){
            data.result = renameTreeJsonData(data.response);
        },
        loadChildren: function(event, data) {

            data.node.visit(function(subNode){
                // Loads all lazy/unloaded child nodes and triggers loadChildren recursively
                if( subNode.isUndefined() && subNode.isExpanded() ) {
                    subNode.load();
                }
            });
        }
    });
    $(".fancytree-container").toggleClass("fancytree-connectors");

    $("#copyImage").submit(function() {
        // Render hidden <input> elements for active and selected nodes
        var imagePathArray = [];
        $('.pic-select').each(function(){
            var imagePath = $(this).parent().attr('data-image-path').replace(/\\/g, "/");
            imagePathArray.push(imagePath);
        });
        $('input[name="copiedImages"]').val(imagePathArray);

        $("#copyImageTree").fancytree("getTree").generateFormElements();
        // alert("POST data:\n" + jQuery.param($(this).serializeArray()));
        // // return false to prevent submission of this sample
        // return false;
    });
});

