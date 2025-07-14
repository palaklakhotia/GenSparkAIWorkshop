import java.time.LocalTime;
import java.util.Scanner;

public class UberTicketSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter cab type (Mini, Sedan, SUV): ");
        String cabType = scanner.nextLine();

        System.out.print("Enter distance in km: ");
        double distance = scanner.nextDouble();
        scanner.nextLine(); // clear newline

        System.out.print("Enter travel time (HH:mm): ");
        String timeInput = scanner.nextLine();
        LocalTime time = LocalTime.parse(timeInput);

        try {
            CabFareCalculator cab = CabFareFactory.getCabFareCalculator(cabType);
            double fare = cab.calculateFare(distance, time);

            System.out.println("\n--- Fare Summary ---");
            System.out.println("Cab Type   : " + cabType);
            System.out.println("Distance   : " + distance + " km");
            System.out.println("Time       : " + time);
            System.out.println("Final Fare : ₹" + fare);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        scanner.close();
    }
}
