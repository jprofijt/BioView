/**
 * Handles all user tag interaction with api
 *
 * @author Jouke Profijt
 */



/**
 * Fills each table with the correct Regions of interest
 * @author Jouke Profijt
 */
$(document).ready(function () {
    $('.tag-table').each(function () {
        let id = $(this).attr('id').replace("ImageRois-", "");
        new LoadRoiTable(id)
    });
});

/**
 * Pre-existing Tags the user can choose from
 * @type {Array}
 *
 * @author Jouke Profijt
 */
let available = [];
const url = "http://"+document.location.hostname + ":8080/api/tags/all/";
$.getJSON(url, function (result) {
    for (let i in result) {
        available.push(result[i])
    }
});

/**
 * Reloads Regions of interest for given id
 * @param id    Image id
 *
 * @author Jouke Profijt
 */
function ReloadTable(id) {
    $('#ImageRois-' + id).find('tbody').find('tr').remove();
    LoadRoiTable(id);
}

/**
 * Handles Adding of regions of interest
 *
 * @author Jouke Profijt
 */
$(document).ready(function () {
    const add = $('.add-button');
    const save = $('.save-button');
    const cancel = $('.cancel-button');
    //Add button creates new form fields
    add.on("click", function () {
        $('.image-roi-row').removeClass("bg-primary selected");
        save.attr('hidden', false);
        cancel.attr('hidden', false);
        add.attr('hidden', true);
        let EditingId = $(this).parent().attr('id');
        $('#ImageRois-' + EditingId).append("<tr class='image-roi-row' id='editing-row'>" +
            "<td>#</td>" +
            "<td><input type='number' id='ph' name='ph' min='0' max='14' placeholder='pH' step='0.01'></td>" +
            "<td><input type='number' id='t' name='t' min='-273.15' placeholder='Temperature (C)' step='0.01'></td>" +
            "<td><input type='number' id='o2' name='o2' min='0' max='100' placeholder='oxygen %'></td>" +
            "<td><input type='number' id='co2' name='co2' min='0' max='100' placeholder='Co2 %'></td>" +
            "</tr>");
    });

    //cancel cancels adding of new ROI
    cancel.on("click", function () {
        let EditingId = $(this).parent().attr('id');
        let error = $('#InputError-'+EditingId);
        $('#editing-row').remove();
        save.attr('hidden', true);
        cancel.attr('hidden', true);
        add.attr('hidden', false);
        error.attr('hidden', true);
    });

    //Sends the new roi data to api
    save.on("click", function () {
        let EditingId = $(this).parent().attr('id');
        let editingRow = $('#editing-row');
        let inputs = editingRow.find('input');
        let InputData = {
            id: EditingId,
            ph: parseFloat(inputs[0].value),
            temp:  parseFloat(inputs[1].value),
            o2: parseInt(inputs[2].value),
            co2: parseInt(inputs[3].value)
        };

        /**
         * Checks the user entered fields
         *
         * @return {boolean}
         * @author Jouke Profijt
         */
        function CheckInputs(InputData) {
            if (isNaN(InputData.ph) || InputData.ph > 14 || InputData.ph < 0) {
                return false;
            }
            if (isNaN(InputData.o2) || InputData.o2 < 0 || InputData.o2 > 100){
                return false;
            }
            if (isNaN(InputData.co2) || InputData.co2 < 0 || InputData.co2 > 100) {
                return false;
            }
            if (isNaN(InputData.temp) || InputData.temp < -273.15){
                return false;
            }
            return true
        }

        if (CheckInputs(InputData)) {
            $('#InputError-'+EditingId).attr("hidden", true);
            const url = "http://"+document.location.hostname + ":8081/api/roi/state/";
            $.ajax({
                url: url,
                type: "POST",
                data: JSON.stringify(InputData),
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function () {
                    toastr["success"]("Successfully Added Region of Interest!");
                    ReloadTable(EditingId);
                    save.attr('hidden', true);
                    cancel.attr('hidden', true);
                    add.attr('hidden', false)
                }

                }

            )
        } else {
            toastr["error"]("Could Not add Roi: incorrect inputs!");
        }

    })



});


/**
 * inserts the tags of an image using BioView Api
 * @param id id of image
 *
 * @deprecated Tag table implementation removed.
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
                AppendNewTagToTable(id, data.tags[tag]);

            })
        }
    })

}

/**
 * Appends a new tag to the existing tag table for user only
 * @param id image id
 * @param tag tag to be added
 * @deprecated tag table implementation removed
 *
 * @author Jouke Profijt
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
 * @deprecated Tag table implementation removed
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

/**
 * Loads regions of interest from api call
 *
 * @param id
 * @author Jouke Profijt
 */
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

/**
 * Adds new Region of interest with given json data
 * @param id
 * @param roi
 *
 * @author Jouke Profijt
 */
function AppendNewRoiToTable(id, roi) {
    const RowID = "image-" + id + "-roi-" + roi.id;
    $('#ImageRois-' + id).append("<tr class='image-roi-row' id='"+ RowID + "'>" +
        "<td>" + roi.id + "</td>" +
        "<td>" + roi.ph + "</td>" +
        "<td>" + roi.temp + "</td>" +
        "<td>" + roi.o2 + "</td>" +
        "<td>" + roi.co2 + "</td>" +
        "</tr>");

    //adds event listener for new row
    $('.image-roi-row').on("click", function () {
        Ready = false;
        $('.image-roi-row').removeClass("bg-primary selected");
        $(this).addClass("bg-primary selected");
        const currentTags = $('#tag-input-roi-' + id).tagsinput('items');

        $('#tag-input-roi-' + id).tagsinput('removeAll');




        selected = $(this).attr('id').replace(new RegExp("image-[0-9]+-roi-"), "");
        SelectedImage = $(this).attr('id').replace("image-", "").replace(new RegExp("-roi-[0-9]+"), "");

        $.getJSON("http://"+document.location.hostname + ":8081/api/roi/tags/get/?roi=" + selected, function (result) {
            //let remainingTags = $('#tag-input-roi-' + id).tagsinput('items');
            //checkRemain(remainingTags, id);
            for (let i in result.tags){
                $('#tag-input-roi-' + SelectedImage).tagsinput('add', result.tags[i])
            }
        });
    Ready = true;
    });
}

/**
 * event listener for tag
 * @author Jouke Profijt
 */
$(document).ready(function () {
    const TagInput = $('.TagInput');
    //on item added
    TagInput.on('itemAdded', function(event) {
        if (Ready) {
            let data = {
                id: selected,
                tag: event.item
        };


            $.ajax({
                type: "POST",
                url: "http://" + document.location.hostname + ":8081/api/roi/tags/add/",
                data: JSON.stringify(data),
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                async: true,
                success: function () {
                    //$('#ImageTags-' + id).append("<tr><td>" + $('#tag-input-' + id).val() + "</td></tr>")

                }
            })
        }
    });

    //on item remove
    TagInput.on('beforeItemRemove', function (event) {
        let tag = event.item;
        let removeData = {
            id: selected,
            tag: tag
        };

        $.ajax({
            type: "POST",
            url: "http://" + document.location.hostname + ":8081/api/roi/tags/delete/",
            data: JSON.stringify(removeData),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            async: true,
            success: function () {
                console.log("removed tag " + tag + "from roi " + selected)
            }

        })

    });

    //typeahead, should work but doesn't.
    TagInput.tagsinput({
        typeahead: {
            highlight: true,
            source: available
        }
    })
});

