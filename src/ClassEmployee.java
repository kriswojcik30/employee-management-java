import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.time.Year;
import java.util.stream.Collectors;

public class ClassEmployee {
    private String nazwaGrupy;
    private List<Employee> employees;
    private int maxEmployees;

    public ClassEmployee(String nazwaGrupy, int maxEmployees) {
        this.nazwaGrupy = nazwaGrupy;
        this.maxEmployees = maxEmployees;
        this.employees = new ArrayList<>();
    }

    public void addEmployee(Employee employee) {
        if (employees.size() >= maxEmployees) {
            System.out.println("Nie można dodać pracownika. Przekroczono pojemność grupy.");
            return;
        }

        for (Employee e : employees) {
            if (e.getImie().equals(employee.getImie()) &&
                    e.getNazwisko().equals(employee.getNazwisko())) {
                System.out.println("Pracownik o takim imieniu i nazwisku już istnieje.");
                return;
            }
        }

        employees.add(employee);
    }

    public void addSalary(Employee employee, double salary) {
        for (Employee e : employees) {
            if (e.getImie().equals(employee.getImie()) &&
                    e.getNazwisko().equals(employee.getNazwisko())) {
                e.setSalary(e.getSalary() + salary);
                return;
            }
        }

        System.out.println("Nie znaleziono pracownika.");
    }

    public void removeEmployee(Employee employee) {
        for (int i = 0; i < employees.size(); i++) {
            Employee e = employees.get(i);

            if (e.getImie().equals(employee.getImie()) &&
                    e.getNazwisko().equals(employee.getNazwisko())) {
                employees.remove(i);
                return;
            }
        }

        System.out.println("Nie znaleziono pracownika do usunięcia.");
    }

    public void changeCondition(Employee employee, EmployeeCondition condition) {
        for (Employee e : employees) {
            if (e.getImie().equals(employee.getImie()) &&
                    e.getNazwisko().equals(employee.getNazwisko())) {
                e.setCondition(condition);
                return;
            }
        }

        System.out.println("Nie znaleziono pracownika.");
    }

    public Employee search(String nazwisko) {
        List<Employee> temp = new ArrayList<>(employees);
        temp.sort(Comparator.comparing(Employee::getNazwisko));

        for (Employee e : temp) {
            if (e.getNazwisko().equalsIgnoreCase(nazwisko)) {
                return e;
            }
        }

        return null;
    }

    public List<Employee> searchPartial(String fragment) {
        List<Employee> wynik = new ArrayList<>();

        for (Employee e : employees) {
            if (e.getImie().toLowerCase().contains(fragment.toLowerCase()) ||
                    e.getNazwisko().toLowerCase().contains(fragment.toLowerCase())) {
                wynik.add(e);
            }
        }

        return wynik;
    }

    public int countByCondition(EmployeeCondition condition) {
        int count = 0;

        for (Employee e : employees) {
            if (e.getCondition() == condition) {
                count++;
            }
        }

        return count;
    }

    public void summary() {
        for (Employee e : employees) {
            e.printing();
            System.out.println();
        }
    }

    public List<Employee> sortByName() {
        List<Employee> sorted = new ArrayList<>(employees);
        Collections.sort(sorted);
        return sorted;
    }

    public List<Employee> sortBySalary() {
        List<Employee> sorted = new ArrayList<>(employees);

        sorted.sort(new Comparator<Employee>() {
            @Override
            public int compare(Employee e1, Employee e2) {
                return Double.compare(e2.getSalary(), e1.getSalary());
            }
        });

        return sorted;
    }

    public Employee max() {
        if (employees.isEmpty()) {
            return null;
        }

        return Collections.max(employees, Comparator.comparingDouble(Employee::getSalary));
    }

    public int removeDuplicates() {
        if (employees == null || employees.isEmpty()) {
            employees = new ArrayList<>();
            return 0;
        }

        List<Employee> uniqueEmployees = new ArrayList<>();
        Set<Employee> seenEmployees = new HashSet<>();

        for (Employee employee : employees) {
            if (seenEmployees.add(employee)) {
                uniqueEmployees.add(employee);
            }
        }

        int removedDuplicates = employees.size() - uniqueEmployees.size();
        employees = uniqueEmployees;
        return removedDuplicates;
    }

    public Map<EmployeeCondition, List<Employee>> groupByCondition() {
        Map<EmployeeCondition, List<Employee>> groupedEmployees = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getCondition,
                        () -> new EnumMap<>(EmployeeCondition.class),
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> {
                                    List<Employee> sortedEmployees = new ArrayList<>(list);
                                    sortedEmployees.sort(Comparator.comparing(Employee::getNazwisko));
                                    return sortedEmployees;
                                }
                        )
                ));

        for (EmployeeCondition condition : EmployeeCondition.values()) {
            groupedEmployees.putIfAbsent(condition, new ArrayList<>());
        }

        return groupedEmployees;
    }

    public double medianSalary() {
        if (employees == null || employees.isEmpty()) {
            return 0.0;
        }

        List<Double> sortedSalaries = employees.stream()
                .map(Employee::getSalary)
                .sorted()
                .collect(Collectors.toList());

        int size = sortedSalaries.size();
        int middleIndex = size / 2;

        if (size % 2 == 1) {
            return sortedSalaries.get(middleIndex);
        }

        return (sortedSalaries.get(middleIndex - 1) + sortedSalaries.get(middleIndex)) / 2.0;
    }

    public Map<Double, List<Employee>> groupBySalary() {
        return employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getSalary,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .sorted(Comparator.comparing(Employee::getNazwisko))
                                        .collect(Collectors.toList())
                        )
                ));
    }

    public Map<String, List<Employee>> groupBySalaryRange(double rangeSize) {
        double normalizedRangeSize = rangeSize > 0.0 ? rangeSize : 1000.0;
        return employees.stream()
                .collect(Collectors.groupingBy(
                        employee -> buildSalaryRangeLabel(employee.getSalary(), normalizedRangeSize),
                        LinkedHashMap::new,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .sorted(Comparator.comparingDouble(Employee::getSalary).reversed())
                                        .collect(Collectors.toList())
                        )
                ));
    }

    public SalaryDistribution getSalaryDistribution() {
        return SalaryAnalyzer.analyzeDistribution(employees);
    }

    public List<Employee> filterByMinSalary(double minSalary) {
        return employees.stream()
                .filter(SalaryFilter.minSalary(minSalary))
                .sorted(Comparator.comparingDouble(Employee::getSalary).reversed())
                .collect(Collectors.toList());
    }

    public List<Employee> filterBySalaryRange(double min, double max) {
        return employees.stream()
                .filter(SalaryFilter.salaryRange(min, max))
                .sorted(Comparator.comparingDouble(Employee::getSalary).reversed())
                .collect(Collectors.toList());
    }

    public List<Employee> getTopEarners(int count) {
        if (count <= 0) {
            return new ArrayList<>();
        }

        return employees.stream()
                .sorted(Comparator.comparingDouble(Employee::getSalary).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    public List<Employee> getBottomEarners(int count) {
        if (count <= 0) {
            return new ArrayList<>();
        }

        return employees.stream()
                .sorted(Comparator.comparingDouble(Employee::getSalary))
                .limit(count)
                .collect(Collectors.toList());
    }

    public List<Employee> filterByPercentile(double percentile) {
        return employees.stream()
                .filter(SalaryFilter.topPercent(employees, percentile))
                .sorted(Comparator.comparingDouble(Employee::getSalary).reversed())
                .collect(Collectors.toList());
    }

    public boolean hasEmployeesOnDelegation() {
        return employees.stream()
                .anyMatch(employee -> employee.getCondition() == EmployeeCondition.DELEGACJA);
    }

    public boolean allEmployeesPresent() {
        return employees.stream()
                .allMatch(employee -> employee.getCondition() == EmployeeCondition.OBECNY);
    }

    public boolean noEmployeesSick() {
        return employees.stream()
                .noneMatch(employee -> employee.getCondition() == EmployeeCondition.CHORY);
    }

    public Map<EmployeeCondition, Long> getConditionSummary() {
        Map<EmployeeCondition, Long> summary = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getCondition,
                        () -> new EnumMap<>(EmployeeCondition.class),
                        Collectors.counting()
                ));

        for (EmployeeCondition condition : EmployeeCondition.values()) {
            summary.putIfAbsent(condition, 0L);
        }

        return summary;
    }

    public Optional<Employee> oldestEmployee() {
        return employees.stream()
                .min(Comparator.comparingInt(Employee::getBirthYear));
    }

    public Optional<Employee> youngestEmployee() {
        return employees.stream()
                .max(Comparator.comparingInt(Employee::getBirthYear));
    }

    public double getAverageAge() {
        if (employees == null || employees.isEmpty()) {
            return 0.0;
        }

        int currentYear = Year.now().getValue();
        return employees.stream()
                .mapToInt(employee -> currentYear - employee.getBirthYear())
                .average()
                .orElse(0.0);
    }

    public AgeStatistics getAgeStatistics() {
        int currentYear = Year.now().getValue();
        Map<String, Integer> ageRanges = createEmptyAgeRanges();

        if (employees == null || employees.isEmpty()) {
            return new AgeStatistics(0, 0, 0.0, ageRanges);
        }

        List<Integer> ages = employees.stream()
                .map(employee -> currentYear - employee.getBirthYear())
                .collect(Collectors.toList());

        ages.forEach(age -> ageRanges.merge(getAgeRangeLabel(age), 1, Integer::sum));

        int youngestAge = ages.stream()
                .min(Integer::compareTo)
                .orElse(0);
        int oldestAge = ages.stream()
                .max(Integer::compareTo)
                .orElse(0);
        double averageAge = ages.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);

        return new AgeStatistics(youngestAge, oldestAge, averageAge, ageRanges);
    }

    public String getNazwaGrupy() {
        return nazwaGrupy;
    }

    public List<Employee> getEmployees() {
        return new ArrayList<>(employees);
    }

    public int getEmployeeCount() {
        return employees.size();
    }

    public int getMaxEmployees() {
        return maxEmployees;
    }

    private Map<String, Integer> createEmptyAgeRanges() {
        Map<String, Integer> ageRanges = new LinkedHashMap<>();
        ageRanges.put("18-29", 0);
        ageRanges.put("30-39", 0);
        ageRanges.put("40-49", 0);
        ageRanges.put("50+", 0);
        return ageRanges;
    }

    private String getAgeRangeLabel(int age) {
        if (age < 30) {
            return "18-29";
        }
        if (age < 40) {
            return "30-39";
        }
        if (age < 50) {
            return "40-49";
        }
        return "50+";
    }

    private String buildSalaryRangeLabel(double salary, double rangeSize) {
        double lowerBound = Math.floor(salary / rangeSize) * rangeSize;
        double upperBound = lowerBound + rangeSize;
        return String.format("%.2f-%.2f", lowerBound, upperBound);
    }
}
