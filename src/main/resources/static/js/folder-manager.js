// Adds icon to folder titles
$(document).ready(function() {
    $("#folders li").each(function() {
        var getType = $(this).attr("data-file-icon");
        if (getType == "folder") {
            $(this)
                .children("b")
                .prepend('<i class="fas fa-folder"></i>');
            $(this).find("form").children("b").prepend('<i class="fas fa-folder"></i>');
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
        $('.append-option-box').css("visibility", "hidden");
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

$(document).on("contextmenu", ".folder-manager", function(e) {
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
    // $('.append-option-box').css("visibility", "hidden");

    $('.append-option-box').css({"visibility" : "visible", "top": top + "px", "left": left+"px"});
    return false;
});

$(document).on("click contextmenu dblclick", function() {
    $("[data-file-icon]")
        .removeClass("select renaming");
    $('.append-option-box').css("visibility", "hidden");
});
$(document).on("click contextmenu", ".append-option-box", function(e) {
    e.stopPropagation();
    $("[data-file-icon]")
        .removeClass("select");
    $('.append-option-box').css("visibility", "hidden");
});
$(document).on("click contextmenu", "[data-file-icon]", function(e) {
    e.stopPropagation();
});

function createDateFolder() {
    $('.date-folder-container').submit();
}

// function showPopupFolder() {
//     $('.popup-folder-creation').css("visibility", "visible");
// }
