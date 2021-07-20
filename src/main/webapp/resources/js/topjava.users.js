const userAjaxUrl = "admin/users/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: userAjaxUrl
};

function setEnabled(row) {
    let id = row.closest('tr').attr("id");
    let enable = row[0].checked;
    $.ajax({
        type: "POST",
        url: ctx.ajaxUrl + id,
        data: "enabled=" + enable
    }).done(function () {
        //alert("Успешно обновлен");
        row.closest('tr').attr("data-userEnable", enable);
        successNoty("Enabled for user: " + enable);
    });
}

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "name"
                },
                {
                    "data": "email"
                },
                {
                    "data": "roles"
                },
                {
                    "data": "enabled"
                },
                {
                    "data": "registered"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "asc"
                ]
            ]
        })
    );
});