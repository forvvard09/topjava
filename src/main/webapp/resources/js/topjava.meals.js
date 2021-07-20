const mealsAjaxUrl = "profile/meals/";

const ctx = {
    ajaxUrl: mealsAjaxUrl
};

function clearFilter() {
    $('#meals-filter')[0].reset();
    filter();
    localStorage.removeItem("dataFilter");
}

function filter() {
    $.ajax({
        type: "GET",
        url: ctx.ajaxUrl + 'filter',
        data: $('#meals-filter').serialize()
    }).done(function (data) {
        //localStorage.setItem("dataFilter", "true");
        ctx.datatableApi.clear().rows.add(data).draw();
    });
}

function checkFilterForm() {
    let result = false;
    // to do while
    $('#meals-filter input').each(function() {
        if ($(this).val() !== "") {
            result = true;
            return;
        }
    });
    return result;
}


$(function () {
    makeEditable(
        $("#meals-datatable").DataTable({
            "paging": false,
            "info": true,
            "filter": false,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
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
        })
    );
});