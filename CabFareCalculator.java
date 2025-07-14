import java.time.LocalTime;
public interface CabFareCalculator {
    double calculateFare(double distanceInKm, LocalTime time);
}
