import java.util.LinkedHashMap;
import java.util.Map;

public class AgeStatistics {
    private int youngestAge;
    private int oldestAge;
    private double averageAge;
    private Map<String, Integer> ageRangeCounts;

    public AgeStatistics(int youngestAge, int oldestAge, double averageAge, Map<String, Integer> ageRangeCounts) {
        this.youngestAge = youngestAge;
        this.oldestAge = oldestAge;
        this.averageAge = averageAge;
        this.ageRangeCounts = new LinkedHashMap<>(ageRangeCounts);
    }

    public int getYoungestAge() {
        return youngestAge;
    }

    public int getOldestAge() {
        return oldestAge;
    }

    public double getAverageAge() {
        return averageAge;
    }

    public Map<String, Integer> getAgeRangeCounts() {
        return new LinkedHashMap<>(ageRangeCounts);
    }
}
