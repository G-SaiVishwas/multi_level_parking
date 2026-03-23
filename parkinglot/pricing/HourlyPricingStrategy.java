package parkinglot.pricing;

import parkinglot.enums.SlotType;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.EnumMap;
import java.util.Map;

public class HourlyPricingStrategy implements PricingStrategy {
    private final Map<SlotType, Double> rates;

    public HourlyPricingStrategy() {
        this.rates = new EnumMap<>(SlotType.class);
        this.rates.put(SlotType.SMALL, 20.0);
        this.rates.put(SlotType.MEDIUM, 40.0);
        this.rates.put(SlotType.LARGE, 100.0);
    }

    public HourlyPricingStrategy(Map<SlotType, Double> rates) {
        this.rates = new EnumMap<>(SlotType.class);
        this.rates.putAll(rates);
    }

    @Override
    public double calculate(LocalDateTime entryTime, LocalDateTime exitTime, SlotType slotType) {
        long minutes = ChronoUnit.MINUTES.between(entryTime, exitTime);
        if (minutes < 0) {
            throw new IllegalArgumentException("Exit time cannot be before entry time");
        }

        // ceil so partial hours don't go unpaid
        double hours = Math.ceil(minutes / 60.0);
        Double rate = rates.get(slotType);
        if (rate == null) {
            throw new IllegalStateException("No pricing rate configured for slot type: " + slotType);
        }
        return hours * rate;
    }
}