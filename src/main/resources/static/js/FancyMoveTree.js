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

//Fancy 'Move to' directory Tree
$(function () {
    $("#moveTree").fancytree({
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
            console.log(data.response);
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
        $("#moveTree").fancytree("getTree").generateFormElements();

        alert("POST data:\n" + jQuery.param($(this).serializeArray()));
        // return false to prevent submission of this sample
        return false;
    });

});

