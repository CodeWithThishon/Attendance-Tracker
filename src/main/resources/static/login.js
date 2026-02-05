const params = new URLSearchParams(window.location.search);

if (params.get("error") === "admin") {
    alert("You are not Admin!");
}

if (params.get("error") === "teacher") {
    alert("You are not Teacher!");
}

if (params.get("error") === "invalid") {
    alert("Invalid Email or Password!");
}
