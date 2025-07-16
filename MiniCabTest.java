import java.time.LocalTime;

public class MiniCabTest {
    public static void main(String[] args) {
        MiniCabStub miniCab = new MiniCabStub();
        testFare(miniCab, 0, 30.0);
        testFare(miniCab, 1, 40.0);
        testFare(miniCab, 5, 80.0);
        testFare(miniCab, 10, 130.0);
    }

    static void testFare(MiniCabStub cab, double distance, double expectedFare) {
        double fare = cab.calculateFare(distance, LocalTime.of(10, 0));
        if (fare == expectedFare) {
            System.out.println("testFare(" + distance + ") passed.");
        } else {
            System.out.println("testFare(" + distance + ") failed. Expected " + expectedFare + " but got " + fare);
        }
    }

    static class MiniCabStub {
        private static final double BASE_FARE = 30.0;
        private static final double PER_KM_RATE = 10.0;
        public double calculateFare(double distance, LocalTime time) {
            return BASE_FARE + (distance * PER_KM_RATE);
        }
    }
}
