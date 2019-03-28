$(document).ready(function (){
    $('#tree').fancytree({
        source: {
            url: "/getFolders",
            cache: false
        }

    })
});
