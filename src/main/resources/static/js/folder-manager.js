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


// Adds select class to selected folder(s) and shows the select navbar
$(document).on("click contextmenu", "[data-file-icon] form div", function(e) {
    if (e.ctrlKey) {
        $(this).addClass("select");
    } else {
        $(".select").removeClass("select");
        $(this)
            .addClass("select");
        $('.folder-navbar-unselected').hide();
        $('.folder-navbar-selected').show();
    }
});

// Deselects (and submits new folder) when clicking elsewhere and shows the default unselected navbar
$(document).on("click dblclick", ".folder-manager", function() {
    $("[data-file-icon] div")
        .removeClass("select");
    $('.folder-navbar-selected').hide();
    $('.folder-navbar-unselected').show();
});

$(document).on("click", "[data-file-icon]", function() {
    $(".select").removeClass("select");
});

$(document).on("click", "[data-file-icon] form div", function(e) {
    e.stopPropagation();
});


/*---Context Menu ---*/
function pickContextCommand(key) {
    if (key == "new-folder"){
        createNewFolder();
    }
    else if (key == "sort-by-name"){
        sortbyName('ul#folders > li', 'b:not(.created-title)')
    }
    else if (key == "sort-by-date"){
        sortByDate('ul#folders > li', '.last-modified-date')
    }
    else if (key == "delete"){
        deleteSelected()
    }
}

$(function() {
    $.contextMenu({
        selector: '.context-menu-folder-unselected',
        callback: function(key, options) {
            pickContextCommand(key)
        },
        items: {
            "new-folder": {name: "New Folder", icon: "fas fa-folder-plus"},
            "sort-by": {name: "Sort By", icon: "fas fa-sort",
                items: {
                "sort-by-name": {name: "Name"},
                "sort-by-date": {name: "Date"}
                }
            }
        }
    });
    $.contextMenu({
        selector: '.context-menu-folder-selected',
        callback: function(key, options) {
            pickContextCommand(key)
        },
        items: {
            "open": {name: "Open", icon: "fas fa-folder-open"},
            "cut": {name: "Cut", icon: "fas fa-cut"},
            copy: {name: "Copy", icon: "fas fa-copy"},
            "paste": {name: "Paste", icon: "fas fa-paste"},
            "delete": {name: "Delete", icon: "fas fa-trash-alt"},
            "properties": {name: "Properties"}
        }
    });
});


/*---Creates new folder that you can name---*/
function createNewFolder(){
    $(".creating").removeClass("creating");
    $(".folder-creation-container").css("display", "inline-block");
    $(".folder-creation-container").addClass("creating");
    $("#dirInput").select().focus();
    var position = $(".creating").position().top + $('.folder-manager').scrollTop()
    $('.folder-manager').animate({
        scrollTop: position
    }, 1000);
}
/*---Creates folder when clicking away from input---*/
$(document).on('blur', "#dirInput", function () {
    $(".creating").find("form").submit();
});

$(document).on("click", '[data-function="new-folder"]',function() {
    createNewFolder();
});

/*---Sort by buttons---*/
var nameOrder = 'asc';
function sortbyName(list, element){
    tinysort(list,{selector : element, order : nameOrder});
    if (nameOrder === 'asc') {
        nameOrder = 'desc'
    }
    else {
        nameOrder = 'asc'
    }
}
$(document).on("click", '[data-sort="folder-name"]', function () {
    sortbyName('ul#folders > li', 'b:not(.created-title)')
});

var dateOrder = 'asc';
function sortByDate(list, element){
    tinysort(list,{selector : element, attr:'value', order : dateOrder});
    if (dateOrder === 'asc') {
        dateOrder = 'desc'
    }
    else {
        dateOrder = 'asc'
    }
}
$(document).on("click", '[data-sort="folder-date"]', function () {
    sortByDate('ul#folders > li', '.last-modified-date')
});

/*--- Delete command---*/
// Spring csrf token
$(function () {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
});

function deleteSelected() {
    $('.select').each(function (index) {
        var directory = $(this).siblings('[name = "location"]').val();
        $.ajax({
            type: "POST",
            url: "/deletefolder",
            dataType: "text",
            data: {'directory' : directory},
            success: function (data) {
                console.log("successfully deleted", directory);
            },
            error: function(xhr, desc, err) {
                console.log(xhr);
                console.log("Details0: " + desc + "\nError:" + err);
            }
        });
        })
    $('.select').parents('li').css("display", "none");
    $(".select").removeClass("select");
    $('.folder-navbar-selected').hide();
    $('.folder-navbar-unselected').show();
}

$(document).on("click", '[data-function="delete-folder"]', function () {
    deleteSelected()
});
