
import java.util.Date;

public class Employee {
    private int id;
    private String name;

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    private char gender;
    private double salary;
    private Date date;

    public Employee(int id, String name, char gender, double salary, Date date) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.date = date;
        this.gender = gender;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public double getSalary() { return salary; }

    public void setSalary(double salary) { this.salary = salary; }

    public Date getDate() { return date; }

    public void setDate(Date date) { this.date = date; }
}