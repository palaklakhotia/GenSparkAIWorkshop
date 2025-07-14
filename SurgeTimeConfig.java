import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class SurgeTimeConfig {
    private final List<TimeRange> surgeRanges;

    public SurgeTimeConfig() {
        this.surgeRanges = new ArrayList<>();
    }

    public void addSurgeTime(LocalTime start, LocalTime end) {
        surgeRanges.add(new TimeRange(start, end));
    }

    public List<TimeRange> getSurgeRanges() {
        return surgeRanges;
    }
}
