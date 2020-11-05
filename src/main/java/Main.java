import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws JDBCCustomException, SQLException {
        DBConnection db = new DBConnection();
        Connection con = db.getConnection();

        EmployeeOperation employeeOperation = new EmployeeOperation();
//        employeeOperation.readData(con);
//        employeeOperation.retrieveEmployeesByDate(con, "2018-10-10", "2020-01-01");
//        employeeOperation.dataBaseFunctionOps(con, "F");
        Date date = new Date(0115, 9,25);
        employeeOperation.insertDataToEmployeeDB(con, "Dani", 'F', 20000, date, "Sales" );


    }
}