import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ClassContainer {
    private Map<String, ClassEmployee> grupy;
    private StorageMode currentMode;
    private Comparator<String> keyComparator;

    public ClassContainer() {
        this(StorageMode.HASH_MAP);
    }

    public ClassContainer(StorageMode mode) {
        this(mode, String.CASE_INSENSITIVE_ORDER);
    }

    public ClassContainer(Comparator<String> keyComparator) {
        this(StorageMode.TREE_MAP, keyComparator);
    }

    public ClassContainer(StorageMode mode, Comparator<String> keyComparator) {
        this.currentMode = mode != null ? mode : StorageMode.HASH_MAP;
        this.keyComparator = createKeyComparator(keyComparator);
        this.grupy = createMap(this.currentMode);
    }

    public void addClass(String nazwa, double pojemnosc) {
        String existingKey = findExistingKey(nazwa);
        if (existingKey != null) {
            System.out.println("Grupa o tej nazwie już istnieje.");
            return;
        }

        grupy.put(nazwa, new ClassEmployee(nazwa, (int) pojemnosc));
    }

    public void removeClass(String nazwa) {
        String existingKey = findExistingKey(nazwa);
        if (existingKey != null) {
            grupy.remove(existingKey);
        } else {
            System.out.println("Nie ma grupy o takiej nazwie.");
        }
    }

    public List<ClassEmployee> findEmpty() {
        List<ClassEmployee> emptyGroups = new ArrayList<>();

        for (ClassEmployee grupa : grupy.values()) {
            if (grupa.getEmployeeCount() == 0) {
                emptyGroups.add(grupa);
            }
        }

        return emptyGroups;
    }

    public void summary() {
        for (Map.Entry<String, ClassEmployee> entry : grupy.entrySet()) {
            String nazwa = entry.getKey();
            ClassEmployee grupa = entry.getValue();

            double zapelnienie = 0.0;

            if (grupa.getMaxEmployees() > 0) {
                zapelnienie = ((double) grupa.getEmployeeCount() / grupa.getMaxEmployees()) * 100.0;
            }

            System.out.println("Grupa: " + nazwa + ", zapełnienie: " + zapelnienie + "%");
        }
    }

    public List<String> getGroupsInOrder() {
        return new ArrayList<>(grupy.keySet());
    }

    public void changeStorageMode(StorageMode newMode) {
        StorageMode targetMode = newMode != null ? newMode : StorageMode.HASH_MAP;
        if (targetMode == currentMode) {
            return;
        }

        Map<String, ClassEmployee> convertedGroups = createMap(targetMode);
        convertedGroups.putAll(grupy);
        grupy = convertedGroups;
        currentMode = targetMode;
    }

    public void demonstrateOrderDifferences() {
        String[] groupNames = {"Zespol C", "Zespol A", "Zespol B", "Zespol D"};

        for (StorageMode mode : StorageMode.values()) {
            ClassContainer container = new ClassContainer(mode, keyComparator);
            for (String groupName : groupNames) {
                container.addClass(groupName, 10);
            }

            System.out.println("Tryb: " + mode);
            System.out.println("Kolejnosc: " + container.getGroupsInOrder());
            System.out.println();
        }
    }

    public Map<String, Integer> countEmployeesInGroups() {
        return grupy.entrySet().stream()
                .filter(entry -> entry.getValue().getEmployeeCount() > 0)
                .sorted((first, second) -> Integer.compare(
                        second.getValue().getEmployeeCount(),
                        first.getValue().getEmployeeCount()
                ))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().getEmployeeCount(),
                        (first, second) -> first,
                        LinkedHashMap::new
                ));
    }

    public void printEmployeeStatistics() {
        Map<String, Integer> stats = countEmployeesInGroups();
        System.out.println("Statystyki pracowników w grupach:");
        stats.forEach((groupName, count) ->
                System.out.println(groupName + ": " + count + " pracowników"));
    }

    public Map<String, GroupStatistics> getDetailedStatistics() {
        return grupy.entrySet().stream()
                .filter(entry -> entry.getValue().getEmployeeCount() > 0)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> buildGroupStatistics(entry.getValue()),
                        (first, second) -> first,
                        LinkedHashMap::new
                ));
    }

    public ClassEmployee getClass(String nazwa) {
        String existingKey = findExistingKey(nazwa);
        if (existingKey == null) {
            return null;
        }
        return grupy.get(existingKey);
    }

    public ClassEmployee getGroup(String nazwa) {
        return getClass(nazwa);
    }

    private GroupStatistics buildGroupStatistics(ClassEmployee classEmployee) {
        List<Employee> employees = classEmployee.getEmployees();
        double averageSalary = employees.stream()
                .mapToDouble(Employee::getSalary)
                .average()
                .orElse(0.0);
        double maxSalary = employees.stream()
                .mapToDouble(Employee::getSalary)
                .max()
                .orElse(0.0);
        double minSalary = employees.stream()
                .mapToDouble(Employee::getSalary)
                .min()
                .orElse(0.0);

        Map<EmployeeCondition, Integer> conditionDistribution = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getCondition,
                        () -> new EnumMap<>(EmployeeCondition.class),
                        Collectors.summingInt(employee -> 1)
                ));

        for (EmployeeCondition condition : EmployeeCondition.values()) {
            conditionDistribution.putIfAbsent(condition, 0);
        }

        return new GroupStatistics(
                employees.size(),
                averageSalary,
                maxSalary,
                minSalary,
                conditionDistribution
        );
    }

    private Comparator<String> createKeyComparator(Comparator<String> customComparator) {
        Comparator<String> safeComparator = customComparator != null ? customComparator : String::compareTo;
        return (first, second) -> {
            int caseInsensitiveResult = String.CASE_INSENSITIVE_ORDER.compare(first, second);
            if (caseInsensitiveResult == 0) {
                return 0;
            }
            return safeComparator.compare(first, second);
        };
    }

    private Map<String, ClassEmployee> createMap(StorageMode mode) {
        if (mode == StorageMode.LINKED_HASH_MAP) {
            return new LinkedHashMap<>();
        }
        if (mode == StorageMode.TREE_MAP) {
            return new TreeMap<>(keyComparator);
        }
        return new HashMap<>();
    }

    private String findExistingKey(String nazwa) {
        if (nazwa == null) {
            return null;
        }

        if (grupy instanceof TreeMap) {
            return grupy.containsKey(nazwa) ? nazwa : null;
        }

        return grupy.keySet().stream()
                .filter(existingName -> existingName.equalsIgnoreCase(nazwa))
                .findFirst()
                .orElse(null);
    }
}
