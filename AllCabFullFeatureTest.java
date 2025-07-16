import java.time.LocalTime;

public class AllCabFullFeatureTest {
    public static void main(String[] args) {
        // MiniCab tests
        SurgeTimeCheckerStub miniSurgeChecker = new SurgeTimeCheckerStub();
        MiniCabStub miniCab = new MiniCabStub(miniSurgeChecker);
        System.out.println("MiniCab Tests:");
        testFareBase(miniCab, 10, LocalTime.of(10, 0), 100.0); // Base fare, no surge, no night, normal distance
        testFareSurge(miniCab, 10, LocalTime.of(8, 30), 150.0); // Surge time
        testFareNight(miniCab, 10, LocalTime.of(2, 0), 120.0); // Night time
        testFareLongDistance(miniCab, 201, LocalTime.of(10, 0), 2510.0); // Long distance
        testFareSurgeNight(miniCab, 10, LocalTime.of(1, 0), 180.0); // Both surge and night
        testFareAll(miniCab, 201, LocalTime.of(1, 0), 4518.0); // Surge, night, long distance

        // SedanCab tests
        SurgeTimeCheckerStub sedanSurgeChecker = new SurgeTimeCheckerStub();
        SedanCabStub sedanCab = new SedanCabStub(sedanSurgeChecker);
        System.out.println("\nSedanCab Tests:");
        testFareBase(sedanCab, 10, LocalTime.of(10, 0), 150.0);
        testFareSurge(sedanCab, 10, LocalTime.of(8, 30), 225.0);
        testFareNight(sedanCab, 10, LocalTime.of(2, 0), 180.0);
        testFareLongDistance(sedanCab, 201, LocalTime.of(10, 0), 3010.0);
        testFareSurgeNight(sedanCab, 10, LocalTime.of(1, 0), 270.0);
        testFareAll(sedanCab, 201, LocalTime.of(1, 0), 5418.0);

        // SvcCab tests
        SurgeTimeCheckerStub svcSurgeChecker = new SurgeTimeCheckerStub();
        SvcCabStub svcCab = new SvcCabStub(svcSurgeChecker);
        System.out.println("\nSvcCab Tests:");
        testFareBase(svcCab, 10, LocalTime.of(10, 0), 120.0);
        testFareSurge(svcCab, 10, LocalTime.of(8, 30), 180.0);
        testFareNight(svcCab, 10, LocalTime.of(2, 0), 144.0);
        testFareLongDistance(svcCab, 201, LocalTime.of(10, 0), 2412.0);
        testFareSurgeNight(svcCab, 10, LocalTime.of(1, 0), 216.0);
        testFareAll(svcCab, 201, LocalTime.of(1, 0), 4337.6);
    }

    static void testFareBase(CabStub cab, double distance, LocalTime time, double expectedFare) {
        double fare = cab.calculateFare(distance, time);
        printResult("Base", fare, expectedFare);
    }

    static void testFareSurge(CabStub cab, double distance, LocalTime time, double expectedFare) {
        cab.setSurge(true);
        double fare = cab.calculateFare(distance, time);
        printResult("Surge", fare, expectedFare);
        cab.setSurge(false);
    }

    static void testFareNight(CabStub cab, double distance, LocalTime time, double expectedFare) {
        double fare = cab.calculateFare(distance, time);
        printResult("Night", fare, expectedFare);
    }

    static void testFareLongDistance(CabStub cab, double distance, LocalTime time, double expectedFare) {
        double fare = cab.calculateFare(distance, time);
        printResult("LongDistance", fare, expectedFare);
    }

    static void testFareSurgeNight(CabStub cab, double distance, LocalTime time, double expectedFare) {
        cab.setSurge(true);
        double fare = cab.calculateFare(distance, time);
        printResult("SurgeNight", fare, expectedFare);
        cab.setSurge(false);
    }

    static void testFareAll(CabStub cab, double distance, LocalTime time, double expectedFare) {
        cab.setSurge(true);
        double fare = cab.calculateFare(distance, time);
        printResult("AllFeatures", fare, expectedFare);
        cab.setSurge(false);
    }

    static void printResult(String testName, double fare, double expectedFare) {
        if (fare == expectedFare) {
            System.out.println(testName + " test passed.");
        } else {
            System.out.println(testName + " test failed. Expected " + expectedFare + " but got " + fare);
        }
    }

    // Common stub interface for all cabs
    interface CabStub {
        double calculateFare(double distance, LocalTime time);
        void setSurge(boolean surge);
    }

    // MiniCab stub
    static class MiniCabStub implements CabStub {
        private static final double BASE_RATE = 10.0;
        private final SurgeTimeCheckerStub surgeChecker;
        public MiniCabStub(SurgeTimeCheckerStub surgeChecker) {
            this.surgeChecker = surgeChecker;
        }
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
        public void setSurge(boolean surge) {
            surgeChecker.setSurge(surge);
        }
        private boolean isNightTime(LocalTime time) {
            return time.isAfter(LocalTime.MIDNIGHT) && time.isBefore(LocalTime.of(6, 0));
        }
    }

    // SedanCab stub
    static class SedanCabStub implements CabStub {
        private static final double BASE_RATE = 15.0;
        private final SurgeTimeCheckerStub surgeChecker;
        public SedanCabStub(SurgeTimeCheckerStub surgeChecker) {
            this.surgeChecker = surgeChecker;
        }
        public double calculateFare(double distance, LocalTime time) {
            double fare = distance * BASE_RATE;
            if (surgeChecker.isSurgeTime(time)) {
                fare *= 1.5;
            }
            if (isNightTime(time)) {
                fare *= 1.2;
            }
            if (distance > 200) {
                fare += 1000;
            }
            return fare;
        }
        public void setSurge(boolean surge) {
            surgeChecker.setSurge(surge);
        }
        private boolean isNightTime(LocalTime time) {
            return time.isAfter(LocalTime.MIDNIGHT) && time.isBefore(LocalTime.of(6, 0));
        }
    }

    // SvcCab stub
    static class SvcCabStub implements CabStub {
        private static final double BASE_RATE = 12.0;
        private final SurgeTimeCheckerStub surgeChecker;
        public SvcCabStub(SurgeTimeCheckerStub surgeChecker) {
            this.surgeChecker = surgeChecker;
        }
        public double calculateFare(double distance, LocalTime time) {
            double fare = distance * BASE_RATE;
            if (surgeChecker.isSurgeTime(time)) {
                fare *= 1.5;
            }
            if (isNightTime(time)) {
                fare *= 1.2;
            }
            if (distance > 200) {
                fare += 800;
            }
            return fare;
        }
        public void setSurge(boolean surge) {
            surgeChecker.setSurge(surge);
        }
        private boolean isNightTime(LocalTime time) {
            return time.isAfter(LocalTime.MIDNIGHT) && time.isBefore(LocalTime.of(6, 0));
        }
    }

    // Stub for SurgeTimeChecker
    static class SurgeTimeCheckerStub {
        private boolean surge = false;
        public void setSurge(boolean surge) {
            this.surge = surge;
        }
        public boolean isSurgeTime(LocalTime time) {
            return surge || (time.isAfter(LocalTime.of(8, 0)) && time.isBefore(LocalTime.of(10, 0)));
        }
    }
}
