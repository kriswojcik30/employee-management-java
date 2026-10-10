import java.util.Objects;

public class Employee implements Comparable<Employee> {
    private String imie;
    private String nazwisko;
    private EmployeeCondition condition;
    private int birthYear;
    private double salary;

    public Employee(String imie, String nazwisko, EmployeeCondition condition, int birthYear, double salary) {
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.condition = condition;
        this.birthYear = birthYear;
        this.salary = salary;
    }

    public String getImie() {
        return imie;
    }

    public String getFirstName() {
        return imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public String getLastName() {
        return nazwisko;
    }

    public EmployeeCondition getCondition() {
        return condition;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public double getSalary() {
        return salary;
    }

    public void setCondition(EmployeeCondition condition) {
        this.condition = condition;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void printing() {
        System.out.println("Imie: " + imie);
        System.out.println("Nazwisko: " + nazwisko);
        System.out.println("Stan: " + condition);
        System.out.println("Rok urodzenia: " + birthYear);
        System.out.println("Wynagrodzenie: " + salary);
    }

    @Override
    public int compareTo(Employee other) {
        return this.nazwisko.compareTo(other.nazwisko);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employee)) {
            return false;
        }
        Employee employee = (Employee) o;
        return Objects.equals(imie, employee.imie) &&
                Objects.equals(nazwisko, employee.nazwisko);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imie, nazwisko);
    }
}
