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
        var path = $('.pic-select').find('.image-path').val();
        var url = "http://" + document.location.hostname + ":8081/api/metadata/filepath";
        $.getJSON(url, {path: path}, function (data) {
            console.log(data);
        })
    }
});