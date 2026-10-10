import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ManualTest {
    public static void main(String[] args) {
        ClassContainer container = new ClassContainer();
        container.addClass("IT", 10);
        container.addClass("HR", 5);

        ClassEmployee itGroup = container.getGroup("IT");
        ClassEmployee hrGroup = container.getGroup("HR");

        itGroup.addEmployee(new Employee("Jan", "Kowalski", EmployeeCondition.OBECNY, 1990, 8000));
        itGroup.addEmployee(new Employee("Anna", "Nowak", EmployeeCondition.DELEGACJA, 1985, 9000));
        itGroup.addEmployee(new Employee("Piotr", "Kowalski", EmployeeCondition.OBECNY, 1988, 7500));

        hrGroup.addEmployee(new Employee("Maria", "Zielinska", EmployeeCondition.OBECNY, 1992, 5500));
        hrGroup.addEmployee(new Employee("Tomasz", "Wisniewski", EmployeeCondition.CHORY, 1980, 6000));

        Map<EmployeeCondition, List<Employee>> byCondition = itGroup.groupByCondition();
        if (!byCondition.containsKey(EmployeeCondition.OBECNY)) {
            throw new RuntimeException("Brak klucza OBECNY w groupByCondition()");
        }
        if (byCondition.get(EmployeeCondition.OBECNY).size() != 2) {
            throw new RuntimeException("Zla liczba pracownikow OBECNY");
        }

        double median = itGroup.medianSalary();
        if (Math.abs(median - 8000.0) > 0.01) {
            throw new RuntimeException("Zla mediana wynagrodzen: " + median);
        }

        List<Employee> highEarners = itGroup.filterByMinSalary(8000);
        if (highEarners.size() != 2) {
            throw new RuntimeException("Zla liczba pracownikow z pensja >= 8000");
        }

        Optional<Employee> oldest = itGroup.oldestEmployee();
        if (!oldest.isPresent()) {
            throw new RuntimeException("Brak najstarszego pracownika");
        }
        if (!oldest.get().getLastName().equals("Nowak")) {
            throw new RuntimeException("Zly najstarszy pracownik: " + oldest.get().getLastName());
        }

        if (!itGroup.hasEmployeesOnDelegation()) {
            throw new RuntimeException("IT powinno miec pracownika na delegacji");
        }

        if (hrGroup.hasEmployeesOnDelegation()) {
            throw new RuntimeException("HR nie powinno miec pracownika na delegacji");
        }

        Map<String, Integer> stats = container.countEmployeesInGroups();
        if (!Integer.valueOf(3).equals(stats.get("IT"))) {
            throw new RuntimeException("IT powinno miec 3 pracownikow");
        }
        if (!Integer.valueOf(2).equals(stats.get("HR"))) {
            throw new RuntimeException("HR powinno miec 2 pracownikow");
        }

        System.out.println("Wszystkie testy przeszly poprawnie.");
    }
}