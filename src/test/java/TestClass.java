import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestClass {
    EmployeeOperation eo;
    DBConnection dbc;
    Connection con;

    @Before
    public void init(){
        eo = new EmployeeOperation();
        dbc = new DBConnection();
        con = dbc.getConnection();
    }

    @Test
    public void onUpdation_compareEmpPayrollObjectWithDB() throws JDBCCustomException, SQLException {

        /* UC3 -- update employee object and in the database and compare */
        eo.readData(con);
        eo.updateData(con, "Salary", "Terisa", "200000");

        Employee e = null;
        for(Employee ep: EmployeeOperation.employee_list){
            if(ep.getName().equals("Terisa"))
                e = ep;
        }
        ResultSet rs = con.createStatement().executeQuery("Select Salary from employee_payroll where Name = 'Terisa'");
        double salary = 0;
        while(rs.next()){
            salary = rs.getDouble("Salary");
        }
        Assert.assertEquals(e.getSalary(), salary,0);
    }
}
