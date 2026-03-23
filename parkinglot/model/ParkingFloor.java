package parkinglot.model;

import parkinglot.enums.SlotType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ParkingFloor {
    private final int floorNumber;
    private final List<ParkingSlot> slots;

    public ParkingFloor(int floorNumber) {
        this.floorNumber = floorNumber;
        this.slots = new ArrayList<>();
    }

    public void addSlot(ParkingSlot slot) {
        if (slot.getFloorNumber() != floorNumber) {
            throw new IllegalArgumentException("Slot floor number does not match parking floor");
        }
        slots.add(slot);
    }

    public List<ParkingSlot> getAvailableSlots(SlotType type) {
        return slots.stream()
                .filter(slot -> slot.getSlotType() == type && !slot.isOccupied())
                .collect(Collectors.toList());
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public List<ParkingSlot> getSlots() {
        return slots;
    }
}