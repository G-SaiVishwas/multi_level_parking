package parkinglot;

import parkinglot.core.ParkingLotManager;
import parkinglot.enums.SlotType;
import parkinglot.enums.VehicleType;
import parkinglot.model.Ticket;
import parkinglot.model.Vehicle;
import parkinglot.parser.ParkingLotParser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        ParkingLotParser parser = new ParkingLotParser();
        ParkingLotParser.ParkingLotConfig config = buildConfig();
        ParkingLotManager manager = parser.parse(config);

        LocalDate date = LocalDate.of(2026, 3, 23);

        Ticket ticket1 = manager.park(
                new Vehicle("MH12AB1234", VehicleType.TWO_WHEELER),
                LocalDateTime.of(date, LocalTime.of(10, 0)),
                SlotType.SMALL,
                "G1"
        );
        System.out.println("1) Parked TWO_WHEELER via G1 requesting SMALL at 10:00 AM");
        System.out.println("   " + ticket1);

        Ticket ticket2 = manager.park(
                new Vehicle("MH14XY5678", VehicleType.CAR),
                LocalDateTime.of(date, LocalTime.of(10, 30)),
                SlotType.MEDIUM,
                "G2"
        );
        System.out.println("2) Parked CAR via G2 requesting MEDIUM at 10:30 AM");
        System.out.println("   " + ticket2);

        Ticket ticket3 = manager.park(
                new Vehicle("MH01ZZ9999", VehicleType.BUS),
                LocalDateTime.of(date, LocalTime.of(11, 0)),
                SlotType.LARGE,
                "G3"
        );
        System.out.println("3) Parked BUS via G3 requesting LARGE at 11:00 AM");
        System.out.println("   " + ticket3);

        int smallBeforeExit = manager.getStatus(SlotType.SMALL);
        System.out.println("4) Available SMALL slots: " + smallBeforeExit + " (expected 4)");

        double twoWheelerFee = manager.exit(ticket1.getTicketId(), LocalDateTime.of(date, LocalTime.of(12, 15)));
        System.out.println("5) Exit TWO_WHEELER at 12:15 PM, fee: " + twoWheelerFee + " (expected 60.0)");

        double carFee = manager.exit(ticket2.getTicketId(), LocalDateTime.of(date, LocalTime.of(13, 45)));
        System.out.println("6) Exit CAR at 1:45 PM, fee: " + carFee + " (expected 160.0)");

        int smallAfterExit = manager.getStatus(SlotType.SMALL);
        System.out.println("7) Available SMALL slots after exits: " + smallAfterExit + " (expected 5)");

        // Keep this referenced so the variable isn't misleadingly unused in this scenario.
        if (ticket3.getTicketId() == null) {
            System.out.println("Unexpected state");
        }
    }

    private static ParkingLotParser.ParkingLotConfig buildConfig() {
        ParkingLotParser.ParkingLotConfig config = new ParkingLotParser.ParkingLotConfig();

        config.gates.add(new ParkingLotParser.GateConfig("G1", "North Entry"));
        config.gates.add(new ParkingLotParser.GateConfig("G2", "South Entry"));
        config.gates.add(new ParkingLotParser.GateConfig("G3", "East Entry"));

        ParkingLotParser.FloorConfig floor1 = new ParkingLotParser.FloorConfig(1);
        floor1.slots.add(new ParkingLotParser.SlotConfig("F1-S1", SlotType.SMALL, distances(10, 80, 50)));
        floor1.slots.add(new ParkingLotParser.SlotConfig("F1-S2", SlotType.SMALL, distances(15, 75, 45)));
        floor1.slots.add(new ParkingLotParser.SlotConfig("F1-S3", SlotType.SMALL, distances(25, 60, 35)));
        floor1.slots.add(new ParkingLotParser.SlotConfig("F1-M1", SlotType.MEDIUM, distances(20, 55, 40)));
        floor1.slots.add(new ParkingLotParser.SlotConfig("F1-M2", SlotType.MEDIUM, distances(30, 50, 30)));
        floor1.slots.add(new ParkingLotParser.SlotConfig("F1-L1", SlotType.LARGE, distances(40, 40, 20)));

        ParkingLotParser.FloorConfig floor2 = new ParkingLotParser.FloorConfig(2);
        floor2.slots.add(new ParkingLotParser.SlotConfig("F2-S1", SlotType.SMALL, distances(60, 20, 55)));
        floor2.slots.add(new ParkingLotParser.SlotConfig("F2-S2", SlotType.SMALL, distances(65, 15, 60)));
        floor2.slots.add(new ParkingLotParser.SlotConfig("F2-M1", SlotType.MEDIUM, distances(70, 25, 50)));
        floor2.slots.add(new ParkingLotParser.SlotConfig("F2-M2", SlotType.MEDIUM, distances(75, 30, 45)));
        floor2.slots.add(new ParkingLotParser.SlotConfig("F2-M3", SlotType.MEDIUM, distances(80, 35, 40)));
        floor2.slots.add(new ParkingLotParser.SlotConfig("F2-L1", SlotType.LARGE, distances(85, 45, 35)));
        floor2.slots.add(new ParkingLotParser.SlotConfig("F2-L2", SlotType.LARGE, distances(90, 50, 30)));

        config.floors.add(floor1);
        config.floors.add(floor2);

        return config;
    }

    private static Map<String, Double> distances(double g1, double g2, double g3) {
        Map<String, Double> distances = new HashMap<>();
        distances.put("G1", g1);
        distances.put("G2", g2);
        distances.put("G3", g3);
        return distances;
    }
}