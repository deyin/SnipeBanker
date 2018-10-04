package io.dylan.snipebanker.persist.converters;

import android.arch.persistence.room.TypeConverter;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import io.dylan.snipebanker.models.Handicap;
import io.dylan.snipebanker.models.Match;
import io.dylan.snipebanker.models.Odds;

public class JsonObjectConverter {

    static ObjectMapper MAPPER = new ObjectMapper();

    @TypeConverter
    public Match.Team converTeamFromJson(String json) {
        try {
            return MAPPER.readValue(json, Match.Team.class);
        } catch (IOException e) {
            Log.e(JsonObjectConverter.class.getName(), "converFromJson: ", e);
        }
        return null;
    }

    @TypeConverter
    public Odds.OddsChange converOddsChangeFromJson(String json) {
        try {
            return MAPPER.readValue(json, Odds.OddsChange.class);
        } catch (IOException e) {
            Log.e(JsonObjectConverter.class.getName(), "converFromJson: ", e);
        }
        return null;
    }

    @TypeConverter
    public Handicap.HandicapChange converHandicapChangeFromJson(String json) {
        try {
            return MAPPER.readValue(json, Handicap.HandicapChange.class);
        } catch (IOException e) {
            Log.e(JsonObjectConverter.class.getName(), "converFromJson: ", e);
        }
        return null;
    }

    @TypeConverter
    public String converToJson(Object object) {
        try {
            return MAPPER.writeValueAsString(object);
        } catch (IOException e) {
            Log.e(JsonObjectConverter.class.getName(), "converToJson: ", e);
        }
        return null;
    }


}
