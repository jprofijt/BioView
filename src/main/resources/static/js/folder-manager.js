// Adds icon to folder titles
$(document).ready(function() {
    $("#folders li").each(function() {
        var getType = $(this).attr("data-file-icon");
        if (getType == "folder") {
            $(this)
                .children("b")
                .prepend('<i class="fas fa-folder"></i>');
        }
    });

});

// Submits on double click
$(document).on('dblclick', '.folder-manager ul li', function(e) {
    $(this).addClass('folder-active');
    $(this).children('form').submit();
});


// Adds select class to selected folder(s)
$(document).on("click", "[data-file-icon]", function(e) {
    if (e.ctrlKey) {
        $(this).addClass("select");
    } else {
        $(".select").removeClass("select");
        $(this)
            .addClass("select")
            .siblings()
            .removeClass("select");
    }
});

// Deselects when clicking elsewhere
$(document).on("click dblclick", function() {
    $("[data-file-icon]")
        .removeClass("select");
});

$(document).on("click", "[data-file-icon]", function(e) {
    e.stopPropagation();
});


/*---Context Menu ---*/

// $(document).on("contextmenu", "[data-file-icon]:not(.show-up)", function(e) {
//     var off = $(this).offset();
//     var topPos = e.pageY;
//     var leftPos = e.pageX;
//     $(".append-option-box").remove();
//     $(this)
//         .addClass("context-visible")
//         .addClass("select");
//     $(this).append(
//         '<div class="append-option-box" style="top:' +
//         topPos +
//         "px;left:" +
//         leftPos +
//         'px;"><div class="inner-contenxt-box"><div data-open="data-move">Open</div><div data-function="data-copy">Copy</div> <div data-function="data-move">Move</div> <div data-function="data-rename">Rename</div> <div data-function="data-delete">Delete</div> <div class="">Share</div> <div data-function="data-properties">Properties</div></div></div>'
//     );
//     $(".append-option-box>div>div:has(div)").addClass("has-sub-context");
//     if ($(this).attr("data-file-icon") != "folder") {
//         $(".append-option-box .inner-contenxt-box").append(
//             '<div data-function="data-copy-path">Copy Path</div>'
//         );
//         $(".append-option-box .inner-contenxt-box").append(
//             '<div data-function="data-copy-secure-path">Copy Secure Path</div>'
//         );
//     }
//     return false;
// });
$(document).on("contextmenu", ".folder-manager", function(e) {
    // var off = $(this).offset();
    var top = e.pageY;
    var left = e.pageX;
    // $(".append-option-box").remove();
    // $(this).append('<div class="append-option-box" id="append-option-box">\n' +
    //     '    <div class="inner-context-box">\n' +
    //     '        <div data-function="new-folder">New Folder</div>\n' +
    //     '        <div class="" data-function="new-date-folder">New Folder (Date)</div>\n' +
    //     '        <div data-function="folder-properties">Properties</div>\n' +
    //     '    </div>\n' +
    //     '</div>'
    //     );
    $('.append-option-box').css("visibility", "hidden");

    $('.append-option-box').css({"visibility" : "visible", "top": top + "px", "left": left+"px"});
    // $(".append-option-box>div>div:has(div)").addClass("has-sub-context");
    // $(".has-sub-context").append('<i class="fas fa-chevron-right"></i>');
    return false;
});
$(document).on("click contextmenu dblclick", function() {
    $("[data-file-icon]")
        // .removeClass("context-visible")
        .removeClass("select renaming");
    $('.append-option-box').css("visibility", "hidden");
    // removeUnwanted();
    // $(".name").attr("contenteditable", false);
});
$(document).on("click contextmenu", ".append-option-box", function(e) {
    e.stopPropagation();
    $("[data-file-icon]")
        // .removeClass("context-visible")
        .removeClass("select");
    $('.append-option-box').css("visibility", "hidden");
});
$(document).on("click contextmenu", "[data-file-icon]", function(e) {
    e.stopPropagation();
});
