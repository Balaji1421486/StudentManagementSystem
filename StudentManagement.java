package studentmanagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class StudentManagement {

    static final String URL =
            "jdbc:mysql://localhost:3306/student_management";

    static final String USER = "root";

    static final String PASSWORD = "YOUR_MYSQL_PASSWORD";

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con =
                    DriverManager.getConnection(URL, USER, PASSWORD);

            System.out.println("Database Connected Successfully!");

            int choice;

            do {

                System.out.println("\n===== STUDENT MANAGEMENT SYSTEM =====");
                System.out.println("1. Add Student");
                System.out.println("2. View Students");
                System.out.println("3. Update Student");
                System.out.println("4. Delete Student");
                System.out.println("5. View Courses");
                System.out.println("6. Add Enrollment");
                System.out.println("7. View Enrollments");
                System.out.println("8. Add Attendance");
                System.out.println("9. View Attendance");
                System.out.println("10. Add Marks");
                System.out.println("11. View Marks");
                System.out.println("12. Result Generation");
                System.out.println("13. Exit");

                System.out.print("Enter your choice: ");

                choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {

                case 1:
                    addStudent(con);
                    break;

                case 2:
                    viewStudents(con);
                    break;

                case 3:
                    updateStudent(con);
                    break;

                case 4:
                    deleteStudent(con);
                    break;

                case 5:
                    viewCourses(con);
                    break;

                case 6:
                    addEnrollment(con);
                    break;

                case 7:
                    viewEnrollments(con);
                    break;

                case 8:
                    addAttendance(con);
                    break;

                case 9:
                    viewAttendance(con);
                    break;

                case 10:
                    addMarks(con);
                    break;

                case 11:
                    viewMarks(con);
                    break;

                case 12:
                    generateResult(con);
                    break;

                case 13:
                    System.out.println("Program Ended.");
                    break;

                default:
                    System.out.println("Invalid choice.");
                }

            } while (choice != 13);

            con.close();
            sc.close();

        } catch (Exception e) {

            System.out.println("Error: " + e.getMessage());
        }
    }

    // 1. Add Student
    static void addStudent(Connection con) {

        try {

            System.out.print("Enter Student Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Email: ");
            String email = sc.nextLine();

            System.out.print("Enter Phone: ");
            String phone = sc.nextLine();

            System.out.print("Enter Course: ");
            String course = sc.nextLine();

            String sql =
                    "INSERT INTO students " +
                    "(student_name, email, phone, course) " +
                    "VALUES (?, ?, ?, ?)";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, course);

            ps.executeUpdate();

            System.out.println("Student added successfully!");

            ps.close();

        } catch (Exception e) {

            System.out.println("Error: " + e.getMessage());
        }
    }

    // 2. View Students
    static void viewStudents(Connection con) {

        try {

            String sql = "SELECT * FROM students";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println("\n===== STUDENT DETAILS =====");

            while (rs.next()) {

                System.out.println("ID: "
                        + rs.getInt("student_id"));

                System.out.println("Name: "
                        + rs.getString("student_name"));

                System.out.println("Email: "
                        + rs.getString("email"));

                System.out.println("Phone: "
                        + rs.getString("phone"));

                System.out.println("Course: "
                        + rs.getString("course"));

                System.out.println("--------------------------");
            }

            rs.close();
            ps.close();

        } catch (Exception e) {

            System.out.println("Error: " + e.getMessage());
        }
    }

    // 3. Update Student
    static void updateStudent(Connection con) {

        try {

            System.out.print("Enter Student ID: ");
            int id = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter New Name: ");
            String name = sc.nextLine();

            System.out.print("Enter New Email: ");
            String email = sc.nextLine();

            System.out.print("Enter New Phone: ");
            String phone = sc.nextLine();

            System.out.print("Enter New Course: ");
            String course = sc.nextLine();

            String sql =
                    "UPDATE students SET student_name=?, " +
                    "email=?, phone=?, course=? " +
                    "WHERE student_id=?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, course);
            ps.setInt(5, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Student updated successfully!");
            } else {
                System.out.println("Student ID not found.");
            }

            ps.close();

        } catch (Exception e) {

            System.out.println("Error: " + e.getMessage());
        }
    }

    // 4. Delete Student
    static void deleteStudent(Connection con) {

        try {

            System.out.print("Enter Student ID to delete: ");
            int id = sc.nextInt();
            sc.nextLine();

            con.setAutoCommit(false);

            String sql1 =
                    "DELETE FROM marks WHERE student_id=?";

            PreparedStatement ps1 =
                    con.prepareStatement(sql1);

            ps1.setInt(1, id);
            ps1.executeUpdate();
            ps1.close();

            String sql2 =
                    "DELETE FROM attendance WHERE student_id=?";

            PreparedStatement ps2 =
                    con.prepareStatement(sql2);

            ps2.setInt(1, id);
            ps2.executeUpdate();
            ps2.close();

            String sql3 =
                    "DELETE FROM enrollments WHERE student_id=?";

            PreparedStatement ps3 =
                    con.prepareStatement(sql3);

            ps3.setInt(1, id);
            ps3.executeUpdate();
            ps3.close();

            String sql4 =
                    "DELETE FROM students WHERE student_id=?";

            PreparedStatement ps4 =
                    con.prepareStatement(sql4);

            ps4.setInt(1, id);

            int rows = ps4.executeUpdate();

            if (rows > 0) {

                con.commit();

                System.out.println(
                        "Student deleted successfully!");

            } else {

                con.rollback();

                System.out.println(
                        "Student ID not found.");
            }

            ps4.close();

            con.setAutoCommit(true);

        } catch (Exception e) {

            try {
                con.rollback();
                con.setAutoCommit(true);
            } catch (Exception ex) {
                System.out.println("Rollback Error.");
            }

            System.out.println("Error: " + e.getMessage());
        }
    }

    // 5. View Courses
    static void viewCourses(Connection con) {

        try {

            String sql = "SELECT * FROM courses";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println("\n===== COURSE DETAILS =====");

            while (rs.next()) {

                System.out.println("Course ID: "
                        + rs.getInt("course_id"));

                System.out.println("Course Name: "
                        + rs.getString("course_name"));

                System.out.println("--------------------------");
            }

            rs.close();
            ps.close();

        } catch (Exception e) {

            System.out.println("Error: " + e.getMessage());
        }
    }

    // 6. Add Enrollment
    static void addEnrollment(Connection con) {

        try {

            System.out.print("Enter Student ID: ");
            int studentId = sc.nextInt();

            System.out.print("Enter Course ID: ");
            int courseId = sc.nextInt();
            sc.nextLine();

            String sql =
                    "INSERT INTO enrollments " +
                    "(student_id, course_id, enrollment_date) " +
                    "VALUES (?, ?, CURDATE())";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, studentId);
            ps.setInt(2, courseId);

            ps.executeUpdate();

            System.out.println(
                    "Student enrolled successfully!");

            ps.close();

        } catch (Exception e) {

            System.out.println("Error: " + e.getMessage());
        }
    }

    // 7. View Enrollments
    static void viewEnrollments(Connection con) {

        try {

            String sql =
                    "SELECT e.enrollment_id, s.student_name, " +
                    "c.course_name, e.enrollment_date " +
                    "FROM enrollments e " +
                    "JOIN students s " +
                    "ON e.student_id = s.student_id " +
                    "JOIN courses c " +
                    "ON e.course_id = c.course_id";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println(
                    "\n===== ENROLLMENT DETAILS =====");

            while (rs.next()) {

                System.out.println("Enrollment ID: "
                        + rs.getInt("enrollment_id"));

                System.out.println("Student Name: "
                        + rs.getString("student_name"));

                System.out.println("Course Name: "
                        + rs.getString("course_name"));

                System.out.println("Enrollment Date: "
                        + rs.getDate("enrollment_date"));

                System.out.println("--------------------------");
            }

            rs.close();
            ps.close();

        } catch (Exception e) {

            System.out.println("Error: " + e.getMessage());
        }
    }

    // 8. Add Attendance
    static void addAttendance(Connection con) {

        try {

            System.out.print("Enter Student ID: ");
            int studentId = sc.nextInt();
            sc.nextLine();

            System.out.print(
                    "Enter Status (Present/Absent): ");

            String status = sc.nextLine();

            String sql =
                    "INSERT INTO attendance " +
                    "(student_id, attendance_date, status) " +
                    "VALUES (?, CURDATE(), ?)";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, studentId);
            ps.setString(2, status);

            ps.executeUpdate();

            System.out.println(
                    "Attendance added successfully!");

            ps.close();

        } catch (Exception e) {

            System.out.println("Error: " + e.getMessage());
        }
    }

    // 9. View Attendance
    static void viewAttendance(Connection con) {

        try {

            String sql =
                    "SELECT a.attendance_id, " +
                    "s.student_name, " +
                    "a.attendance_date, a.status " +
                    "FROM attendance a " +
                    "JOIN students s " +
                    "ON a.student_id = s.student_id";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println(
                    "\n===== ATTENDANCE DETAILS =====");

            while (rs.next()) {

                System.out.println("Attendance ID: "
                        + rs.getInt("attendance_id"));

                System.out.println("Student Name: "
                        + rs.getString("student_name"));

                System.out.println("Date: "
                        + rs.getDate("attendance_date"));

                System.out.println("Status: "
                        + rs.getString("status"));

                System.out.println("--------------------------");
            }

            rs.close();
            ps.close();

        } catch (Exception e) {

            System.out.println("Error: " + e.getMessage());
        }
    }

    // 10. Add Marks
    static void addMarks(Connection con) {

        try {

            System.out.print("Enter Student ID: ");
            int studentId = sc.nextInt();

            System.out.print("Enter Course ID: ");
            int courseId = sc.nextInt();

            System.out.print("Enter Marks: ");
            int marks = sc.nextInt();
            sc.nextLine();

            if (marks < 0 || marks > 100) {

                System.out.println(
                        "Marks must be between 0 and 100.");

                return;
            }

            String sql =
                    "INSERT INTO marks " +
                    "(student_id, course_id, marks) " +
                    "VALUES (?, ?, ?)";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            ps.setInt(3, marks);

            ps.executeUpdate();

            System.out.println(
                    "Marks added successfully!");

            ps.close();

        } catch (Exception e) {

            System.out.println("Error: " + e.getMessage());
        }
    }

    // 11. View Marks
    static void viewMarks(Connection con) {

        try {

            String sql =
                    "SELECT m.mark_id, s.student_name, " +
                    "c.course_name, m.marks " +
                    "FROM marks m " +
                    "JOIN students s " +
                    "ON m.student_id = s.student_id " +
                    "JOIN courses c " +
                    "ON m.course_id = c.course_id";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println("\n===== MARKS DETAILS =====");

            while (rs.next()) {

                System.out.println("Mark ID: "
                        + rs.getInt("mark_id"));

                System.out.println("Student Name: "
                        + rs.getString("student_name"));

                System.out.println("Course Name: "
                        + rs.getString("course_name"));

                System.out.println("Marks: "
                        + rs.getInt("marks"));

                System.out.println("--------------------------");
            }

            rs.close();
            ps.close();

        } catch (Exception e) {

            System.out.println("Error: " + e.getMessage());
        }
    }

    // 12. Result Generation
    static void generateResult(Connection con) {

        try {

            String sql =
                    "SELECT s.student_id, s.student_name, " +
                    "SUM(m.marks) AS total_marks, " +
                    "AVG(m.marks) AS average_marks " +
                    "FROM students s " +
                    "JOIN marks m " +
                    "ON s.student_id = m.student_id " +
                    "GROUP BY s.student_id, s.student_name";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println(
                    "\n========== STUDENT RESULT ==========");

            while (rs.next()) {

                int studentId =
                        rs.getInt("student_id");

                String studentName =
                        rs.getString("student_name");

                int total =
                        rs.getInt("total_marks");

                double average =
                        rs.getDouble("average_marks");

                String grade;

                if (average >= 90) {
                    grade = "A+";
                } else if (average >= 80) {
                    grade = "A";
                } else if (average >= 70) {
                    grade = "B";
                } else if (average >= 60) {
                    grade = "C";
                } else if (average >= 50) {
                    grade = "D";
                } else {
                    grade = "F";
                }

                System.out.println("Student ID: "
                        + studentId);

                System.out.println("Student Name: "
                        + studentName);

                System.out.println("Total Marks: "
                        + total);

                System.out.println("Average: "
                        + String.format("%.2f", average));

                System.out.println("Grade: "
                        + grade);

                System.out.println(
                        "----------------------------------");
            }

            rs.close();
            ps.close();

        } catch (Exception e) {

            System.out.println("Error: " + e.getMessage());
        }
    }
}