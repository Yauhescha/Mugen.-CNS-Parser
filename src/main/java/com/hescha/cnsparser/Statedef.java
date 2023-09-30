package com.hescha.cnsparser;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Statedef {
    private Integer number;
    private Map<String, String> data = new HashMap<>();
    private List<State> states = new ArrayList<>();

    public void putData(String key, String value) {
        data.put(key, value);
    }

    public void addState(State state) {
        states.add(state);
    }
}
