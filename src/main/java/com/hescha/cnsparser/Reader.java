package com.hescha.cnsparser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Reader {
    public static void main(String[] args) throws Exception {
        boolean isReadingStateBlock = false;
        Map<String, String> data = new HashMap<>();
        Map<String, String> size = new HashMap<>();
        Map<String, String> velocity = new HashMap<>();
        Map<String, String> movement = new HashMap<>();
        List<Statedef> statedefList = new ArrayList<>();

        Map<String, String> tempMap = null;
        Statedef tempStatedef = null;
        State tempState = null;


        File cnsFile = new File("src/main/resources/DNaruto.cns");
        Scanner sc = new Scanner(cnsFile);
        String line;
        int ie = 1;
        while (sc.hasNext()) {
            line = clearLine(sc.nextLine());
            System.out.println(ie++);
            System.out.println(line);

            if (line.startsWith("[")) {
                if (line.toLowerCase().startsWith("[stat")) {
                    line=line.toLowerCase();
                    if (line.toLowerCase().startsWith("[statedef")) {
                        String number = line.replace("[statedef ", "").replace("]", "");
                        int statedefNumber = Integer.parseInt(number.trim());
                        tempStatedef = new Statedef();
                        tempStatedef.setNumber(statedefNumber);
                        statedefList.add(tempStatedef);
                        tempState = null;
                    } else {
                        String number;
                        String type;
                        String[] split = line.split(",");

                         number = split[0].replace("[state ", "").replace("]", "");
                         type = split.length > 1 ? split[1].replace("]", "") : null;
                        int stateNumber = Integer.parseInt(number.trim());
                        tempState = new State();
                        tempState.setNumber(stateNumber);
                        tempState.setType(type);
                        tempStatedef.addState(tempState);
                    }
                    isReadingStateBlock = true;
                    tempMap = null;
                } else {
                    if (line.equals("[Data]")) {
                        tempMap = data;
                    }
                    if (line.equals("[Size]")) {
                        tempMap = size;
                    }
                    if (line.equals("[Velocity]")) {
                        tempMap = velocity;
                    }
                    if (line.equals("[Movement]")) {
                        tempMap = movement;
                    }
                    isReadingStateBlock = false;
                    tempStatedef = null;
                    tempState = null;
                }
            } else if (line.trim().isEmpty()) {
                continue;
            } else {
                if (isReadingStateBlock) {
                    int i = line.indexOf('=');
                    String substring1 = line.substring(0, i);
                    String substring2 = line.substring(i, line.length());
                    if (tempState == null) {
                        tempStatedef.putData(substring1, substring2);
                    } else {
                        tempState.putData(substring1, substring2);
                    }
                } else {
                    String[] split = line.split(" = ");
                    tempMap.put(split[0], split[1]);
                }
            }

        }

        //print
        System.out.println("Print map data");
        printMap(data);
        System.out.println("\n\n\n");
        System.out.println("Print map size");
        printMap(size);
        System.out.println("\n\n\n");
        System.out.println("Print map velocity");
        printMap(velocity);
        System.out.println("\n\n\n");
        System.out.println("Print map movement");
        printMap(movement);
        System.out.println("\n\n\n");

        System.out.println(statedefList);
    }

    private static void printMap(Map<String, String> tempMap) {
        tempMap.entrySet()
                .forEach(entry -> System.out.println("Key: " + entry.getKey() + ", value: " + entry.getValue()));
    }

    private static String clearLine(String line) {
        int indexOf = line.indexOf(";");
        if (indexOf == 0) {
            line = "";
        }
        if (indexOf > 0) {
            line = line.substring(0, indexOf);
        }
        line = line.trim();
        return line;
    }
}
/*
если строка начинается с символа [ - значит что это начало блока с данными
нужно проверить является ли это выделенным блоком (Data, Size and etc) или системным State, Statedef И тд
и затем читать данные до пустой строки

если строка началось с пустой строки или не со знака [, то проверяем читали ли до этого какой-либо блок
если блок читали - то в предыдущий (который читали) блок вносим все данные до следующей пустой строки
если блок не читали - просто пропускаем





*/
