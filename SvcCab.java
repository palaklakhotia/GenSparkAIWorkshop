import java.time.LocalTime;

public class SvcCab implements CabFareCalculator {
    private static final double BASE_RATE = 15.0;
    private final SurgeTimeChecker surgeChecker;

    public SvcCab(SurgeTimeChecker surgeChecker) {
        this.surgeChecker = surgeChecker;
    }

    @Override
    public double calculateFare(double distance, LocalTime time) {
        double fare = distance * BASE_RATE;

        if (surgeChecker.isSurgeTime(time)) {
            fare *= 1.5;
        }

        if (isNightTime(time)) {
            fare *= 1.2;
        }

        if (distance > 200) {
            fare += 500;
        }

        return fare;
    }

    private boolean isNightTime(LocalTime time) {
        return time.isAfter(LocalTime.MIDNIGHT) && time.isBefore(LocalTime.of(6, 0));
    }
}


