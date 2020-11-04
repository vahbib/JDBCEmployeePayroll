
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmployeeOperation {
    static List<Employee> employee_list = new ArrayList<>();
    private PreparedStatement preparedStatement;
    private static EmployeeOperation employeeOperation;

    public List<Employee> readData(Connection con) throws JDBCCustomException {
        try{
            String query = "select * from employee_payroll";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()){
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String Gender = rs.getString(3);
                double salary = rs.getDouble(4);
                Date date = rs.getDate(5);

                System.out.print("\nID: " + id);
                System.out.print("\nName: " + name);
                System.out.print("\nGender: "+ Gender);
                System.out.print("\nSalary: " + salary);
                System.out.println("\nDate: " + date);

                Employee emp = new Employee(id,name,salary,date);
                employee_list.add(emp);
            }
        }catch(Exception e){
            throw new JDBCCustomException("Read Process Unsuccessful");
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
            throw new JDBCCustomException("Update Process Unsuccessful");
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
            throw new JDBCCustomException("Retrieve by Date process Unsuccessful");
        }

        return employee_list;
    }
    public List<Employee> dataBaseFunctionOps(Connection con, String gender) throws SQLException, JDBCCustomException {
        PreparedStatement stmt =con.prepareStatement("select gender, count(gender), min(salary), max(salary), avg(salary), sum(salary) from employee_payroll group by ?");
        stmt.setString(1, gender);

        stmt.executeQuery();

        return employee_list;
    }
}