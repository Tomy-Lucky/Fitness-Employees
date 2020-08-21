package com.arcadegamepark.fitnessemployees.converters;

import androidx.room.TypeConverter;

import com.arcadegamepark.fitnessemployees.pojo.Speciality;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class Converter {

    @TypeConverter
    public String listSpecialityToString(List<Speciality> specialities) {
        return new Gson().toJson(specialities); // We convert our Objects to JSONObject
    }

    @TypeConverter
    public List<Speciality> stringToListSpeciality(String specialityAsString) {
        Gson gson = new Gson();
        ArrayList objects = gson.fromJson(specialityAsString, ArrayList.class); // We convert our JSONArray to ArrayList class Object
        ArrayList<Speciality> specialities = new ArrayList<>();
        for (Object object: objects) {
            specialities.add(gson.fromJson(object.toString(), Speciality.class));
        }
        return specialities;
    }
}
