import java.sql.Connection;

public class Main {
    public static void main(String[] args) throws JDBCCustomException {
        DBConnection db = new DBConnection();
        Connection con = db.getConnection();

        EmployeeOperation employeeOperation = new EmployeeOperation();
        employeeOperation.readData(con);


    }
}
