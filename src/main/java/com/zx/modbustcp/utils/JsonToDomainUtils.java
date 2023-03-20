package com.zx.modbustcp.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.zx.modbustcp.domain.Json;


import java.io.*;
import java.util.ArrayList;

public class JsonToDomainUtils {
    public static void main(String[] args) {
        readStream();
    }
    public static void readStream() {
        try {
            InputStream stream=new FileInputStream(new File("src/main/resources/json/test.json"));
            JsonReader reader = new JsonReader(new InputStreamReader(stream, "UTF-8"));
            Gson gson = new GsonBuilder().create();

            // Read file in stream mode
            reader.beginArray();
            while (reader.hasNext()) {
                // Read data into object model
                //把JSON格式的字符串轉成对象数组
                String msg;
                ArrayList<Json> list = new Gson().fromJson((String) msg.obj, new TypeToken<ArrayList<Json>>() {
                }.getType());
                Json json = gson.fromJson(reader, Json.class);
                System.out.println(json.toString());
            }
            reader.close();
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
