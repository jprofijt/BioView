/**
 * Scripts that manage the basic folder manager actions
 *
 * @author Kim Chau Duong
 * @version 1.0
 */

// Submits on double click
$(document).on('dblclick', '.folder-manager ul li form div', function(e) {
    $(this).addClass('folder-active');
    $(this).parents('form').submit();
});


// Adds select class to selected folder(s)
$(document).on("click", "[data-file-icon] form div", function(e) {
    if (e.ctrlKey) {
        $(this).addClass("select");
    } else {
        $(".select").removeClass("select");
        $(this)
            .addClass("select");
        // $('.append-option-box').css("visibility", "hidden");
    }
});

// Deselects (and submits new folder) when clicking elsewhere
$(document).on("click dblclick", ".folder-manager", function() {
    $("[data-file-icon] div")
        .removeClass("select");
});

$(document).on("click", "[data-file-icon]", function() {
    $(".select").removeClass("select");
});

$(document).on("click", "[data-file-icon] form div", function(e) {
    e.stopPropagation();
});


/*---Context Menu ---*/


$(function() {
    $.contextMenu({
        selector: '.context-menu-folder-unselected',
        callback: function(key, options) {
            if (key == "New Folder"){
                createNewFolder();
            }
        },
        items: {
            "New Folder": {name: "New Folder", icon: "fas fa-folder-plus"},
            "move": {name: "move", icon: "fas fa-cut"},
            copy: {name: "Copy", icon: "fas fa-copy"},
            "paste": {name: "Paste", icon: "fas fa-paste"},
            "delete": {name: "Delete", icon: "fas fa-trash-alt"}
        }
    });

});


/*---Creates new folder that you can name---*/
function createNewFolder(){
    $(".creating").removeClass("creating");
    $(".folder-creation-container").css("display", "inline-block");
    $(".folder-creation-container").addClass("creating");
    $("#dirInput").select().focus();
}
/*---Creates folder when clicking away from input---*/
$(document).on('blur', "#dirInput", function () {
    $(".creating").find("form").submit();
});

$(document).on("click", '[data-function="new-folder"]',function() {
    createNewFolder();
    var position = $(".creating").position().top + $('.folder-manager').scrollTop()
    $('.folder-manager').animate({
        scrollTop: position
    }, 1000);
});

/*---Sort by buttons---*/
var nameOrder = 'asc';
$(document).on("click", '[data-sort="folder-name"]', function () {
    tinysort('ul#folders > li',{selector : 'b:not(.created-title)', order : nameOrder});
    if (nameOrder === 'asc') {
        nameOrder = 'desc'
    }
    else {
        nameOrder = 'asc'
    }
});
var dateOrder = 'asc';
$(document).on("click", '[data-sort="folder-date"]', function () {
    tinysort('ul#folders > li',{selector : '.last-modified-date', attr:'value', order : dateOrder});
    if (dateOrder === 'asc') {
        dateOrder = 'desc'
    }
    else {
        dateOrder = 'asc'
    }
});