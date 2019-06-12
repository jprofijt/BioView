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
    for (let i in result) {
        available.push(result[i])
    }
});

function ReloadTable(id) {
    $('#ImageRois-' + id).find('tbody').find('tr').remove();
    LoadRoiTable(id);
}

$(document).ready(function () {
    const add = $('.add-button');
    const save = $('.save-button');
    const cancel = $('.cancel-button');
    add.on("click", function () {
        $('.image-roi-row').removeClass("bg-primary selected");
        save.attr('hidden', false);
        cancel.attr('hidden', false);
        add.attr('hidden', true);
        let EditingId = $(this).parent().attr('id');
        $('#ImageRois-' + EditingId).append("<tr class='image-roi-row' id='editing-row'>" +
            "<td>#</td>" +
            "<td><input type='number' id='ph' name='ph' min='0' max='14' placeholder='pH' step='0.01'></td>" +
            "<td><input type='number' id='t' name='t' min='0'></td>" +
            "<td><input type='number' id='o2' name='o2' min='0' max='100' placeholder='oxygen %'></td>" +
            "<td><input type='number' id='co2' name='co2' min='0' max='100' placeholder='Co2 %'></td>" +
            "</tr>");
    });

    cancel.on("click", function () {
        let EditingId = $(this).parent().attr('id');
        $('#editing-row').remove();
        save.attr('hidden', true);
        cancel.attr('hidden', true);
        add.attr('hidden', false);
    });

    save.on("click", function () {
        let EditingId = $(this).parent().attr('id');
        let editingRow = $('#editing-row');
        let inputs = editingRow.find('input');
        let InputData = {
            id: EditingId,
            ph: parseFloat(inputs[0].value),
            temp:  parseInt(inputs[1].value),
            o2: parseInt(inputs[2].value),
            co2: parseInt(inputs[3].value)
        };

        /**
         * @return {boolean}
         */
        function CheckInputs(InputData) {
            if (InputData.ph > 14 || InputData.ph < 0) {
                return false;
            }
            if (InputData.o2 < 0 || InputData.o2 > 100){
                return false;
            }
            if (InputData.co2 < 0 || InputData.co2 > 100) {
                return false;
            }
            return true
        }

        if (CheckInputs(InputData)) {
            const url = "http://"+document.location.hostname + ":8081/api/roi/state/";
            $.ajax({
                url: url,
                type: "POST",
                data: JSON.stringify(InputData),
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function () {

                    ReloadTable(EditingId);
                    save.attr('hidden', true);
                    cancel.attr('hidden', true);
                    add.attr('hidden', false)
                }

                }

            )
        }

    })



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
    const RowID = "image-" + id + "-roi-" + roi.id;
    $('#ImageRois-' + id).append("<tr class='image-roi-row' id='"+ RowID + "'>" +
        "<td>" + roi.id + "</td>" +
        "<td>" + roi.ph + "</td>" +
        "<td>" + roi.temp + "</td>" +
        "<td>" + roi.o2 + "</td>" +
        "<td>" + roi.co2 + "</td>" +
        "</tr>");

    $('.image-roi-row').on("click", function () {
        Ready = false;
        $('.image-roi-row').removeClass("bg-primary selected");
        $(this).addClass("bg-primary selected");
        const currentTags = $('#tag-input-roi-' + id).tagsinput('items');
        console.log(currentTags);

        $('#tag-input-roi-' + id).tagsinput('removeAll');




        selected = $(this).attr('id').replace(new RegExp("image-[0-9]+-roi-"), "");
        console.log(selected);
        SelectedImage = $(this).attr('id').replace("image-", "").replace(new RegExp("-roi-[0-9]+"), "");
        $.getJSON("http://"+document.location.hostname + ":8081/api/roi/tags/?roi=" + selected, function (result) {
            //let remainingTags = $('#tag-input-roi-' + id).tagsinput('items');
            //checkRemain(remainingTags, id);
            for (let i in result.tags){
                $('#tag-input-roi-' + SelectedImage).tagsinput('add', result.tags[i])
            }
        });
    Ready = true;
    });

    function checkRemain(tags, id) {
        if ($.isEmptyObject(tags)) {
            return
        }
        else {
            for (let i in tags) {
                $('#tag-input-roi-' + id).tagsinput('remove', tags[i]);
            }
            checkRemain($('#tag-input-roi-' + id).tagsinput('items'), id);
        }

    }
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

