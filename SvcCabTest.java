import java.time.LocalTime;

public class SvcCabTest {
    public static void main(String[] args) {
        SvcCabStub svcCab = new SvcCabStub();
        testFare(svcCab, 0, 40.0);
        testFare(svcCab, 1, 55.0);
        testFare(svcCab, 5, 90.0);
        testFare(svcCab, 10, 140.0);
    }

    static void testFare(SvcCabStub cab, double distance, double expectedFare) {
        double fare = cab.calculateFare(distance, LocalTime.of(10, 0));
        if (fare == expectedFare) {
            System.out.println("testFare(" + distance + ") passed.");
        } else {
            System.out.println("testFare(" + distance + ") failed. Expected " + expectedFare + " but got " + fare);
        }
    }

    static class SvcCabStub {
        private static final double BASE_FARE = 40.0;
        private static final double PER_KM_RATE = 15.0;
        public double calculateFare(double distance, LocalTime time) {
            return BASE_FARE + (distance * PER_KM_RATE);
        }
    }
}
