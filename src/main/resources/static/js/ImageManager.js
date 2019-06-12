function pickPicContextCommand(key) {
    if (key == "properties") {
        $('#imgPropertyModal').modal('toggle');
    }
}

$(function() {
    $.contextMenu({
        selector: '.context-menu-pics',
        callback: function(key, options) {
            pickPicContextCommand(key)
        },
        items: {
            "properties": {name: "Properties", icon: "fas fa-info"}

        }
    });
});

$(document).on("click contextmenu", ".picture-img", function(e) {
    if (e.ctrlKey) {
        $(this).addClass("pic-select");
    } else {
        $(".pic-select").removeClass("pic-select");
        $(this)
            .addClass("pic-select");
    }

});

$(document).on("click dblclick", ".pictures", function() {
    $(".picture-img")
        .removeClass("pic-select");

});

$(document).on("click", ".picture-img", function(e) {
    e.stopPropagation();
});

$(document).on('show.bs.modal','#imgPropertyModal', function (e) {
    if ($('.pic-select').length > 1){
        console.log($('.select').length);
        e.preventDefault();
    } else {
        var path = $('.pic-select').find('.image-path').val().replace("\\", "/");
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
    var toasts = []
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