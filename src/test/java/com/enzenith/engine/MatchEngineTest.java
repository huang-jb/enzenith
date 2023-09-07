package com.enzenith.engine;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 信息匹配单元测试
 */
class MatchEngineTest {
    /**
     * 通用计算权重（匹配率） 比例
     */
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

        conditions = new ArrayList<>();
        conditions.add(CalculationModel.create("var0"));
        conditions.add(CalculationModel.create("var1"));
        conditions.add(CalculationModel.create("var2"));
        variables = new HashMap<>();
        variables.put("var0", "1");
        variables.put("var1", "1");
        variables.put("var2", "0");
        weightedMatchModel = WeightedMatchModel.create(conditions);
        result = MatchEngine.evaluate(weightedMatchModel, ModelVariables.create(variables));
        System.out.println(result.toPlainString());
        assertTrue(result.toPlainString().equals("0.66"));

        List<BigDecimal> weights = new ArrayList<>();
        weights.add(BigDecimal.ONE);
        weights.add(BigDecimal.ONE);
        weights.add(new BigDecimal("2"));

        weightedMatchModel = WeightedMatchModel.create(conditions, weights);
        result = MatchEngine.evaluate(weightedMatchModel, ModelVariables.create(variables));
        System.out.println(result.toPlainString());
        assertTrue(result.toPlainString().equals("0.50"));

        conditions = new ArrayList<>();
        conditions.add(CalculationModel.create("var0"));
        conditions.add(CalculationModel.create("var1"));
        conditions.add(CalculationModel.create("var2"));
        variables = new HashMap<>();
        variables.put("var0", "0");
        variables.put("var1", "0");
        variables.put("var2", "0");
        weightedMatchModel = WeightedMatchModel.create(conditions);
        result = MatchEngine.evaluate(weightedMatchModel, ModelVariables.create(variables));
        System.out.println(result.toPlainString());
        assertTrue(result.toPlainString().equals("0.00"));
    }


    /**
     * 计算匹配程度高低
     */
    @Test
    void evaluate() throws Exception {
        BasicMatchModel model;
        List<String> mandatoryFormulae;
        List<String> mandatoryResultHolders;
        List<String> additionalFormulae;
        List<String> additionalResultHolders;
        HashMap<String, String> variables;
        MatchResult result;

        /**
         * 既有 必要条件匹配 又有 额外条件匹配 情况
         */
        mandatoryFormulae = new ArrayList<>();
        mandatoryResultHolders = new ArrayList<>();
        mandatoryFormulae.add("var0 > 10");
        mandatoryFormulae.add("var1 > 15");
        mandatoryFormulae.add("temp0 && temp1");
        mandatoryResultHolders.add("temp0");
        mandatoryResultHolders.add("temp1");

        additionalFormulae = new ArrayList<>();
        additionalResultHolders = new ArrayList<>();
        additionalFormulae.add("var2 > 20");

        variables = new HashMap<>();
        variables.put("var0", "25");
        variables.put("var1", "20");
        variables.put("var2", "25");
        model = BasicMatchModel.create(mandatoryFormulae, mandatoryResultHolders,
                additionalFormulae, additionalResultHolders);
        //必须满足必要条件，才能进行匹配度判定
        result = MatchEngine.evaluate(model, ModelVariables.create(variables));
        System.out.println(result);
        assertTrue(result.equals(MatchResult.HIGH));

        variables = new HashMap<>();
        variables.put("var0", "15");
        variables.put("var1", "20");
        variables.put("var2", "20");
        model = BasicMatchModel.create(mandatoryFormulae, mandatoryResultHolders,
                additionalFormulae, additionalResultHolders);
        result = MatchEngine.evaluate(model, ModelVariables.create(variables));
        System.out.println(result);
        assertTrue(result.equals(MatchResult.LOW));


        variables = new HashMap<>();
        variables.put("var0", "10");
        variables.put("var1", "20");
        variables.put("var2", "25");
        model = BasicMatchModel.create(mandatoryFormulae, mandatoryResultHolders,
                additionalFormulae, additionalResultHolders);
        result = MatchEngine.evaluate(model, ModelVariables.create(variables));
        System.out.println(result);
        assertTrue(result.equals(MatchResult.NONE));


        mandatoryFormulae = new ArrayList<>();
        mandatoryResultHolders = new ArrayList<>();
        mandatoryFormulae.add("var0 > 10");
        mandatoryFormulae.add("var1 > 15");
        mandatoryFormulae.add("temp0 && temp1");
        mandatoryResultHolders.add("temp0");
        mandatoryResultHolders.add("temp1");

        variables = new HashMap<>();
        variables.put("var0", "15");
        variables.put("var1", "20");
        model = BasicMatchModel.create(mandatoryFormulae, mandatoryResultHolders);
        result = MatchEngine.evaluate(model, ModelVariables.create(variables));
        System.out.println(result);
        assertTrue(result.equals(MatchResult.HIGH));

        variables = new HashMap<>();
        variables.put("var0", "10");
        variables.put("var1", "20");
        model = BasicMatchModel.create(mandatoryFormulae, mandatoryResultHolders);
        result = MatchEngine.evaluate(model, ModelVariables.create(variables));
        System.out.println(result);
        assertTrue(result.equals(MatchResult.NONE));

    }

    /**
     * 匹配率测试
     *  返回匹配率区间以及匹配结果
     */
    @Test
    void weightedRange(){
        ArrayList<CalculationModel> conditions;
        HashMap<String, String> variables;
        WeightedMatchModel weightedMatchModel;
        WeightedRange evaluate;

        conditions = new ArrayList<>();
        conditions.add(CalculationModel.create("var0 > 10"));
        conditions.add(CalculationModel.create("var1 > 20"));
        conditions.add(CalculationModel.create("var2 > 30"));
        conditions.add(CalculationModel.create("var3 > 40"));
        weightedMatchModel = WeightedMatchModel.create(conditions);

        variables = new HashMap<>();
        variables.put("var0", "15");
        variables.put("var1", "25");
        variables.put("var2", "25");
        variables.put("var3", "35");

        //匹配率区间定义
        WeightedRange weightedRange = WeightedRange.create("0.20", "0.70");

        evaluate = MatchEngine.evaluate(weightedMatchModel, ModelVariables.create(variables), weightedRange);
        System.out.println(evaluate.getMatchResult());
        assertTrue(evaluate.getMatchResult().equals("0.50"));

        conditions = new ArrayList<>();
        conditions.add(CalculationModel.create("var0"));
        conditions.add(CalculationModel.create("var1"));
        conditions.add(CalculationModel.create("var2"));
        variables = new HashMap<>();
        variables.put("var0", "1");
        variables.put("var1", "1");
        variables.put("var2", "0");
        weightedMatchModel = WeightedMatchModel.create(conditions);
        evaluate = MatchEngine.evaluate(weightedMatchModel, ModelVariables.create(variables), weightedRange);
        System.out.println(evaluate);
        assertTrue(evaluate.getMatchResult().equals("0.66"));

        List<BigDecimal> weights = new ArrayList<>();
        weights.add(BigDecimal.ONE);
        weights.add(BigDecimal.ONE);
        weights.add(new BigDecimal("2"));

        weightedMatchModel = WeightedMatchModel.create(conditions, weights);
        evaluate = MatchEngine.evaluate(weightedMatchModel, ModelVariables.create(variables), weightedRange);
        System.out.println(evaluate);
        assertTrue(evaluate.getMatchResult().equals("0.50"));


        conditions = new ArrayList<>();
        conditions.add(CalculationModel.create("var0"));
        conditions.add(CalculationModel.create("var1"));
        conditions.add(CalculationModel.create("var2"));
        variables = new HashMap<>();
        variables.put("var0", "0");
        variables.put("var1", "0");
        variables.put("var2", "0");
        weightedMatchModel = WeightedMatchModel.create(conditions);
        evaluate = MatchEngine.evaluate(weightedMatchModel, ModelVariables.create(variables), weightedRange);
        System.out.println(evaluate);
        assertTrue(evaluate.getMatchResult().equals("0.00"));

    }
}