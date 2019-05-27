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

let available;
const url = "http://"+document.location.hostname + ":8081/api/tags/all/";
$.getJSON(url, function (result) {
    available = result;});


$(document).ready(function () {
    $('.newTag').typeahead({
        highlight: true,
        source: available
    })
});

/**
 * inserts the tags of an image using BioView Api
 * @param id id of image
 *
 * @author Jouke Profijt
 */
function LoadTagTable(id) {
    const url = "http://"+document.location.hostname + ":8081/api/tags/image/?image=" + id.toString();
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

/**
 * sends a new tag to the BioView Api
 * @param element button element
 * @param user the user that added the new tag
 * @param id image id
 *
 * @author Jouke Profijt
 */
function AddTag(element, user, id) {
    //const url = 'http://'+document.location.hostname + ':8081/api/tags/';
    const tag = $('#tag-input-' + id).val();
    const data = {
        id: id,
        tag: tag,
        user: user
    };

    $.ajax({
        type: "POST",
        url: url,
        data: data,
        success: function () {
            $('#ImageTags-' + id).append("<tr><td>" + $('#tag-input-' + id).val() + "</td></tr>")
        },
        dataType: 'application/json'
    })



}
