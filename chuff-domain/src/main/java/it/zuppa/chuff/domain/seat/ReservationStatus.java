package it.zuppa.chuff.domain.seat;

public enum ReservationStatus {
    PENDING,
    CONFIRMED,
    CANCELLED,
    NO_SHOW, // the consumer didn't show up
    COMPLETED // the consumer show up
}
