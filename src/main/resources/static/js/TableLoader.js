/**
 * Handles all user tag interaction with api
 *
 * @author Jouke Profijt
 */


$(document).ready(function () {

        $('.tag-table').each(function () {
            let id = $(this).attr('id').replace("ImageTags-", "");
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
                //$('#ImageTags-' + id).append("<tr><td>" + data.tags[tag] + "</td></tr>")
                console.log("appending new " + data.tags);
                AppendNewTagToTable(id, data.tags[tag]);

            })
        }
    })

}

/**
 * Appends a new tag to the existing tag table for user only
 * @param id image id
 * @param tag tag to be added
 * @constructor
 */
function AppendNewTagToTable(id, tag) {
    $('#ImageTags-' + id).append("<tr><td>" + tag + "</td></tr>")

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
    console.log(tag);
    const data = {
        id: id,
        tag: tag,
        user: user
    };
    const TagTable = '#ImageTags-' + id + ' tr > td:contains(' + tag + ')';

    if (!$(TagTable).length >= 1) {
        $('#tag-input-div-' + id).removeClass("has-error");
        $.ajax({

            type: "POST",
            url: "http://" + document.location.hostname + ":8081/api/tags/image/",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            async: true,
            success: function () {
                //$('#ImageTags-' + id).append("<tr><td>" + $('#tag-input-' + id).val() + "</td></tr>")
                new AppendNewTagToTable(id, tag);
                $('#tag-input-' + id).val("")
            }
        })
    } else {
        $('#tag-input-div-' + id).addClass("has-error");
    }

}
