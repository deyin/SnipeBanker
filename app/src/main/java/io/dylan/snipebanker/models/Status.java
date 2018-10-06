package io.dylan.snipebanker.models;

import android.arch.persistence.room.Ignore;

public enum Status {

    UNKNOWN(-1, "Unknown"),

    NOT_STARTED(0, "Not started yet"),

    IN_PROGRESS(1, "In progress"),

    FINISHED(2, "Finished");

    int code;
    String description;

    @Ignore
    Status(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static Status fromCode(int code) {
        switch (code) {
            case 0:
                return NOT_STARTED;
            case 1:
                return IN_PROGRESS;
            case 2:
                return FINISHED;
            default:
                return UNKNOWN;
        }
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }

} // end enum Status