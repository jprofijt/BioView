/**
 * Scripts that manage the basic folder manager actions
 *
 * @author Kim Chau Duong
 * @version 1.0
 */

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
    $(".creating").find("form").submit();
});

$(document).on("click", "[data-file-icon]", function(e) {
    e.stopPropagation();
});


/*---Context Menu ---*/

$(document).on("contextmenu", ".folder-manager", function(e) {
    var top = e.pageY;
    var left = e.pageX;

    $('.append-option-box').css({"visibility" : "visible", "top": top + "px", "left": left+"px"});
    return false;
});

$(document).on("click contextmenu dblclick", function() {
    $("[data-file-icon]")
        .removeClass("select");
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

/*---Creates folder with current date as its name---*/
function createDateFolder() {
    $('.date-folder-container').submit();
}
$(document).on("click", '[data-function="new-date-folder"]',function() {
    createDateFolder();
});

/*---Creates new folder that you can name---*/
function createNewFolder(){
    $(".creating").removeClass("creating");
    $(".popup-folder-creation").css("visibility", "visible");
    $(".popup-folder-creation").addClass("creating");
    $("#dirInput").select().focus();

}

$(document).on("click", '[data-function="new-folder"]',function() {
    createNewFolder();
});



// function showPopupFolder() {
//     $('.popup-folder-creation').css("visibility", "visible");
// }
