// $(document).on("dblclick", )(function(){
//     $( "#bioimage" ).dblclick(function() {
//         var path = $("#bioimage").val();
//         window.location.replace($(this).attr(path));
//         alert("it works: " + path);
//     });
// });
$(document).on("dblclick", "#bioimage", function openImageOnDblclick(path) {
    // window.location.replace($(this).attr(path));
    alert("it works: " + path);
    //code here
});



