document.addEventListener("DOMContentLoaded", () => {
    loadYearMonth();
    loadPercentage();
});

function loadYearMonth() {
    const yearSelect = document.getElementById("year");
    const monthSelect = document.getElementById("month");

    const currentYear = new Date().getFullYear();

    for (let y = currentYear - 2; y <= currentYear + 1; y++) {
        yearSelect.innerHTML += `<option value="${y}">${y}</option>`;
    }

    for (let m = 1; m <= 12; m++) {
        monthSelect.innerHTML += `<option value="${m}">${m}</option>`;
    }

    yearSelect.value = currentYear;
    monthSelect.value = new Date().getMonth() + 1;
}

function loadPercentage() {

    const year = document.getElementById("year").value;
    const month = document.getElementById("month").value;

    fetch(`/api/attendance/teacher/percentage?year=${year}&month=${month}`)
        .then(res => {
            if (!res.ok) throw new Error("Unauthorized");
            return res.json();
        })
        .then(data => {

            const table = document.getElementById("percentageTable");
            table.innerHTML = "";

            if (data.length === 0) {
                table.innerHTML = `
                    <tr>
                        <td colspan="4">No students assigned</td>
                    </tr>
                `;
                return;
            }

            data.forEach(s => {
                table.innerHTML += `
                    <tr>
                        <td>${s.rollNo}</td>
                        <td>${s.name}</td>
                        <td>${s.monthlyPercentage.toFixed(2)}%</td>
                        <td>${s.totalPercentage.toFixed(2)}%</td>
                    </tr>
                `;
            });
        })
        .catch(() => {
            alert("Failed to load attendance percentage");
        });
}

function goBack() {
    window.location.href = "/teacher/dashboard.html";
}
