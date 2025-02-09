package com.Giga_JAD.Wapi_Wapi.model.blueprint;

public enum BookingStatus {
    BOOKED(6),
    IN_PROGRESS(7),
    COMPLETED(9);

    private final int statusId;

    BookingStatus(int statusId) {
        this.statusId = statusId;
    }

    public int getStatusId() {
        return statusId;
    }

    public static BookingStatus fromId(int id) {
        for (BookingStatus status : values()) {
            if (status.getStatusId() == id) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid status id: " + id);
    }
}