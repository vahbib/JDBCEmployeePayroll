import java.sql.*;
import java.util.*;
import java.util.Date;

public class EmployeeOperation {
    List<Employee> employee_list = new ArrayList<>();
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
}
