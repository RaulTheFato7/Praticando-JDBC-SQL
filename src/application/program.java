package application;

import db.DB;
import db.DbException;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Scanner;

public class program {

    public static void main(String[] args ) {

        Locale.setDefault(Locale.US);;
        Scanner sc = new Scanner(System.in);

        Connection conn = null;
        Statement st = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DB.getConnection();

            System.out.println("==== Departments available ====");
            st = conn.createStatement();
            rs = st.executeQuery("select * from department");

            while (rs.next()) {
                System.out.println(rs.getInt("Id") + ", " + rs.getString("Name"));
            }
            DB.closeResultSet(rs);
            DB.closeStatement(st);

            System.out.println("==== Salesperson Registration ====");
            System.out.print("Name: ");
            String name = sc.nextLine();

            System.out.print("Email: ");
            String email = sc.next();

            System.out.print("Data de Nascimento (dd/MM/yyyy): ");
            String birthDateStr = sc.next();

            System.out.print("Salário Base: ");
            double baseSalary = sc.nextDouble();

            System.out.print("ID do Departamento: ");
            int departmentId = sc.nextInt();

            ps = conn.prepareStatement(
                    "INSERT INTO seller "
                    + "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
                    + "VALUES "
                    + "(?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, name);
            ps.setString(2, email);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            java.util.Date date = sdf.parse(birthDateStr);
            ps.setDate(3, new java.sql.Date(date.getTime()
            ));

            ps.setDouble(4, baseSalary);
            ps.setInt(5, departmentId);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    System.out.println("Sucess!! New seller with id insert: " + id);
                }
            } else {
                System.out.println("No rows affected.");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (ParseException e) {
            System.out.println("Date formatting error. Use the format dd/MM/yyyy.");;
        }
        finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
            DB.closeStatement(ps);
            DB.closeConnection();
        }
    }
}