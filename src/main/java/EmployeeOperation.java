import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmployeeOperation {
    static List<Employee> employee_list = new ArrayList<>();
    private PreparedStatement preparedStatement;
    private static EmployeeOperation employeeOperation;
    public static List<Employee> getEmployee_list() {
        return employee_list;
    }

    public List<Employee> readData(Connection con) throws JDBCCustomException, SQLException {
        try{
            String query = "select * from employee_payroll";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()){
                int id = rs.getInt(1);
                String name = rs.getString(2);
                char gender = rs.getString(3).charAt(0);
                double salary = rs.getDouble(4);
                Date date = rs.getDate(5);

                System.out.print("\nID: " + id);
                System.out.print("\nName: " + name);
                System.out.print("\nGender: "+ gender);
                System.out.print("\nSalary: " + salary);
                System.out.println("\nDate: " + date);

                Employee emp = new Employee(id,name,gender,salary,date);
                employee_list.add(emp);
            }
        }catch(Exception e){
            throw new JDBCCustomException("Read Process Unsuccessful");
        }
        finally {
            con.close();
        }
        return employee_list;
    }
    public void updateData(Connection con, String column, String name, String value) throws JDBCCustomException, SQLException {
        try{
            PreparedStatement stmt = con.prepareStatement("Update employee_payroll set Salary = ? where Name = ?");
            stmt.setDouble(1,Double.parseDouble(value));
            stmt.setString(2,name);
            stmt.executeUpdate();
            for(Employee e: EmployeeOperation.employee_list) {
                if (e.getName().equals(name)) {
                    e.setSalary(Double.parseDouble(value));
                    System.out.println(e.getSalary());
                }
            }
        }catch(Exception e){
            try {
                con.rollback();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
            throw new JDBCCustomException("Update Process Unsuccessful");
        }finally {
            con.close();
        }
    }
    public List<Employee> retrieveEmployeesByDate(Connection con, String startDate, String endDate) throws SQLException, JDBCCustomException {
        try {
            PreparedStatement stmt = con.prepareStatement("Select * from employee_payroll where StartDate between ? and ?");
            stmt.setString(1, startDate);
            stmt.setString(2, endDate);

            stmt.executeQuery();

            System.out.println(stmt.toString());
        } catch (Exception e) {
            try {
                con.rollback();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
            throw new JDBCCustomException("Retrieve by Date process Unsuccessful");
        }
        finally {
            con.close();
        }

        return employee_list;
    }
    public List<Employee> dataBaseFunctionOps(Connection con, String gender) throws SQLException, JDBCCustomException {
        try {
            PreparedStatement stmt = con.prepareStatement("select gender, count(gender), min(salary), max(salary), avg(salary), sum(salary) from employee_payroll group by ?");
            stmt.setString(1, gender);

            stmt.executeQuery();
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException sq){
                sq.printStackTrace();
            }
            throw new JDBCCustomException("DataBase Function operation Failed");
        }
        finally {
            con.close();
        }

        return employee_list;
    }

    public int insertDataToEmployeeDB(Connection con, String name, char gender, double salary, Date date, String dept) throws SQLException {
        int result_query1 = -1, result_query2 = -1,result_query3 = -1, result = 0;
        int id = 0;
        Employee emp = null;
        try {
            DBConnection jdbc_con = new DBConnection();
            con = jdbc_con.getConnection();

            con.setAutoCommit(false);
            String query = String.format("Insert into employee_payroll (Name,Gender,Salary,StartDate, Is_Active) values " +
                    "('%s', '%s', '%s', '%s','%s')", name, gender, salary, date, "Yes");
            Statement stmt = con.createStatement();
            result_query1 = stmt.executeUpdate(query, stmt.RETURN_GENERATED_KEYS);
            if (result_query1 == 1) {
                ResultSet rs = stmt.getGeneratedKeys();
                while (rs.next()) {
                    id = rs.getInt(1);
                    emp = new Employee(id, name, gender, salary, date, "yes");
                }
            }

            double deductions = .2 * salary;
            double taxable_pay = salary - deductions;
            double income_tax = 0.1 * taxable_pay;
            double net_salary = salary - income_tax;
            String query2 = String.format("Insert into payroll_details (Id, basic_pay, deductions, taxable_pay, income_tax, netPay)" +
                    " values ('%s', '%s', '%s', '%s', '%s', '%s')", id, salary, deductions, taxable_pay, income_tax, net_salary);
            result_query2 = stmt.executeUpdate(query2, stmt.RETURN_GENERATED_KEYS);

            if (result_query1 == 1 && result_query2 == 2) {
                con.commit();
                EmployeeOperation.getEmployee_list().add(emp);
            }

            String query3 = String.format("Insert into department (Id, dept)" + "values ('%s', '%s')", id, dept);
            result_query3 = stmt.executeUpdate(query3, stmt.RETURN_GENERATED_KEYS);

            if (result_query1 == 1 && result_query2 == 1 && result_query3 == 1) {
                con.commit();
                result = 1;
                EmployeeOperation.getEmployee_list().add(emp);
            }
        } catch (SQLException sq) {
            sq.printStackTrace();
            try {
                con.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    public void removeEmployeeFromDB(Connection con, String name) throws SQLException {
        try {
            DBConnection db = new DBConnection();
            con = db.getConnection();

            String query = String.format("Update employee_payroll set Is_Active = 'No' where name = '" + name + "'");
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            con.close();
        }
    }
}