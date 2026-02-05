const params = new URLSearchParams(window.location.search);
const teacherId = params.get("id");

document.addEventListener("DOMContentLoaded", () => {
    loadClasses();
    loadTeacher();
});

/* ================= LOAD TEACHER ================= */
function loadTeacher() {
    fetch(`/api/admin/teachers/${teacherId}`)
        .then(res => res.json())
        .then(t => {
            document.getElementById("name").value = t.name;
            document.getElementById("email").value = t.email;

            if (t.schoolClass) {
                document.getElementById("classSelect").value = t.schoolClass.id;
            }
        });
}

/* ================= LOAD CLASSES ================= */
function loadClasses() {
    fetch("/api/admin/class")
        .then(res => res.json())
        .then(classes => {
            const select = document.getElementById("classSelect");

            classes.forEach(c => {
                const option = document.createElement("option");
                option.value = c.id;
                option.textContent = c.className;
                select.appendChild(option);
            });
        });
}

/* ================= UPDATE TEACHER ================= */
function updateTeacher() {
    const name = document.getElementById("name").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const classIdValue = document.getElementById("classSelect").value;

    if (!name || !email) {
        alert("Name and email are required");
        return;
    }

    const payload = {
        name,
        email,
        password: password ? password : null,
        classId: classIdValue ? classIdValue : null
    };

    fetch(`/api/admin/teachers/${teacherId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
    })
        .then(res => {
            if (!res.ok) return res.text().then(t => { throw new Error(t); });
            alert("Teacher updated successfully");
            window.location.href = "manage-teachers.html";
        })
        .catch(err => alert(err.message));
}

/* ================= BACK ================= */
function goBack() {
    window.location.href = "manage-teachers.html";
}
