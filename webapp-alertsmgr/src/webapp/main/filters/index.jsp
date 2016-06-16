<html>

<head><title>Alert Filters Manager</title></head>
<script language="JavaScript" src="/includes/js/jq/jquery-1.4.4.min.js"></script>
<script language="JavaScript">

function createFilter() {
    $("#new_status").html("Adding ...");
    var url = '/alertsapi/json/filter/0/' + encodeURIComponent($("#new_name").val()) + '/' + encodeURIComponent($("#new_model").val()) + '/' + encodeURIComponent($("#new_definition").val()) + '/';
    $.ajax({ 
        type: "POST",
        url: url, 
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            $("#new_status").html("FAILED");
        },
        success: function() {
            $("#new_status").html("Added");
            listRefresh();
        }
    });
    url = null;
}

function deleteFilter(id) {
    $.ajax({ 
        type: "DELETE",
        url: "/alertsapi/json/filter/" + id, 
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            $("#delete-"+id).html("Delete Failed (" + deleteFilterHTML(id, "Try Again") + ")");
        },
        success: function() {
            $("#delete-"+id).html("Deleted");
            listRefresh();
        }
    });
}

function deleteFilterHTML(id, text) {
    return "<a href=\"#delete-" + id + "\" onclick=\"if (confirm('Are You Sure?')) { deleteFilter(" + id + "); } return false;\">" + text + "</a>";
}

function listFilters() {
    $.ajax({ 
        type: "GET",
        url: '/alertsapi/json/filters/all', 
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            $("#list").html($("#list").html() + " FAILED (<a href=\"#listFilters\" onclick=\"listRefresh(); return false;\">Try Again</a>)");
        },
        success: function(data) {
            var html = "<table border=\"1\"><tr><th>ID</th><th>Name</th><th>Model</th><th>Description</th><th>Action</th></tr>"; 
            if (data.length <= 0) {
                html = html + "<tr><td colspan=\"5\">No Data Found</td></tr>";
            }
            else {
                for(i=0; i<data.length; i++) {
                    html = html 
                         + "<tr>"
                     + "<td>" + data[i].key + "</td>"
                     + "<td>" + data[i].name + "</td>"
                     + "<td>" + data[i].model + "</td>"
                     + "<td>" + data[i].description + "</td>"
                     + "<td id=\"delete-" + data[i].key + "\">" + deleteFilterHTML(data[i].key, "Delete") + "</td>"
                     + "</tr>";
                }
            }
            html = html + "</table><a href=\"#listFilters\" onclick=\"listRefresh(); return false;\">Refresh List</a><br>";
            $("#list").html(html);
            //alert('Load was performed.');
        }
    });
}

function listRefresh() {
    $("#list").html("List of filters refreshing ...");
    listFilters();
}

//function updateFilter() {
//    var url = '/alertsapi/json/filter/' + encodeURIComponent($("#new_name").val()) + '/' + encodeURIComponent($("#new_definition").val()) + '/';
//    $.post(url, function(data) {
//        list();
//    });
//    url = null;
//}

$(document).ready(function() {
    listRefresh();
});

</script>

<body>


<form method="POST" action="" onsubmit="createFilter(); return false;">
Create Alert Filter<br/>
<table border="0">
 <tr><td>Name</td><td><input name="new_name" id="new_name" value="" /></td><td></td></tr>
 <tr><td>Model</td><td><input name="new_model" id="new_model" value="transporation" /></td><td></td></tr>
 <tr><td>Definition</td><td><input name="new_definition" id="new_definition" value="" /></td><td>&nbsp;Example: {"D_eventType":{"eventType":"= 'Flight'"},"geoList":[{"geoCity":"LIKE 'Dallas*'"}]}</td></tr>
</table>
<input type="submit" name="submit" value=" Add Alert "> <span id="new_status"></span>
</form>
<br/>&nbsp;
__________________________________________________________________________<br/>
<br/>&nbsp;
Filters
<span id="list"></span>

</body>

</html>
