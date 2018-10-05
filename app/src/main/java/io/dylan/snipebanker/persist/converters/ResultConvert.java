package io.dylan.snipebanker.persist.converters;

import android.arch.persistence.room.TypeConverter;
import android.support.annotation.NonNull;

import io.dylan.snipebanker.models.Result;

public class ResultConvert {

    @TypeConverter
    public static Result fromCode(String code) {
        return Result.fromCode(code);
    }

    @TypeConverter
    public static String toCode(@NonNull Result rs) {
        return rs.getCode();
    }
}
