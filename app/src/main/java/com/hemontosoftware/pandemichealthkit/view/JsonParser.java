package com.hemontosoftware.pandemichealthkit.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonParser {
    private HashMap<String, String> parseJsonObject(JSONObject object) {
        String str = "lng";
        String str2 = "lat";
        String str3 = "location";
        String str4 = "geometry";
        String str5 = "name";
        HashMap<String, String> dataList = new HashMap<>();
        try {
            String name = object.getString(str5);
            String latitude = object.getJSONObject(str4).getJSONObject(str3).getString(str2);
            String longitude = object.getJSONObject(str4).getJSONObject(str3).getString(str);
            dataList.put(str5, name);
            dataList.put(str2, latitude);
            dataList.put(str, longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataList;
    }

    private List<HashMap<String, String>> parseJsonArray(JSONArray jsonArray) {
        List<HashMap<String, String>> dataList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                dataList.add(parseJsonObject((JSONObject) jsonArray.get(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return dataList;
    }

    public List<HashMap<String, String>> parseResult(JSONObject object) {
        JSONArray jsonArray = null;
        try {
            jsonArray = object.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return parseJsonArray(jsonArray);
    }
}
