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

