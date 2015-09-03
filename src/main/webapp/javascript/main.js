// Add the $() function
$ = jQuery;
// Now you can use it

$(document).ready(function () {
});

function manageExpandCollapseFirstPanel() {
    if (document.getElementById('top30DevicesPanel_content').style.height === '100%') {
        document.getElementById('top30DevicesPanel_content').style.height = '300px';
        $('#expandFirstPanel').children().eq(1).html('Expand');
        $('#expandFirstPanel').children().eq(0).removeClass('ui-icon-circle-triangle-n').addClass('ui-icon-circle-triangle-s');
        
    } else {
        document.getElementById('top30DevicesPanel_content').style.height = '100%';
        $('#expandFirstPanel').children().eq(1).html('Collapse');
        $('#expandFirstPanel').children().eq(0).removeClass('ui-icon-circle-triangle-s').addClass('ui-icon-circle-triangle-n');
    }
    return false;
}

function manageExpandCollapseSecondPanel() {
    if (document.getElementById('top30EmergingDevicesPanel_content').style.height === '100%') {
        document.getElementById('top30EmergingDevicesPanel_content').style.height = '300px';
        $('#expandSecondPanel').children().eq(1).html('Expand');
        $('#expandSecondPanel').children().eq(0).removeClass('ui-icon-circle-triangle-n').addClass('ui-icon-circle-triangle-s');
        
    } else {
        document.getElementById('top30EmergingDevicesPanel_content').style.height = '100%';
        $('#expandSecondPanel').children().eq(1).html('Collapse');
        $('#expandSecondPanel').children().eq(0).removeClass('ui-icon-circle-triangle-s').addClass('ui-icon-circle-triangle-n');
    }
    return false;
}

