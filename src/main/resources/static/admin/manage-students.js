let currentPage = 0;
const pageSize = 10;

document.addEventListener("DOMContentLoaded", () => {
    loadClasses();
    loadStudents(0);
});

function loadClasses() {
    fetch("/api/admin/class")
        .then(res => res.json())
        .then(classes => {
            const classSelect = document.getElementById("classSelect");
            const classFilter = document.getElementById("classFilter");

            classes.forEach(c => {
                let opt1 = new Option(c.className, c.id);
                let opt2 = new Option(c.className, c.id);

                classSelect.add(opt1);
                classFilter.add(opt2);
            });
        });
}

/* ➕ Add Student */
function addStudent() {
    const name = document.getElementById("studentName").value;
    const rollNo = document.getElementById("rollNo").value;
    const classId = document.getElementById("classSelect").value;

    if (!name || !rollNo || !classId) {
        alert("All fields are required");
        return;
    }

    fetch("/api/admin/students", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            name,
            rollNo,
            classId
        })
    })
        .then(res => {
            if (!res.ok) throw new Error("Failed");
            return res.json();
        })
        .then(() => {
            document.getElementById("studentName").value = "";
            document.getElementById("rollNo").value = "";
            document.getElementById("classSelect").value = "";
            loadStudents(0);
        })
        .catch(err => alert("Error adding student"));
}

/* 📋 Load Students */
function loadStudents(page) {
    currentPage = page;

    const search = document.getElementById("searchInput").value;
    const classId = document.getElementById("classFilter").value;

    let url = `/api/admin/students?page=${page}&size=${pageSize}`;
    if (search) url += `&search=${search}`;
    if (classId) url += `&classId=${classId}`;

    fetch(url)
        .then(res => res.json())
        .then(data => {
            const tbody = document.getElementById("studentsTable");
            tbody.innerHTML = "";

            data.content.forEach(s => {
                tbody.innerHTML += `
                    <tr>
                        <td>${s.name}</td>
                        <td>${s.rollNo}</td>
                        <td>${s.schoolClass.className}</td>
                        <td>
                            <button class="action-btn edit-btn"
                                onclick="editStudent(${s.id})">Edit</button>
                            <button class="action-btn delete-btn"
                                onclick="deleteStudent(${s.id})">Delete</button>
                        </td>
                    </tr>
                `;
            });

            document.getElementById("pageInfo").innerText =
                `Page ${data.number + 1} of ${data.totalPages}`;
        });
}

function nextPage() {
    loadStudents(currentPage + 1);
}

function prevPage() {
    if (currentPage > 0) {
        loadStudents(currentPage - 1);
    }
}

function deleteStudent(id) {
    if (!confirm("Delete this student?")) return;

    fetch(`/api/admin/students/${id}`, { method: "DELETE" })
        .then(() => loadStudents(currentPage));
}

function editStudent(id) {
    window.location.href = `edit-student.html?id=${id}`;
}
