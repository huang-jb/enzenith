package com.enzenith.engine;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * 解析入参
 *      将入参进行解析成 字符串型 以及 数字型 在进行返回
 */
public class ModelVariables {
    private HashMap<String, String> numberVariables = new HashMap<>();
    private HashMap<String, String> stringVariables = new HashMap<>();

    private ModelVariables() {
    }

    public static ModelVariables create(HashMap<String, String> variables) {
        ModelVariables variable = new ModelVariables();
        for (String key: variables.keySet()) {
            String value = variables.get(key);
            try {
                new BigDecimal(value);
                variable.numberVariables.put(key, value);
            }
            catch (NumberFormatException e) {
                variable.stringVariables.put(key, value);
            }
        }
        return variable;
    }

    public HashMap<String, String> getNumberVariables() {
        return numberVariables;
    }
    public HashMap<String, String> getStringVariables() {
        return stringVariables;
    }
}
