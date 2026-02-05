const params = new URLSearchParams(window.location.search);
const studentId = params.get("id");

document.addEventListener("DOMContentLoaded", () => {
    loadClasses();
    loadStudent();
});

function loadClasses() {
    fetch("/api/admin/class")
        .then(res => res.json())
        .then(classes => {
            const select = document.getElementById("classSelect");
            classes.forEach(c => {
                const option = new Option(c.className, c.id);
                select.add(option);
            });
        });
}

function loadStudent() {
    fetch(`/api/admin/students/${studentId}`)
        .then(res => res.json())
        .then(student => {
            document.getElementById("studentName").value = student.name;
            document.getElementById("rollNo").value = student.rollNo;
            document.getElementById("classSelect").value =
                student.schoolClass.id;
        });
}

function updateStudent() {
    const name = document.getElementById("studentName").value;
    const rollNo = document.getElementById("rollNo").value;
    const classId = document.getElementById("classSelect").value;

    if (!name || !rollNo || !classId) {
        alert("All fields are required");
        return;
    }

    fetch(`/api/admin/students/${studentId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            name,
            rollNo,
            classId
        })
    })
        .then(res => {
            if (!res.ok) throw new Error();
            return res.json();
        })
        .then(() => {
            alert("Student updated successfully");
            window.location.href = "manage-students.html";
        })
        .catch(() => alert("Update failed"));
}

function goBack() {
    window.location.href = "manage-students.html";
}
