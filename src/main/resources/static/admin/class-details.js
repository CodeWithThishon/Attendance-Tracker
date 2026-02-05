const params = new URLSearchParams(window.location.search);
const classId = params.get("classId");

document.addEventListener("DOMContentLoaded", loadDetails);

function loadDetails() {

    // 🔹 Get class info (teacher)
    fetch(`/api/admin/class/teacher/${classId}`)
        .then(res => {
            if (!res.ok) {
                throw new Error("No teacher assigned");
            }
            return res.json();
        })
        .then(teacher => {
            document.getElementById("teacherName").innerText =
                teacher.name;
        })
        .catch(() => {
            document.getElementById("teacherName").innerText =
                "Not Assigned";
        });


    // 🔹 Get students of class
    fetch(`/api/admin/students/class/${classId}`)
        .then(res => res.json())
        .then(students => {
            const ul = document.getElementById("studentList");
            ul.innerHTML = "";

            if (students.length === 0) {
                ul.innerHTML = "<li>No students</li>";
                return;
            }

            students.forEach(s => {
                ul.innerHTML += `<li>${s.name}</li>`;
            });
        });
}

function goBack() {
    window.location.href = "manage-classes.html";
}
