import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConditionAnalyzer {
    public static boolean hasCondition(List<Employee> employees, EmployeeCondition condition) {
        return employees.stream()
                .anyMatch(employee -> employee.getCondition() == condition);
    }

    public static boolean allHaveCondition(List<Employee> employees, EmployeeCondition condition) {
        return employees.stream()
                .allMatch(employee -> employee.getCondition() == condition);
    }

    public static boolean noneHaveCondition(List<Employee> employees, EmployeeCondition condition) {
        return employees.stream()
                .noneMatch(employee -> employee.getCondition() == condition);
    }

    public static Map<EmployeeCondition, Long> summarizeConditions(List<Employee> employees) {
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

    public static double getConditionPercentage(List<Employee> employees, EmployeeCondition condition) {
        if (employees == null || employees.isEmpty()) {
            return 0.0;
        }

        long matchingEmployees = employees.stream()
                .filter(employee -> employee.getCondition() == condition)
                .count();

        return (matchingEmployees * 100.0) / employees.size();
    }
}
