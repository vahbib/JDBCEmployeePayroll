import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to JDBC Program");
        DBConnection db = new DBConnection();
        Connection con = db.getConnection();
        System.out.println(con);
    }
}
