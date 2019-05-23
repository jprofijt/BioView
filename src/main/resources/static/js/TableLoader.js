/**
 * For every image table, load image tags from API
 */
$(document).ready(function () {
        $('.tag-table').each(function () {
            let id = $(this).attr('id').slice(-1);
            new LoadTagTable(id);
            console.log(id);
        });
    });

/**
 * inserts the tags of an image using BioView Api
 * @param id id of image
 * @constructor
 * @author Jouke Profijt
 */
function LoadTagTable(id) {
    const url = "http://"+document.location.hostname + ":8081/api/tags/?image=" + id.toString();
    $.ajax({
        url: url,
        type: "GET",
        crossDomain: true,
        success: function (data) {
            $.each(data.tags, function (tag) {
                $('#ImageTags-' + id).append("<tr><td>" + data.tags[tag] + "</td></tr>")
            })
        }
    })

}
