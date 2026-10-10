import java.util.EnumMap;
import java.util.Map;

public class GroupStatistics {
    private int employeeCount;
    private double averageSalary;
    private double maxSalary;
    private double minSalary;
    private Map<EmployeeCondition, Integer> conditionDistribution;

    public GroupStatistics(int employeeCount, double averageSalary, double maxSalary, double minSalary,
                           Map<EmployeeCondition, Integer> conditionDistribution) {
        this.employeeCount = employeeCount;
        this.averageSalary = averageSalary;
        this.maxSalary = maxSalary;
        this.minSalary = minSalary;
        this.conditionDistribution = new EnumMap<>(conditionDistribution);
    }

    public int getEmployeeCount() {
        return employeeCount;
    }

    public double getAverageSalary() {
        return averageSalary;
    }

    public double getMaxSalary() {
        return maxSalary;
    }

    public double getMinSalary() {
        return minSalary;
    }

    public Map<EmployeeCondition, Integer> getConditionDistribution() {
        return new EnumMap<>(conditionDistribution);
    }
}
