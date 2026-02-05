function openPage(apiUrl) {
    alert("Connect this with frontend page later: " + apiUrl);
}

function logout() {

    fetch("/logout", {
        method: "POST"
    })
    .then(() => {
        window.location.href = "/login.html";
    });
}
