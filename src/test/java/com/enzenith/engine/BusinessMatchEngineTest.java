package com.enzenith.engine;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 匹配度计算
 *
 * @author LinShuPeng
 * @date 2021-02-03 9:40
 */
public class BusinessMatchEngineTest {
    @Test
    void evaluateWeight() {
        ArrayList<CalculationModel> conditions;
        HashMap<String, String> variables;
        WeightedMatchModel weightedMatchModel;
        BigDecimal result;

        conditions = new ArrayList<>();
        conditions.add(CalculationModel.create("var0 > 10"));
        conditions.add(CalculationModel.create("var1 > 20"));
        conditions.add(CalculationModel.create("var2 > 30"));
        conditions.add(CalculationModel.create("var3 > 40"));

        variables = new HashMap<>();
        variables.put("var0", "15");
        variables.put("var1", "25");
        variables.put("var2", "35");
        variables.put("var3", "45");
        weightedMatchModel = WeightedMatchModel.create(conditions);

        result = MatchEngine.evaluate(weightedMatchModel, ModelVariables.create(variables));
        System.out.println(result.toPlainString());
        assertTrue(result.toPlainString().equals("1.00"));

    }

}
