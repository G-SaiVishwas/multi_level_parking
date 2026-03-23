package parkinglot.model;

import parkinglot.enums.SlotType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ParkingSlot {
    private final String slotId;
    private final SlotType slotType;
    private final int floorNumber;
    private boolean occupied;
    private final Map<String, Double> distances;

    public ParkingSlot(String slotId, SlotType slotType, int floorNumber, Map<String, Double> distances) {
        this.slotId = slotId;
        this.slotType = slotType;
        this.floorNumber = floorNumber;
        this.distances = new HashMap<>(distances);
        this.occupied = false;
    }

    public void occupy() {
        this.occupied = true;
    }

    public void vacate() {
        this.occupied = false;
    }

    public double getDistanceTo(String gateId) {
        Double distance = distances.get(gateId);
        if (distance == null) {
            throw new IllegalArgumentException("No distance mapping found for gate: " + gateId);
        }
        return distance;
    }

    public String getSlotId() {
        return slotId;
    }

    public SlotType getSlotType() {
        return slotType;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public Map<String, Double> getDistances() {
        return Collections.unmodifiableMap(distances);
    }
}