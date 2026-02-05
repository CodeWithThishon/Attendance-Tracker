let attendanceMap = {}; // studentId -> PRESENT / ABSENT
let teacherId = null;

document.addEventListener("DOMContentLoaded", () => {
    loadTeacher();
    loadStudents();
    document.getElementById("attendanceDate").valueAsDate = new Date();
});

/* 🔹 Get logged-in teacher info */
function loadTeacher() {
    fetch("/api/me")
        .then(res => res.json())
        .then(data => {
            teacherId = data.teacher.id;
        });
}

/* 🔹 Load assigned students */
function loadStudents() {
    fetch("/api/attendance/students")
        .then(res => res.json())
        .then(students => {
            const tbody = document.getElementById("studentsTable");
            tbody.innerHTML = "";

            students.forEach(s => {
                attendanceMap[s.id] = "PRESENT"; // default

                const tr = document.createElement("tr");
                tr.innerHTML = `
                    <td>${s.rollNo}</td>
                    <td>${s.name}</td>
                    <td>
                        <button class="present"
                            onclick="markStatus(${s.id}, 'PRESENT', this)">
                            Present
                        </button>
                        <button class="absent"
                            onclick="markStatus(${s.id}, 'ABSENT', this)">
                            Absent
                        </button>
                    </td>
                `;
                tbody.appendChild(tr);
            });
        });
}

function markStatus(studentId, status, btn) {
    attendanceMap[studentId] = status;

    const buttons = btn.parentElement.querySelectorAll("button");
    buttons.forEach(b => b.style.opacity = "0.4");
    btn.style.opacity = "1";
}

/* 🔹 Submit attendance */
function submitAttendance() {
    const date = document.getElementById("attendanceDate").value;
    if (!date) {
        alert("Please select date");
        return;
    }

    const attendanceList = Object.keys(attendanceMap).map(studentId => ({
        studentId: studentId,
        status: attendanceMap[studentId]
    }));

    const payload = {
        date: date,
        records: attendanceList
    };

    fetch(`/api/attendance/teacher/${teacherId}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
    })
        .then(res => {
            if (!res.ok) throw new Error("Failed");
            alert("✅ Attendance saved successfully");
        })
        .catch(() => alert("❌ Error saving attendance"));
}

function goBack() {
    window.location.href = "dashboard.html";
}
