import java.time.LocalTime;

public class SurgeTimeChecker {
    private final SurgeTimeConfig config;

    public SurgeTimeChecker(SurgeTimeConfig config) {
        this.config = config;
    }

    public boolean isSurgeTime(LocalTime time) {
        return config.getSurgeRanges().stream()
                .anyMatch(range -> range.isWithinRange(time));
    }
}
