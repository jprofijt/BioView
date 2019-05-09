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

$(document).on("click", "[data-file-icon]", function(e) {
    if (e.ctrlKey) {
        $(this).addClass("select");
        // $(this).removeClass("renaming");
    } else {
        $(".select").removeClass("select");
        $(this)
            .addClass("select")
            .siblings()
            .removeClass("select");
        // $(this).removeClass("renaming");
    }
});


$(document).on("click dblclick", function() {
    $("[data-file-icon]")
        // .removeClass("context-visible")
        .removeClass("select");
    // $(".append-option-box").remove();
    // removeUnwanted();
    // $(".name").attr("contenteditable", false);
});
// $(document).on("click contextmenu", ".append-option-box", function(e) {
//     e.stopPropagation();
//     $("[data-file-icon]")
//         .removeClass("context-visible")
//         .removeClass("select");
//     $(".append-option-box").remove();
// });
$(document).on("click", "[data-file-icon]", function(e) {
    e.stopPropagation();
});
// function removeUnwanted(){
//     $('.active-folder-wrapper ~ .active-folder-wrapper,.active-folder-wrapper ~ .no-item-inside-folder').remove();
// }