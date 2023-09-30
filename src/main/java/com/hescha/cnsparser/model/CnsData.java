package com.hescha.cnsparser.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class CnsData {
    private Map<String, String> data;
    private Map<String, String> size;
    private Map<String, String> velocity;
    private Map<String, String> movement;
    private List<Statedef> statedefList;
}
