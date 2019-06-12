function pickPicContextCommand(key) {
    if (key == "img-sort-by-name"){
        sortImageByName()
    }
    else if (key == "img-sort-by-date"){
        sortImageByDate()
    }
    else if (key == "img-delete"){
        deleteSelectedImages()
    }
    else if( key == "img-move"){
        $('#moveImageModal').modal('toggle');
    }
    else if (key == "img-copy"){
        $('#copyImageModal').modal('toggle');
    }
    else if (key == "img-rename"){
        if ($('.pic-select').length < 2) {
            $('#renameImageModal').modal('toggle');
        }
    }
    else if (key == "img-properties") {
        if ($('.pic-select').length < 2) {
            $('#imgPropertyModal').modal('toggle');
        }
    }
    else if (key == "img-edit"){
        if ($('.pic-select').length < 2) {
            openImageModal()
        }
    }
}

$(function() {
    $.contextMenu({
        selector: '.context-menu-pics-selected',
        callback: function(key, options) {
            pickPicContextCommand(key)
        },
        items: {
            "img-edit": {name: "Edit", icon: "fas fa-pencil-alt"},
            "img-move": {name: "Move", icon: "fas fa-cut"},
            "img-copy": {name: "Copy", icon: "fas fa-copy"},
            "img-delete": {name: "Delete", icon: "fas fa-trash-alt"}
            // "img-rename": {name: "Rename", icon: "fas fa-edit"},
            // "img-properties": {name: "Properties", icon: "fas fa-info"}
        }
    });
    $.contextMenu({
        selector: '.context-menu-pics-unselected',
        callback: function(key, options) {
            pickContextCommand(key)
        },
        items: {
            "img-sort-by": {name: "Sort By", icon: "fas fa-sort",
                items: {
                    "img-sort-by-name": {name: "Name"},
                    "img-sort-by-date": {name: "Date"}
                }
            }
        }
    });
});

// Shows the Image select navbar
function showImageSelectNav() {
    $('.img-navbar-unselected').hide();
    $('.img-navbar-selected').css("display", "flex");
}

// Shows the Image unselect navbar
function showImageUnselectNav(){
    $('.img-navbar-selected').hide();
    $('.img-navbar-unselected').show();
}

$(document).on("click contextmenu", ".picture-img a img", function(e) {
    if (e.ctrlKey) {
        $(this).addClass("pic-select");
    } else {
        $(".pic-select").removeClass("pic-select");
        $(this)
            .addClass("pic-select");
    }
    showImageSelectNav()
});

$(document).on("click dblclick", ".img-gallery", function() {
    $(".picture-img a img")
        .removeClass("pic-select");
    showImageUnselectNav()
});
$(document).on("click", "picture-img", function() {
    $(".pic-select").removeClass("pic-select");
});

$(document).on("click", ".picture-img a img", function(e) {
    e.stopPropagation();
});


$(document).on('show.bs.modal','#imgPropertyModal', function (e) {
    if ($('.pic-select').length > 1){
        console.log($('.select').length);
        e.preventDefault();
    } else {
        var path = $('.pic-select').parent().siblings('.image-path').val().replace("\\", "/");
        var url = "http://" + document.location.hostname + ":8081/api/metadata/filepath";
        $.getJSON(url, {path: path}, function (data) {
            console.log(data);
        })
    }
});

/*---Toast messages ---*/
function isEmpty(value) {
    return typeof value == 'string' && !value.trim() || typeof value == 'undefined' || value === null;
}

$(function () {
    function Toast(type, msg) {
        this.type = type;
        this.msg = msg;
    }
    toastr.options = {
        "closeButton": true,
        "positionClass": "toast-bottom-left",
        "showDuration": "300",
        "hideDuration": "1000",
        "timeOut": "5000",
        "extendedTimeOut": "1000",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
    };
    var toasts = [];
    if (!isEmpty($('#successMessages').val())){
        if(!isEmpty($('#successMessages').val().slice( 1, -1))){
            var successMessages = $('#successMessages').val().slice( 1, -1).split(",");
            $.each(successMessages, function (index, value) {
                toasts.push(new Toast("success", value))
            })
        }
    }
    if (!isEmpty($('#errorMessages').val())){
        if (!isEmpty($('#errorMessages').val().slice( 1, -1))){
            var errorMessages = $('#errorMessages').val().slice( 1, -1).split(",");
            $.each(errorMessages, function (index, value) {
                toasts.push(new Toast("error", value))
            })
        }
    }
    $.each(toasts, function (index, t) {
        setTimeout( function () {
            toastr[t.type](t.msg);
        }, 500*index)
    })
});

/*---Image sort by buttons---*/
// Uses placeholder .image-path that doesnt work.
var imageNameOrder = 'asc';
function sortImageByName(){
    tinysort('ul.images > li',{selector : '.image-path', order : imageNameOrder});
    if (imageNameOrder === 'asc') {
        imageNameOrder = 'desc'
    }
    else {
        imageNameOrder = 'asc'
    }
}
$(document).on("click", '[data-sort="image-name"]', function () {
    sortImageByName()
});

// !! Still no existing date parameter so selector is a placeholder
var imageDateOrder = 'asc';
function sortImageByDate(){
    tinysort('ul.images > li',{selector : '.image-path', attr:'value', order : imageDateOrder});
    if (imageDateOrder === 'asc') {
        imageDateOrder = 'desc'
    }
    else {
        imageDateOrder = 'asc'
    }
}
$(document).on("click", '[data-sort="image-date"]', function () {
    sortImageByDate()
});


function deleteSelectedImages() {
    $('.pic-select').each(function (index) {
        var directory = $(this).parent().siblings('.image-path').val().replace("\\", "/");
        var imageName = directory.lastIndexOf('/');

        $.ajax({
            type: "POST",
            url: "/deleteimage",
            dataType: "text",
            data: {'directory' : directory},
            success: function (data) {
                toastr["success"]("Successfully deleted " + imageName + "!");
            },
            error: function(xhr, desc, err) {
                toastr["error"]("Could not delete " + imageName + "!");
            }
        });
    });
    $('.pic-select').parents('li').remove();
    $('.pic-select').removeClass("select");
    showImageUnselectNav()
}

$(document).on("click", '[data-function="delete-image"]', function () {
    deleteSelectedImages()
});

function openImageModal() {
    if ($('.pic-select').length > 1){
        e.preventDefault();
    } else {
        let count = $('.pic-select').parent().siblings('.image-iter').val();
        let path = $('.pic-select').parent().siblings('.image-path').val();
        loadDynamicModal(count, path);
    }
}

$(document).on("click", '[data-function="edit-image"]', function () {
    openImageModal()
});