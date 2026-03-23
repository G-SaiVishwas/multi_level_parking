package parkinglot.pricing;

import parkinglot.enums.SlotType;

import java.time.LocalDateTime;

public interface PricingStrategy {
    double calculate(LocalDateTime entryTime, LocalDateTime exitTime, SlotType slotType);
}