import java.time.LocalTime;

public class CabFareFactory {

    public static CabFareCalculator getCabFareCalculator(String cabType) {
        SurgeTimeConfig config = new SurgeTimeConfig();
        config.addSurgeTime(LocalTime.of(8, 0), LocalTime.of(10, 0));   // Morning surge
        config.addSurgeTime(LocalTime.of(17, 0), LocalTime.of(19, 0));  // Evening surge

        SurgeTimeChecker checker = new SurgeTimeChecker(config);

        switch (cabType.toLowerCase()) {
            case "mini": return new MiniCab(checker);
            case "sedan": return new SedanCab(checker);
            case "suv": return new SvcCab(checker);
            default: throw new IllegalArgumentException("Invalid cab type: " + cabType);
        }
    }
}
