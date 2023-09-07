package com.enzenith.engine;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CalculationEngineTest {

    static BigDecimal testFunc(Boolean var0, long var1) {
        var temp0 = (var0 && var1 > 200) ? var1 * 20 : 0;
        var temp1 = (temp0 * 12 > 300000) ? 300000 : temp0 * 12;
        var result = (temp0 + temp1) * 3;
        return BigDecimal.valueOf(result);
    }

    static long getRandomLong() {
        long min = 50;
        long max = 2000;

        long random = (long)(Math.random() * (max - min + 1) + min);
        return random;
    }

    static BigDecimal exprCalc(Boolean var0, long var1) throws Exception {
        List<String> formulae = new ArrayList<>();

        formulae.add("if(var0 && var1 > 200, var1 * 20, 0)");
        formulae.add("if(temp0 * 12 > 300000, 300000, temp0 * 12)");
        formulae.add("(temp0 + temp1) * 3");

        List<String> resultHolders = new ArrayList<>();
        resultHolders.add("temp0");
        resultHolders.add("temp1");

        HashMap<String, String> variables = new HashMap<>();
        variables.put("var0", var0 ? "1" : "0");
        variables.put("var1", String.valueOf(var1));

        //创建计算模型
        CalculationModel model = CalculationModel.create(formulae, resultHolders);
        return CalculationEngine.evaluate(model, ModelVariables.create(variables));
    }

    @Test
    void testFormulaOne(){
        long startTime = System.currentTimeMillis();

        /*HashMap<String, String> variables = new HashMap<>();
        variables.put("var0", "450");
        var model = CalculationModel.create("if(var0 < 300, 3000, if(var0 > 400, 4000, if(var0 >500, 5000, 0)))");
        var result = CalculationEngine.evaluate(model, ModelVariables.create(variables));
        System.out.println(result.toPlainString());*/
        /*new Expression("if(var0 < 300, 3000, if(var0 > 400, 4000, if(var0 >500, 5000, 0)))")
                .with("var0", "450")
                .eval();*/

        long estimatedTime = System.currentTimeMillis() - startTime;

        System.out.println(estimatedTime / 1000.0);

    }

    @Test
    void testFormula() {
        HashMap<String, String> variables = new HashMap<>();
        variables.put("var0", "100");
        variables.put("var1", "200");
        var model = CalculationModel.create("var0 * 1.01 + var1");
        var result = CalculationEngine.evaluate(model, ModelVariables.create(variables));
        System.out.println(result.toPlainString());
        assertTrue(result.toPlainString().equals("301"));
    }

    @Test
    void evaluate() throws Exception {
        var random = new Random();

        for (int i = 0; i < 1000; i++) {
            var var0 = random.nextBoolean();
            var var1 = getRandomLong();

            var result0 = exprCalc(var0, var1);

            var result1 = testFunc(var0, var1);

            System.out.println(result0.toPlainString());
            System.out.println(result1.toPlainString());

            assertTrue(result0.toPlainString().equals(result1.toPlainString()));

        }
    }

    @Test
    void  resultToMap() throws Exception{
        List<String> formulae = new ArrayList<>();

        formulae.add("if(var0 && var1 > 200, var1 * 20, 0)");
        formulae.add("if(temp0 * 12 > 300000, 300000, temp0 * 12)");
        formulae.add("(temp0 + temp1) * 3");

        List<String> resultHolders = new ArrayList<>();
        resultHolders.add("temp0");
        resultHolders.add("temp1");

        HashMap<String, String> variables = new HashMap<>();
        variables.put("var0", "1");
        variables.put("var1", "201");

        HashMap<String, String> resultCandidates = new HashMap<>();
        resultCandidates.put("156780", "正确测试");
        resultCandidates.put("120000", "想想");

        //创建计算模型
        CalculationModel model = CalculationModel.create(formulae, resultHolders);
        String result = CalculationEngine.evaluate(model, ModelVariables.create(variables), resultCandidates);
        assertTrue(result.equals("正确测试"));
    }


    @Test
    void modelTest() throws Exception {
        //表达式
        List<String> formulae = new ArrayList<>();
        formulae.add("if(var0 > 10, 10, 0)");
        formulae.add("if(var1, 100, 0)");
        formulae.add("temp0 + temp1");

        //临时值存放
        List<String> resultHolders = new ArrayList<>();
        resultHolders.add("temp0");
        resultHolders.add("temp1");

        //动态参数变量
        HashMap<String, String> variables = new HashMap<>();
        variables.put("var0", "10");
        variables.put("var1", "1");

        //创建计算模型
        CalculationModel model = CalculationModel.create(formulae, resultHolders);
        //执行计算引擎
        BigDecimal evaluate = CalculationEngine.evaluate(model, ModelVariables.create(variables));
        assertTrue(evaluate.toPlainString().equals("100"));
    }

}