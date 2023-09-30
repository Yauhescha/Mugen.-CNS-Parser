package com.hescha.cnsparser.reader;

import com.hescha.cnsparser.model.CnsData;
import com.hescha.cnsparser.model.State;
import com.hescha.cnsparser.model.Statedef;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CnsReader {
    private static final String BLOCK_NAME_DATA = "[data]";
    private static final String BLOCK_NAME_SIZE = "[size]";
    private static final String BLOCK_NAME_VELOCITY = "[velocity]";
    private static final String BLOCK_NAME_MOVEMENT = "[movement]";
    private static final String BLOCK_NAME_STAT = "[stat";
    private static final String BLOCK_NAME_STATEDEF = "[statedef";
    private static final String EMPTY = "";
    private static final String CLOSING_BRACKET = "]";
    private static final String OPENING_BRACKET = "[";
    private static final String START_BLOCK_STATE = "[state ";
    private static final String COMMA = ",";
    private static final String SEMICOLUMN = ";";
    private static final String EQUALS_AND_SPACE = " = ";
    private static final char EQUALS = '=';

    private boolean isReadingStateBlock;
    private Map<String, String> data;
    private Map<String, String> size;
    private Map<String, String> velocity;
    private Map<String, String> movement;
    private List<Statedef> statedefList;

    private Map<String, String> tempMap;
    private Statedef tempStatedef;
    private State tempState;

    public static void main(String[] args) throws Exception {
        File cnsFile = new File("src/main/resources/DNaruto.cns");
        CnsReader reader = new CnsReader();
        CnsData read = reader.read(cnsFile);
        System.out.println(read);
    }

    public CnsData read(File cnsFile) throws Exception {
        clearFields();
        Scanner sc = new Scanner(cnsFile);
        String line;

        while (sc.hasNext()) {
            line = clearLine(sc.nextLine());
            if (line.startsWith(OPENING_BRACKET)) {
                line = line.toLowerCase();
                if (line.startsWith(BLOCK_NAME_STAT)) {
                    chooseStateOtStatedef(line);
                } else {
                    chooseMapBlock(line);
                }
            } else if (line.trim().isEmpty()) {
                continue;
            } else {
                readDataFromLine(line);
            }
        }

        return new CnsData(data, size, velocity, movement, statedefList);
    }

    private void chooseStateOtStatedef(String line) {
        if (line.startsWith(BLOCK_NAME_STATEDEF)) {
            prepareStatedefBlock(line);
        } else {
            prepareStateBlock(line);
        }
        isReadingStateBlock = true;
        tempMap = null;
    }

    private void prepareStatedefBlock(String line) {
        String number = line.replace(BLOCK_NAME_STATEDEF, EMPTY).replace(CLOSING_BRACKET, EMPTY);
        int statedefNumber = Integer.parseInt(number.trim());
        tempStatedef = new Statedef();
        tempStatedef.setNumber(statedefNumber);
        statedefList.add(tempStatedef);
        tempState = null;
    }

    private void prepareStateBlock(String line) {
        String[] split = line.split(COMMA);
        String number = split[0].replace(START_BLOCK_STATE, EMPTY).replace(CLOSING_BRACKET, EMPTY);
        String type = split.length > 1 ? split[1].replace(CLOSING_BRACKET, EMPTY) : null;
        int stateNumber = Integer.parseInt(number.trim());
        tempState = new State();
        tempState.setNumber(stateNumber);
        tempState.setType(type);
        tempStatedef.addState(tempState);
    }

    private void chooseMapBlock(String line) {
        if (line.equals(BLOCK_NAME_DATA)) {
            tempMap = data;
        }
        if (line.equals(BLOCK_NAME_SIZE)) {
            tempMap = size;
        }
        if (line.equals(BLOCK_NAME_VELOCITY)) {
            tempMap = velocity;
        }
        if (line.equals(BLOCK_NAME_MOVEMENT)) {
            tempMap = movement;
        }
        isReadingStateBlock = false;
        tempStatedef = null;
        tempState = null;
    }

    private void clearFields() {
        isReadingStateBlock = false;
        data = new HashMap<>();
        size = new HashMap<>();
        velocity = new HashMap<>();
        movement = new HashMap<>();
        statedefList = new ArrayList<>();

        tempMap = null;
        tempStatedef = null;
        tempState = null;
    }

    private void readDataFromLine(String line) {
        if (isReadingStateBlock) {
            int i = line.indexOf(EQUALS);
            String substring1 = line.substring(0, i);
            String substring2 = line.substring(i);
            if (tempState == null) {
                tempStatedef.putData(substring1, substring2);
            } else {
                tempState.putData(substring1, substring2);
            }
        } else {
            String[] split = line.split(EQUALS_AND_SPACE);
            tempMap.put(split[0], split[1]);
        }
    }

    private String clearLine(String line) {
        int indexOf = line.indexOf(SEMICOLUMN);
        if (indexOf == 0) {
            line = EMPTY;
        }
        if (indexOf > 0) {
            line = line.substring(0, indexOf);
        }
        line = line.trim();
        return line;
    }
}
