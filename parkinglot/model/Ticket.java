package parkinglot.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Ticket {
    private final String ticketId;
    private final Vehicle vehicle;
    private final ParkingSlot slot;
    private final String gateId;
    private final LocalDateTime entryTime;

    public Ticket(Vehicle vehicle, ParkingSlot slot, String gateId, LocalDateTime entryTime) {
        this.ticketId = UUID.randomUUID().toString();
        this.vehicle = vehicle;
        this.slot = slot;
        this.gateId = gateId;
        this.entryTime = entryTime;
    }

    public String getTicketId() {
        return ticketId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public ParkingSlot getSlot() {
        return slot;
    }

    public String getGateId() {
        return gateId;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    @Override
    public String toString() {
        return "Ticket{"
                + "ticketId='" + ticketId + '\''
                + ", vehicle=" + vehicle
                + ", slot=" + slot.getSlotId()
                + ", gateId='" + gateId + '\''
                + ", entryTime=" + entryTime
                + '}';
    }
}