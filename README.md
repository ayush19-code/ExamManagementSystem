# 📝 Exam Management System

A Java-based desktop application designed to manage and conduct examinations digitally. The system provides separate interfaces for **Administrators** and **Students**, allowing admins to manage questions and results while students can attempt exams and view their scores.

The application is built using **Java Swing** for the graphical user interface, **JDBC** for database connectivity, and **MySQL** for data storage.

## ✨ Features

### 👨‍💼 Admin

- Secure Admin Login
- Admin Dashboard
- Add New MCQ Questions
- View Question Bank
- View Student Results
- Manage Exam Content
- Logout Functionality

### 👨‍🎓 Student

- Student Login
- Student Dashboard
- Select Exam Subject
- Attempt Multiple Choice Questions
- Navigate Between Questions
- Submit Exam
- Automatic Score Calculation
- View Previous Results
- Automatic Exam Report Generation

## 🛠️ Tech Stack

- **Java**
- **Java Swing**
- **JDBC**
- **MySQL**
- **Object-Oriented Programming (OOP)**

## 📁 Project Structure

```text
ExamManagementSystem/
│
├── src/
│   ├── dao/
│   │   ├── ExamDAO.java
│   │   └── UserDAO.java
│   │
│   ├── db/
│   │   └── DBConnection.java
│   │
│   ├── gui/
│   │   ├── AdminDashboard.java
│   │   ├── ExamFrame.java
│   │   ├── LoginFrame.java
│   │   ├── ResultFrame.java
│   │   └── StudentDashboard.java
│   │
│   ├── model/
│   │   ├── Admin.java
│   │   ├── Question.java
│   │   ├── Student.java
│   │   └── User.java
│   │
│   ├── util/
│   │   └── ReportGenerator.java
│   │
│   └── Main.java
│
├── lib/
│   └── mysql-connector-j-9.7.0.jar
│
├── bin/
├── run.bat
└── README.md
```

## ⚙️ Prerequisites

Before running the project, make sure you have:

- Java JDK installed
- MySQL Server installed and running
- MySQL Connector/J
- A Java IDE such as IntelliJ IDEA, Eclipse, or VS Code (optional)

## 🗄️ Database Configuration

The application uses a MySQL database named:

```text
exam_db
```

Database connection settings can be configured inside:

```text
src/db/DBConnection.java
```

Update the following values according to your MySQL configuration:

```java
private static final String URL = "jdbc:mysql://localhost:3306/exam_db";
private static final String USER = "root";
private static final String PASS = "your_password";
```

> Do not upload your real database password to a public GitHub repository.

## 🚀 How to Run

### Using `run.bat`

On Windows, make sure Java and MySQL are properly configured.

Then run:

```text
run.bat
```

The script compiles the Java source files and starts the application.

### Run Manually

Compile the project using the MySQL Connector/J library and then run the `Main` class.

The application will open the login interface where users can access the system as an Admin or Student.

## 📊 Exam Report

After completing an exam, the application can automatically generate a text-based report containing:

- Student Name
- Subject
- Score
- Percentage
- Pass/Fail Result
- Date and Time

Example:

```text
============================
       EXAM REPORT
============================
Student  : Student Name
Subject  : Java
Score    : 8/10
Percent  : 80%
Result   : PASS
============================
```

## 🎯 Project Objectives

This project demonstrates practical implementation of:

- Object-Oriented Programming in Java
- Java Swing GUI Development
- JDBC Database Connectivity
- MySQL Database Management
- DAO (Data Access Object) Pattern
- User Authentication
- Role-Based Dashboards
- Exam and Result Management
- File-Based Report Generation

## 🔮 Future Improvements

- Add exam timer functionality
- Password encryption
- Student registration system
- Edit and delete questions
- Randomized questions
- Improved result analytics
- PDF report generation
- Online/web-based version

## 👨‍💻 Developer

Developed by **Ayush Saini**

## 📄 License

This project was developed for educational and academic purposes.
