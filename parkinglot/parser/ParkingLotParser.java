package parkinglot.parser;

import parkinglot.core.ParkingLotManager;
import parkinglot.enums.SlotType;
import parkinglot.model.Gate;
import parkinglot.model.ParkingFloor;
import parkinglot.model.ParkingSlot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParkingLotParser {

    public ParkingLotManager parse(ParkingLotConfig config) {
        ParkingLotManager manager = ParkingLotManager.getInstance();

        for (GateConfig gateConfig : config.gates) {
            manager.addGate(new Gate(gateConfig.gateId, gateConfig.label));
        }

        for (FloorConfig floorConfig : config.floors) {
            ParkingFloor floor = new ParkingFloor(floorConfig.floorNumber);
            for (SlotConfig slotConfig : floorConfig.slots) {
                floor.addSlot(new ParkingSlot(
                        slotConfig.slotId,
                        slotConfig.slotType,
                        floorConfig.floorNumber,
                        slotConfig.distances
                ));
            }
            manager.addFloor(floor);
        }

        return manager;
    }

    public static class ParkingLotConfig {
        public List<GateConfig> gates = new ArrayList<>();
        public List<FloorConfig> floors = new ArrayList<>();
    }

    public static class GateConfig {
        public String gateId;
        public String label;

        public GateConfig(String gateId, String label) {
            this.gateId = gateId;
            this.label = label;
        }
    }

    public static class FloorConfig {
        public int floorNumber;
        public List<SlotConfig> slots = new ArrayList<>();

        public FloorConfig(int floorNumber) {
            this.floorNumber = floorNumber;
        }
    }

    public static class SlotConfig {
        public String slotId;
        public SlotType slotType;
        public Map<String, Double> distances;

        public SlotConfig(String slotId, SlotType slotType, Map<String, Double> distances) {
            this.slotId = slotId;
            this.slotType = slotType;
            this.distances = distances;
        }
    }
}