package parkinglot.model;

public class Gate {
    private final String gateId;
    private final String label;

    public Gate(String gateId, String label) {
        this.gateId = gateId;
        this.label = label;
    }

    public String getGateId() {
        return gateId;
    }

    public String getLabel() {
        return label;
    }
}