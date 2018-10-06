package io.dylan.snipebanker.persist.converters;

import android.arch.persistence.room.TypeConverter;
import android.support.annotation.NonNull;

import io.dylan.snipebanker.models.Status;

public class StatusConverter {

    @TypeConverter
    public static Status fromCode(int code) {
        return Status.fromCode(code);
    }

    @TypeConverter
    public static int toCode(@NonNull Status status) {
        return status.getCode();
    }
}
