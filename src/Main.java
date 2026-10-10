public class Main {
    public static void main(String[] args) {

        ClassContainer container = new ClassContainer();

        container.addClass("IT", 3);
        container.addClass("HR", 2);

        Employee e1 = new Employee("Jan", "Kowalski", EmployeeCondition.OBECNY, 1995, 5000);
        Employee e2 = new Employee("Anna", "Nowak", EmployeeCondition.CHORY, 1998, 6000);
        Employee e3 = new Employee("Piotr", "Zalewski", EmployeeCondition.DELEGACJA, 1990, 7000);
        Employee e4 = new Employee("Kasia", "Mazur", EmployeeCondition.NIEOBECNY, 2000, 5500);

        ClassEmployee it = container.getClass("IT");
        ClassEmployee hr = container.getClass("HR");

        it.addEmployee(e1);
        it.addEmployee(e2);
        it.addEmployee(e3);

        hr.addEmployee(e4);

        it.addEmployee(e1);

        System.out.println("=== SUMMARY IT ===");
        it.summary();

        it.changeCondition(e2, EmployeeCondition.OBECNY);
        it.addSalary(e1, 1000);

        System.out.println("\n=== SEARCH ===");
        Employee found = it.search("Kowalski");
        if (found != null) {
            found.printing();
        } else {
            System.out.println("Nie znaleziono pracownika.");
        }

        System.out.println("\n=== SEARCH PARTIAL ===");
        for (Employee e : it.searchPartial("al")) {
            e.printing();
            System.out.println();
        }

        System.out.println("\nLiczba obecnych: " + it.countByCondition(EmployeeCondition.OBECNY));

        System.out.println("\n=== SORT BY NAME ===");
        for (Employee e : it.sortByName()) {
            e.printing();
            System.out.println();
        }

        System.out.println("\n=== SORT BY SALARY ===");
        for (Employee e : it.sortBySalary()) {
            e.printing();
            System.out.println();
        }

        System.out.println("\n=== MAX SALARY ===");
        Employee max = it.max();
        if (max != null) {
            max.printing();
        }

        it.removeEmployee(e3);

        System.out.println("\n=== PO USUNIĘCIU ===");
        it.summary();

        System.out.println("\n=== CONTAINER SUMMARY ===");
        container.summary();

        System.out.println("\n=== PUSTE GRUPY ===");
        for (ClassEmployee grupa : container.findEmpty()) {
            System.out.println(grupa.getNazwaGrupy());
        }
    }
}