import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmployeeOperation {
    static List<Employee> employee_list = new ArrayList<>();

    private PreparedStatement preparedStatement;
    private static EmployeeOperation employeeOperation;
    public static EmployeeOperation getInstance(){
        if(employeeOperation == null)
            employeeOperation = new EmployeeOperation();
        return employeeOperation;
    }

    public void readData(Connection con) throws JDBCCustomException {
        try{
            String query = "select * from employee_payroll";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()){
                int id = rs.getInt(1);
                String name = rs.getString(2);
                double salary = rs.getDouble(3);
                Date date = rs.getDate(4);

                System.out.print("\nID: " + id);
                System.out.print("\nName: " + name);
                System.out.print("\nSalary: " + salary);
                System.out.println("\nDate: " + date);

                Employee emp = new Employee(id,name,salary,date);
                employee_list.add(emp);
            }
        }catch(Exception e){
            throw new JDBCCustomException("Read Process Unsuccessful");
        }
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
                    System.out.println("\n"+e.getSalary());
                }
            }
        }catch(Exception e){
            throw new JDBCCustomException("Update Process Unsuccessful");
        }
    }
    public Employee getPayrollDataByName(String name) throws SQLException {
        DBConnection jdbc_con = new DBConnection();
        Connection con = jdbc_con.getConnection();
        preparedStatement = con.prepareStatement("Select * from employee_payroll where name = ?");
        preparedStatement.setString(1,name);
        ResultSet rs = preparedStatement.executeQuery();
        Employee emp = null;
        while(rs.next()){
            int id = rs.getInt(1);
            double salary = rs.getDouble(3);
            Date date = rs.getDate(4);
            emp = new Employee(id,name,salary,date);
        }
        return emp;
    }
}