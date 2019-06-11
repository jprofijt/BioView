/**
 * Handles all user tag interaction with api
 *
 * @author Jouke Profijt
 */


$(document).ready(function () {

        $('.tag-table').each(function () {
            let id = $(this).attr('id').replace("ImageRois-", "");
            new LoadRoiTable(id)
        });

    });

let available = [];
const url = "http://"+document.location.hostname + ":8081/api/tags/all/";
$.getJSON(url, function (result) {
    for (const i in result) {
        available.push(result[i])
    }
});


$(document).ready(function () {
    $('.TagInput').tagsinput({
        typeahead: {
            highlight: true,
            source: available
        }
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

function LoadRoiTable(id) {
    const url = "http://"+document.location.hostname + ":8081/api/roi/state/?image=" + id.toString();

    $.ajax({
        url: url,
        type: "GET",
        crossDomain: true,
        success: function (data) {
            $.each(data, function (roi) {
                //$('#ImageTags-' + id).append("<tr><td>" + data.tags[tag] + "</td></tr>")
                AppendNewRoiToTable(id, data[roi])

            })
        }
    })

}

let selected;
let SelectedImage;
let Ready = true;

function AppendNewRoiToTable(id, roi) {
    const RowID = "image-" + id + "-roi-" + roi.roiID;
    $('#ImageRois-' + id).append("<tr class='image-roi-row' id='"+ RowID + "'>" +
        "<td>" + roi.roiID + "</td>" +
        "<td>" + roi.ph + "</td>" +
        "<td>" + roi.t + "</td>" +
        "<td>" + roi.oxygen + "</td>" +
        "<td>" + roi.co2 + "</td>" +
        "</tr>");

    $('.image-roi-row').on("click", function () {
        Ready = false;
        $('.image-roi-row').removeClass("bg-primary selected");
        $(this).addClass("bg-primary selected");
        const currentTags = $('#tag-input-roi-' + id).tagsinput('items');
        console.log(currentTags);
        for (let i in currentTags) {
            $('#tag-input-roi-' + id).tagsinput('remove', currentTags[i]);

        }


        selected = $(this).attr('id').replace(new RegExp("image-[0-9]+-roi-"), "");
        SelectedImage = $(this).attr('id').replace("image-", "").replace(new RegExp("-roi-[0-9]+"), "");
        $.getJSON("http://"+document.location.hostname + ":8081/api/roi/tags/?roi=" + selected, function (result) {
            for (let i in result){
                $('#tag-input-roi-' + SelectedImage).tagsinput('add', result[i])
            }
        });
    Ready = true;
    })

}

$(document).ready(function () {
    $('.TagInput').on('itemAdded', function(event) {
        if (Ready) {
            let data = {
                id: selected,
                tag: event.item
        };


            $.ajax({
                type: "POST",
                url: "http://" + document.location.hostname + ":8081/api/roi/tags/",
                data: JSON.stringify(data),
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                async: true,
                success: function () {
                    //$('#ImageTags-' + id).append("<tr><td>" + $('#tag-input-' + id).val() + "</td></tr>")

                    console.log('Adding tag success')
                }
            })
        }
    });
    });

