import java.time.LocalTime;

public class SedanCabTest {
    public static void main(String[] args) {
        SedanCabStub sedanCab = new SedanCabStub();
        testFare(sedanCab, 0, 50.0);
        testFare(sedanCab, 1, 65.0);
        testFare(sedanCab, 5, 105.0);
        testFare(sedanCab, 10, 155.0);
    }

    static void testFare(SedanCabStub cab, double distance, double expectedFare) {
        double fare = cab.calculateFare(distance, LocalTime.of(10, 0));
        if (fare == expectedFare) {
            System.out.println("testFare(" + distance + ") passed.");
        } else {
            System.out.println("testFare(" + distance + ") failed. Expected " + expectedFare + " but got " + fare);
        }
    }

    static class SedanCabStub {
        private static final double BASE_FARE = 50.0;
        private static final double PER_KM_RATE = 15.0;
        public double calculateFare(double distance, LocalTime time) {
            return BASE_FARE + (distance * PER_KM_RATE);
        }
    }
}
