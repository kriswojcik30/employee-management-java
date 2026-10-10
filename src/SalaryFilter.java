import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class SalaryFilter {
    public static Predicate<Employee> minSalary(double minSalary) {
        return employee -> employee.getSalary() >= minSalary;
    }

    public static Predicate<Employee> salaryRange(double min, double max) {
        double lowerBound = Math.min(min, max);
        double upperBound = Math.max(min, max);
        return employee -> employee.getSalary() >= lowerBound && employee.getSalary() <= upperBound;
    }

    public static Predicate<Employee> topPercent(List<Employee> allEmployees, double percentile) {
        if (allEmployees == null || allEmployees.isEmpty()) {
            return employee -> false;
        }

        double normalizedPercentile = Math.max(0.0, Math.min(100.0, percentile));
        List<Employee> sortedEmployees = new ArrayList<>(allEmployees);
        sortedEmployees.sort(Comparator.comparingDouble(Employee::getSalary));

        int index = (int) Math.ceil((normalizedPercentile / 100.0) * sortedEmployees.size()) - 1;
        index = Math.max(0, Math.min(sortedEmployees.size() - 1, index));

        double salaryThreshold = sortedEmployees.get(index).getSalary();
        return employee -> employee.getSalary() >= salaryThreshold;
    }
}
