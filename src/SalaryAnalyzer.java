import java.util.ArrayList;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SalaryAnalyzer {
    public static SalaryDistribution analyzeDistribution(List<Employee> employees) {
        if (employees == null || employees.isEmpty()) {
            return new SalaryDistribution(0.0, 0.0, 0.0, 0.0, 0.0, new LinkedHashMap<>());
        }

        List<Double> sortedSalaries = employees.stream()
                .map(Employee::getSalary)
                .sorted()
                .collect(Collectors.toList());

        DoubleSummaryStatistics statistics = sortedSalaries.stream()
                .mapToDouble(Double::doubleValue)
                .summaryStatistics();

        double mean = statistics.getAverage();
        double median = calculateMedian(sortedSalaries);
        double standardDeviation = calculateStandardDeviation(sortedSalaries, mean);
        double rangeSize = determineRangeSize(statistics.getMin(), statistics.getMax());

        Map<String, Integer> ranges = employees.stream()
                .collect(Collectors.groupingBy(
                        employee -> buildSalaryRangeLabel(employee.getSalary(), rangeSize),
                        LinkedHashMap::new,
                        Collectors.summingInt(employee -> 1)
                ));

        return new SalaryDistribution(
                statistics.getMin(),
                statistics.getMax(),
                mean,
                median,
                standardDeviation,
                ranges
        );
    }

    public static List<Employee> findOutliers(List<Employee> employees, double threshold) {
        if (employees == null || employees.isEmpty()) {
            return new ArrayList<>();
        }

        double normalizedThreshold = threshold > 0.0 ? threshold : 2.0;
        SalaryDistribution distribution = analyzeDistribution(employees);
        double mean = distribution.getMean();
        double standardDeviation = distribution.getStandardDeviation();

        if (standardDeviation == 0.0) {
            return new ArrayList<>();
        }

        return employees.stream()
                .filter(employee -> Math.abs(employee.getSalary() - mean) > normalizedThreshold * standardDeviation)
                .sorted(Comparator.comparingDouble(Employee::getSalary).reversed())
                .collect(Collectors.toList());
    }

    public static double calculateGiniCoefficient(List<Employee> employees) {
        if (employees == null || employees.isEmpty()) {
            return 0.0;
        }

        List<Double> sortedSalaries = employees.stream()
                .map(Employee::getSalary)
                .sorted()
                .collect(Collectors.toList());

        double totalIncome = sortedSalaries.stream()
                .mapToDouble(Double::doubleValue)
                .sum();

        if (totalIncome == 0.0) {
            return 0.0;
        }

        double weightedSum = 0.0;
        int size = sortedSalaries.size();
        for (int i = 0; i < size; i++) {
            weightedSum += (i + 1) * sortedSalaries.get(i);
        }

        return (2.0 * weightedSum) / (size * totalIncome) - ((double) (size + 1) / size);
    }

    private static double calculateMedian(List<Double> sortedSalaries) {
        int size = sortedSalaries.size();
        int middleIndex = size / 2;

        if (size % 2 == 1) {
            return sortedSalaries.get(middleIndex);
        }

        return (sortedSalaries.get(middleIndex - 1) + sortedSalaries.get(middleIndex)) / 2.0;
    }

    private static double calculateStandardDeviation(List<Double> salaries, double mean) {
        return Math.sqrt(salaries.stream()
                .mapToDouble(salary -> Math.pow(salary - mean, 2))
                .average()
                .orElse(0.0));
    }

    private static double determineRangeSize(double min, double max) {
        double spread = max - min;
        if (spread <= 0.0) {
            return 1000.0;
        }
        return Math.max(1000.0, Math.ceil(spread / 5.0));
    }

    private static String buildSalaryRangeLabel(double salary, double rangeSize) {
        double lowerBound = Math.floor(salary / rangeSize) * rangeSize;
        double upperBound = lowerBound + rangeSize;
        return String.format("%.2f-%.2f", lowerBound, upperBound);
    }
}
