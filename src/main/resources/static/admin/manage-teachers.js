document.addEventListener("DOMContentLoaded", () => {
    loadClasses();
    loadTeachers();
});

/* ================= LOAD CLASSES ================= */
function loadClasses() {
    fetch("/api/admin/class")
        .then(res => res.json())
        .then(classes => {
            const select = document.getElementById("classSelect");
            select.innerHTML = `<option value="">--- Assign Class ---</option>`;

            classes.forEach(c => {
                const option = document.createElement("option");
                option.value = c.id;
                option.textContent = c.className;
                select.appendChild(option);
            });
        });
}

/* ================= LOAD TEACHERS ================= */
function loadTeachers() {
    fetch("/api/admin/teachers")
        .then(res => res.json())
        .then(teachers => {
            const tbody = document.getElementById("teachersTable");
            tbody.innerHTML = "";

            teachers.forEach(t => {
                const row = document.createElement("tr");

                row.innerHTML = `
                    <td>${t.name}</td>
                    <td>${t.email}</td>
                    <td>${t.schoolClass ? t.schoolClass.className : "Not Assigned"}</td>
                    <td class="actions">
                        <button class="edit" onclick="editTeacher(${t.id})">Edit</button>
                        <button class="delete" onclick="deleteTeacher(${t.id})">Delete</button>
                    </td>
                `;

                tbody.appendChild(row);
            });
        });
}

/* ================= ADD TEACHER ================= */
function addTeacher() {
    const name = document.getElementById("name").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const classId = document.getElementById("classSelect").value || null;

    if (!name || !email || !password) {
        alert("All fields are required");
        return;
    }

    const payload = {
        name,
        email,
        password,
        classId
    };

    fetch("/api/admin/teachers", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
    })
        .then(() => {
            alert("Teacher added");
            clearForm();
            loadTeachers();
        });
}

function clearForm() {
    document.getElementById("name").value = "";
    document.getElementById("email").value = "";
    document.getElementById("password").value = "";
    document.getElementById("classSelect").value = "";
}

/* ================= EDIT TEACHER ================= */
function editTeacher(id) {
    window.location.href = `edit-teacher.html?id=${id}`;
}

/* ================= DELETE TEACHER ================= */
function deleteTeacher(id) {
    if (!confirm("Are you sure you want to delete this teacher?")) return;

    fetch(`/api/admin/teachers/${id}`, {
        method: "DELETE"
    })
        .then(() => {
            alert("Teacher deleted");
            loadTeachers();
        });
}
