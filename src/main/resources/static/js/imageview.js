// $(document).on("dblclick", )(function(){
//     $( "#bioimage" ).dblclick(function() {
//         var path = $("#bioimage").val();
//         window.location.replace($(this).attr(path));
//         alert("it works: " + path);
//     });
// });
$(document).on("dblclick", "#bioimage", function openImageOnDblclick(imagepath) {
    // window.location.replace($(this).attr(path));
    alert("Image location: " + imagepath);
    // window.open("localhost:8080/" + path);
    document.getElementById("jan").innerHTML = imagepath;

    //code here
});



