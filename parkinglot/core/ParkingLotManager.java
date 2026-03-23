package parkinglot.core;

import parkinglot.enums.SlotType;
import parkinglot.model.Gate;
import parkinglot.model.ParkingFloor;
import parkinglot.model.ParkingSlot;
import parkinglot.model.Ticket;
import parkinglot.model.Vehicle;
import parkinglot.pricing.HourlyPricingStrategy;
import parkinglot.pricing.PricingStrategy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ParkingLotManager {
    private static ParkingLotManager instance;

    private final List<ParkingFloor> floors;
    private final Map<String, Gate> gates;
    private final PricingStrategy pricingStrategy;
    private final Map<String, Ticket> activeTickets;

    private ParkingLotManager(PricingStrategy pricingStrategy) {
        this.floors = new ArrayList<>();
        this.gates = new HashMap<>();
        this.pricingStrategy = pricingStrategy;
        this.activeTickets = new HashMap<>();
    }

    public static ParkingLotManager getInstance() {
        if (instance == null) {
            instance = new ParkingLotManager(new HourlyPricingStrategy());
        }
        return instance;
    }

    public static ParkingLotManager getInstance(PricingStrategy strategy) {
        if (instance == null) {
            instance = new ParkingLotManager(strategy);
        }
        return instance;
    }

    public void addFloor(ParkingFloor floor) {
        floors.add(floor);
    }

    public void addGate(Gate gate) {
        gates.put(gate.getGateId(), gate);
    }

    public Ticket park(Vehicle vehicle, LocalDateTime entryTime, SlotType slotType, String gateId) {
        if (!gates.containsKey(gateId)) {
            throw new IllegalArgumentException("Unknown gate id: " + gateId);
        }

        Optional<ParkingSlot> nearestSlot = floors.stream()
                .flatMap(floor -> floor.getAvailableSlots(slotType).stream())
                .min((slot1, slot2) -> Double.compare(slot1.getDistanceTo(gateId), slot2.getDistanceTo(gateId)));

        ParkingSlot selectedSlot = nearestSlot
                .orElseThrow(() -> new RuntimeException("No available slots for requested type"));

        // TODO: lock this section if multiple entry gates can allocate in parallel.
        selectedSlot.occupy();
        Ticket ticket = new Ticket(vehicle, selectedSlot, gateId, entryTime);
        activeTickets.put(ticket.getTicketId(), ticket);
        return ticket;
    }

    public double exit(String ticketId, LocalDateTime exitTime) {
        Ticket ticket = activeTickets.get(ticketId);
        if (ticket == null) {
            // TODO: currently this treats re-exit and bad-id exactly the same.
            throw new IllegalArgumentException("Invalid or inactive ticket id: " + ticketId);
        }

        double fee = pricingStrategy.calculate(ticket.getEntryTime(), exitTime, ticket.getSlot().getSlotType());
        ticket.getSlot().vacate();
        activeTickets.remove(ticketId);
        return fee;
    }

    public int getStatus(SlotType slotType) {
        long count = floors.stream()
                .flatMap(floor -> floor.getSlots().stream())
                .filter(slot -> slot.getSlotType() == slotType && !slot.isOccupied())
                .count();
        return (int) count;
    }
}