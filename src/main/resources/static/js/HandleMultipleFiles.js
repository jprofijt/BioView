/**
 * if the user wants to add more files they click add more...
 *
 * code mostly from StackOverflow.
 * @author Jouke Profijt
 */
$(document).ready(function() {
    $('#addFile').click(function() {
        var fileIndex = $('#fileTable tr').children().length - 1;
        $('#fileTable').append(
            '<tr><td>'+
            '	<input type="file" name="files['+ fileIndex +']" />'+
            '</td></tr>');
    });

});







