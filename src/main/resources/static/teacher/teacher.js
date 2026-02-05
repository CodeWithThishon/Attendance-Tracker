document.addEventListener("DOMContentLoaded", loadTeacherInfo);

function loadTeacherInfo() {
    fetch("/api/me")
        .then(res => {
            if (!res.ok) throw new Error("Unauthorized");
            return res.json();
        })
        .then(data => {

            const classInfoDiv = document.getElementById("classInfo");
            const attendanceBtn = document.getElementById("attendanceBtn");
            const percentageBtn = document.getElementById("percentageBtn");

            const hasClass =
                data.teacher &&
                data.teacher.schoolClass &&
                data.teacher.schoolClass.className;

            if (hasClass) {
                classInfoDiv.innerText =
                    "Assigned Class: " + data.teacher.schoolClass.className;

                attendanceBtn.disabled = false;
                percentageBtn.disabled = false;

            } else {
                classInfoDiv.innerText = "You have no assigned class";

                attendanceBtn.disabled = true;
                percentageBtn.disabled = true;
            }
        })
        .catch(() => {
            document.getElementById("classInfo").innerText =
                "Unable to load class info";
        });
}

/* 🔹 NAVIGATION */
function goTakeAttendance() {
    window.location.href = "take-attendance.html";
}

function goPercentage() {
    window.location.href = "attendance-percentage.html";
}

function logout() {
    fetch("/logout", { method: "POST" })
        .then(() => window.location.href = "/login.html");
}
