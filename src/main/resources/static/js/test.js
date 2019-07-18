var $response = $("#response");

$("#testRegistration").click(function () {
    alert("test");
    prompt("test");
    $.ajax({
        url: "/test",
        type: "POST",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        headers: createAuthorizationTokenHeader(),
        success: function (data, textStatus, jqXHR) {
            showResponse(jqXHR.status, JSON.stringify(data));
        },
        error: function (jqXHR, textStatus, errorThrown) {
            showResponse(jqXHR.status, errorThrown);
        }
    });
});

function showResponse(statusCode, message) {
    $response
        .empty()
        .text("status code: " + statusCode + "\n-------------------------\n" + message);
}

function getJwtToken() {
    return localStorage.getItem(TOKEN_KEY);
}

function setJwtToken(token) {
    localStorage.setItem(TOKEN_KEY, token);
}

function removeJwtToken() {
    localStorage.removeItem(TOKEN_KEY);
}

function createAuthorizationTokenHeader() {
    var token = getJwtToken();
    if (token) {
        return {"Authorization": "Bearer " + token};
    } else {
        return {};
    }
}
