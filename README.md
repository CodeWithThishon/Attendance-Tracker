📚 Attendance Tracker System

A role-based Attendance Management System built using Spring Boot, Spring Security, JPA (Hibernate) for the backend and HTML, CSS, JavaScript for the frontend.

This system allows:

Admin to manage classes, teachers, and students

Teachers to take attendance and view attendance percentages for their assigned class

Secure login with role-based access control

🚀 Features

👤 Admin

Login with Admin role

Create & manage classes

Create, update, assign teachers to classes

Create, update, and delete students

View assigned teachers and students per class


👨‍🏫 Teacher

Login with Teacher role

View assigned class

Take attendance for students

View monthly & total attendance percentage

Access limited strictly to assigned class only


🔐 Security

Spring Security authentication

Role-based authorization (ADMIN / TEACHER)


🛠️ Tech Stack

Backend

Java 21

Spring Boot

Spring Security

Spring Data JPA

Hibernate

MySQL

Lombok


Frontend

HTML

CSS (Dark Theme UI)

JavaScript (Fetch API)

No default browser login popup

Custom HTML login page
