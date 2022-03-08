/**
 * Ajax for application
 */
var maxFileSize = 5242880; //5MB
var numOfSteps = 5;
var app = "app";

$('input[name="fileupload"]').click(function (ev) {
    this.value = null;
});

//
$('input[name="fileupload"]').change(function (ev) {
    try {
        if (ev.target.files[0].name.length > 5) {
            document.getElementById("button1").disabled = false;
            document.getElementById("fileupload").disabled = true;
            deleteAll(numOfSteps);
        }
    } catch (error) { }
});

//
function deleteAll(position) {
    var i, x;
    for (i = 1; i <= position; i++) {
        try {
            x = document.getElementById("errorAtPosition" + i);
            if (window.getComputedStyle(x).display === "block") {
                x.style.display = "none";
            }
        } catch (error) { }

        try {
            x = document.getElementById("successAtPosition" + i);
            if (window.getComputedStyle(x).display === "block") {
                x.style.display = "none";
            }
        } catch (error) { }
    }

    try {
        x = document.getElementById("successDiv");
        if (window.getComputedStyle(x).display === "block") {
            x.style.visibility = "hidden";
        }
    } catch (error) { }

    try {
        x = document.getElementById("errorDiv");
        if (window.getComputedStyle(x).display === "block") {
            x.style.display = "none";
        }
    } catch (error) { }
}

/*
class responseObj {
    constructor(status, position, positionName, message) {
        this.status = status;
        this.position = position;
        this.positionName = positionName;
        this.message = message;
    }
}
*/
var responseObj = /** @class */ (function () {
    function responseObj(status, position, positionName, message) {
        this.status = status;
        this.position = position;
        this.positionName = positionName;
        this.message = message;
    }
    return responseObj;
})();

//
function errorAtPosition(response) {
    var x;

    try {
        x = document.getElementById("successDiv");
        if (window.getComputedStyle(x).display === "block") {
            document.getElementById("successMessageDiv").innerHTML = "";
            document.getElementById("successDiv").style.display = "none";
        }
    } catch (error) { }

    document.getElementById("errorDiv").style.display = "block";
    document.getElementById("errorMessageDiv").innerHTML = response.message;
    document.getElementById("errorAtPosition" + response.position).style.display = "block";

    try {
        x = document.getElementById("successAtPosition" + response.position);
        if (window.getComputedStyle(x).display === "block") {
            document.getElementById("successAtPosition" + response.position).innerHTML = "";
            document.getElementById("successAtPosition" + response.position).style.display = "none";
        }
    } catch (error) { }

    document.getElementById("button" + response.position).disabled = true;
    document.getElementById("fileupload").disabled = false;
}

//
function successAtPosition(response) {
    var sD = document.getElementById("successDiv");
    var x;

    if (window.getComputedStyle(sD).visibility === "hidden") {
        document.getElementById("successDiv").style.visibility = "visible";
    }

    document.getElementById("successDiv").style.display = "block";
    document.getElementById("successMessageDiv").innerHTML = response.message;

    try {
        x = document.getElementById("errorDiv");
        if (window.getComputedStyle(x).display === "block") {
            document.getElementById("errorDiv").style.display = "none";
        }
    } catch (error) { }

    try {
        document.getElementById("successAtPosition" + response.position).style.display = "block";
    } catch (error) { }

    try {
        x = document.getElementById("errorAtPosition" + response.position);
        if (window.getComputedStyle(x).display === "block") {
            document.getElementById("errorAtPosition" + response.position).style.display = "none";
        }
    } catch (error) { }
    
    document.getElementById("button" + response.position).disabled = true;
    if (response.position < numOfSteps) {
        document.getElementById("button" + (response.position + 1)).disabled = false;
    }
    if (response.position == numOfSteps) {
        document.getElementById("fileupload").disabled = false;
    }
}

//
function handleResult(response) {
    //valid 0
    if (response.status === 0) {
        successAtPosition(response);
    }

    //invalid 1
    if (response.status === 1) {
        errorAtPosition(response);
    }
}

//
function uploadFile() {
    var response = new responseObj(0, 1, "");
    var formData = new FormData();
    formData.append("file", fileupload.files[0]);

    if (!fileupload.files[0]) {
        response = new responseObj(1, 1, "upload", "Please select a file before clicking Upload!");
        handleResult(response);
        return;
    } else {
 
        if (fileupload.files[0].size > maxFileSize) {
            response = new responseObj(1, 1, "upload", "File is to large!");
            handleResult(response);
            return;
        }
    }

    var typeHtml = document.getElementById("typeAjax").value;
    var pageHtml = document.getElementById("pageAjax").value;
    formData.append("type", typeHtml);
    formData.append("page", pageHtml);

    $.ajax({
        url: `/${app}/upload-file-ajax`,
        type: "post",
        data: formData,
        enctype: "multipart/form-data",
        processData: false,
        contentType: false,
        cache: false,
        success: function (result) {
            handleResult(new responseObj(result.status, 1, "upload", result.message));
        },
        error: function (err) {
            handleResult(new responseObj(1, 1, "upload", "Something went wrong on server side!<br> Error: " + JSON.stringify(err)));
        },
    });
    return;
}

//
$(document).ready(function () {
    //button2, check file
    $("#button2").click(function () {
        var typeHtml = document.getElementById("typeAjax").value;
        var pageHtml = document.getElementById("pageAjax").value;

        $.ajax({
            type: "post",
            url: `/${app}/check-file-ajax`,
            data: {
                type: typeHtml,
                page: pageHtml,
            },
            success: function (result) {
                handleResult(new responseObj(result.status, 2, "check file", result.message));
                return;
            },
            error: function (err) {
                handleResult(new responseObj(1, 2, "check file", "Something went wrong on server side!<br> Error: " + JSON.stringify(err)));
                return;
            },
        });
    });

    //button3, convert to csv
    $("#button3").click(function () {
        var typeHtml = document.getElementById("typeAjax").value;
        var pageHtml = document.getElementById("pageAjax").value;

        $.ajax({
            type: "post",
            url: `/${app}/convert-to-csv-ajax`,
            data: {
                type: typeHtml,
                page: pageHtml,
            },
            success: function (result) {
                handleResult(new responseObj(result.status, 3, "convert to csv", result.message));
                return;
            },
            error: function (err) {
                handleResult(new responseObj(1, 3, "convert to csv", "Something went wrong on server side!<br> Error: " + JSON.stringify(err)));
                return;
            },
        });
    });

    //button4, send to server
    $("#button4").click(function () {
        var typeHtml = document.getElementById("typeAjax").value;
        var pageHtml = document.getElementById("pageAjax").value;

        $.ajax({
            type: "post",
            url: `/${app}/send-to-server-ajax`,
            data: {
                type: typeHtml,
                page: pageHtml,
            },
            success: function (result) {
                handleResult(new responseObj(result.status, 4, "send to server", result.message));
                return;
            },
            error: function (err) {
                handleResult(new responseObj(1, 4, "send to server", "Something went wrong on server side!<br> Error: " + JSON.stringify(err)));
                return;
            },
        });
    });

    //button5, call stored procedure
    $("#button5").click(function () {
        var typeHtml = document.getElementById("typeAjax").value;
        var pageHtml = document.getElementById("pageAjax").value;

        $.ajax({
            type: "post",
            url: `/${app}/call-first-stored-procedure-ajax`,
            data: {
                type: typeHtml,
                page: pageHtml,
            },
            success: function (result) {
                handleResult(new responseObj(result.status, 5, "call stored procedure", result.message));
                return;
            },
            error: function (err) {
                handleResult(new responseObj(1, 5, "call stored procedure", "Something went wrong on server side!<br> Error: " + JSON.stringify(err)));
                return;
            },
        });
    });
});
