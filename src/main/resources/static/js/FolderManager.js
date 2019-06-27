/**
 * Scripts that manage the basic folder manager actions
 *
 * @author Kim Chau Duong
 * @version 1.0
 */

// Submits on double click
$(document).on('dblclick', '.folder-manager ul li form div', function() {
    $(this).addClass('folder-active');
    $(this).parents('form').submit();
});

// Shows the folder select navbar
function showFolderSelectNav() {
    $('.folder-navbar-unselected').hide();
    $('.folder-navbar-selected').css("display", "flex");
}

// Shows the folder unselect navbar
function showFolderUnselectNav(){
    $('.folder-navbar-selected').hide();
    $('.folder-navbar-unselected').show();
}

// Adds select class to selected folder(s) and shows the select navbar
$(document).on("click contextmenu", "[data-file-icon] form div", function(e) {
    if (e.ctrlKey) {
        $(this).addClass("select");
    } else {
        $(".select").removeClass("select");
        $(this)
            .addClass("select");
    }
    showFolderSelectNav();
});

// Deselects (and submits new folder) when clicking elsewhere and shows the default unselected navbar
$(document).on("click dblclick", ".folder-manager", function() {
    $("[data-file-icon] div")
        .removeClass("select");
    showFolderUnselectNav();
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
        sortbyName()
    }
    else if (key == "sort-by-date"){
        sortByDate()
    }
    else if (key == "delete"){
        deleteSelected()
    }
    else if( key == "move"){
        $('#moveModal').modal('toggle');
    }
    else if (key == "copy"){
        $('#copyModal').modal('toggle');
    }
    else if (key == "rename"){
        if ($('.select').length < 2) {
            $('#renameModal').modal('toggle');
        }
    }
    else if (key == "open"){
        if ($('.select').length < 2) {
            $('.select').parents('form').submit();
        }
    }
    else if (key == "properties") {
        if ($('.select').length < 2) {
            $('#folderPropertyModal').modal('toggle');
        }
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
            "move": {name: "Move", icon: "fas fa-cut"},
            "copy": {name: "Copy", icon: "fas fa-copy"},
            "delete": {name: "Delete", icon: "fas fa-trash-alt"},
            "rename": {name: "Rename", icon: "fas fa-edit"},
            "properties": {name: "Properties", icon: "fas fa-info"}
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
function sortbyName(){
    tinysort('ul#folders > li',{selector : 'b:not(.created-title)', order : nameOrder});
    if (nameOrder === 'asc') {
        nameOrder = 'desc'
    }
    else {
        nameOrder = 'asc'
    }
}
$(document).on("click", '[data-sort="folder-name"]', function () {
    sortbyName()
});

var dateOrder = 'asc';
function sortByDate(){
    tinysort('ul#folders > li',{selector : '.folder-div', data:'folder-date', order : dateOrder});
    if (dateOrder === 'asc') {
        dateOrder = 'desc'
    }
    else {
        dateOrder = 'asc'
    }
}
$(document).on("click", '[data-sort="folder-date"]', function () {
    sortByDate()
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
        var selectedList = $(this);
        var directoryname = $(this).find('b').text();
        var directory = $(this).siblings('[name = "location"]').val();
        $.ajax({
            type: "POST",
            url: "/deletefolder",
            dataType: "text",
            data: {'directory' : directory},
            success: function (data) {
                toastr["success"]("Successfully deleted " + directoryname + "!");
                $(selectedList).parents('li').remove();
            },
            error: function(xhr, desc, err) {
                toastr["error"]("Could not delete " + directoryname + "!");
            }
        });
        });
    $('.select').removeClass("select");
    showFolderUnselectNav();
}

$(document).on("click", '[data-function="delete-folder"]', function () {
    deleteSelected()
});

/*---Rename command---*/
$(document).on('show.bs.modal','#renameModal', function (e) {
    if ($('.select').length > 1){
        e.preventDefault();
    } else {
        var directory = $('.select').siblings('[name = "location"]').val();
        var folderName = $('.select').find('b.folder-name').text();
        $('input[name="renamedFolder"]').val(directory);
        $('input[name="newFolderName"]').val(folderName);
    }

});

$(document).on('shown.bs.modal','#renameModal', function () {
    $('input[name="newFolderName"]').select().focus();
});

$(document).on("click", '[data-function="rename-folder"]', function () {
    if ($('.select').length < 2) {
        $('#renameModal').modal('toggle');
    }
});


function loadFolderProperties(){
    var path = $('.select').siblings('[name = "location"]').val();
    var name = $('.select').attr('title');
    var date = $('.select').attr('data-folder-date');
    var size = $('.select').attr('data-folder-size');

    $('.folder-property-name').text(name);
    $('.folder-property-location').text(path);
    $('.folder-property-size').text(formatBytes(size));
    $('.folder-property-date').text(date);
}

$(document).on('show.bs.modal','#folderPropertyModal', function (e) {
    if ($('.pic-select').length > 1){
        e.preventDefault();
    } else {
        loadFolderProperties();
        loadUniqueFolderTags()
    }
});

$(document).on("click", '[data-function="folder-properties"]', function (e) {
    if ($('.pic-select').length > 1){
        e.preventDefault();
    } else {
        $('#folderPropertyModal').modal('toggle');
    }
});




function addFolderTag(data) {
    $.each(data, function (i, tag) {
        $('#unique-folder-tags').tagsinput('add',tag);
    })
}

// Unique Folder Tags in properties modal
function loadUniqueFolderTags(){
    $('#unique-folder-tags').tagsinput('removeAll');
    var path = $('.select').siblings('[name = "location"]').val();
    var url = "http://" + document.location.hostname + ":8081/api/metadata/directory/tags";
    $.ajax({
        type: "GET",
        url: url,
        dataType: "json",
        data: {path: path},
        success: function (data) {
            addFolderTag(data);
        },
        error: function(xhr, desc, err) {
            toastr["error"]("Could not find folder tags!");
        }
    });
    $('.property-template .bootstrap-tagsinput input[type=text]').prop("readonly", true);
}