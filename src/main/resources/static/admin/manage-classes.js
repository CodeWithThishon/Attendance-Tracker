document.addEventListener("DOMContentLoaded", loadClasses);

function loadClasses() {
    fetch("/api/admin/class")
        .then(res => {
            if (!res.ok) throw new Error("Unauthorized");
            return res.json();
        })
        .then(classes => {
            const table = document.getElementById("classTable");
            table.innerHTML = "";

            classes.forEach(cls => {
                table.innerHTML += `
                    <tr>
                        <td>${cls.id}</td>
                        <td>${cls.className}</td>
                        <td>
                            <button class="action-btn"
                                onclick="viewDetails(${cls.id})">
                                View Details
                            </button>

                            <button class="action-btn delete-btn"
                                onclick="deleteClass(${cls.id})">
                                Delete
                            </button>
                        </td>
                    </tr>
                `;
            });
        })
        .catch(() => alert("Please login as ADMIN"));
}

function addClass() {
    const className = document.getElementById("className").value.trim();
    if (!className) {
        alert("Class name required");
        return;
    }

    fetch("/api/admin/class", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ className })
    })
        .then(res => {
            if (!res.ok) throw new Error();
            document.getElementById("className").value = "";
            loadClasses();
        })
        .catch(() => alert("Failed to add class"));
}

function deleteClass(id) {
    if (!confirm("Delete this class?")) return;

    fetch(`/api/admin/class/${id}`, {
        method: "DELETE"
    })
        .then(() => loadClasses())
        .catch(() => alert("Delete failed"));
}

function viewDetails(classId) {
    window.location.href = `class-details.html?classId=${classId}`;
}

function goBack() {
    window.location.href = "dashboard.html";
}
