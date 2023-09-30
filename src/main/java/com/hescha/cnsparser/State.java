package com.hescha.cnsparser;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class State {
    private Integer number;
    private String type;
    private Map<String, String> data = new HashMap<>();

    public void putData(String key, String value) {
        data.put(key, value);
    }
}
