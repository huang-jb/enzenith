package com.enzenith.engine;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 公式计算单元测试
 */
class ExpressionUtilTest {

    static String testFunc(String var0, String var1) {
        BigDecimal val;
        switch (var0) {
            case "一": val = new BigDecimal("100"); break;
            case "二": val = new BigDecimal("200"); break;
            case "三": val = new BigDecimal("300.05"); break;
            default: val = BigDecimal.ZERO;
        }
        return val.add(new BigDecimal(var1)).toPlainString();
    }

    static String getRandomVar0() {
        int min = 0;
        int  max = 3;

        var vals = "一,二,三,四";
        int random = (int)(Math.random() * (max - min + 1) + min);

        return vals.split(",")[random];
    }

    static String getRandomVar1() {
        long min = 50;
        long max = 10000;

        long random = (long)(Math.random() * (max - min + 1) + min);
        return String.valueOf(random);
    }

    static void testExample() {
        var fn = "CASE(var0,\"一,二,三\", \"100,200,300.05,0\") + var1";
        var numVars = new HashMap<String, String>();
        numVars.put("var1", "200");
        var strVars = new HashMap<String, String>();
        strVars.put("var0", "一");   //var = 1 获取到100 ，在加上var1 ==> var1 + 200 = 300
        assertTrue(ExpressionUtil.evaluate(fn, numVars, strVars).toPlainString().equals("300"));
        strVars = new HashMap<>();
        strVars.put("var0", "三");
        assertTrue(ExpressionUtil.evaluate(fn, numVars, strVars).toPlainString().equals("500.05"));
        strVars = new HashMap<>();
        strVars.put("var0", "四");
        assertTrue(ExpressionUtil.evaluate(fn, numVars, strVars).toPlainString().equals("200"));
    }



    @Test
    void evalFormula() {
        testExample();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            var fn = "CASE(var0,\"一，二，三\", \"100,200,300.05,0\") + var1";
            var numVars = new HashMap<String, String>();

            var var1 = getRandomVar1();
            numVars.put("var1", var1);
            var strVars = new HashMap<String, String>();
            var var0 = getRandomVar0();
            strVars.put("var0", var0);


            var result0 = ExpressionUtil.evaluate(fn, numVars, strVars).toPlainString();
            var result1 = testFunc(var0, var1);

//            System.out.println(result0);
//            System.out.println(result1);

            assertTrue(result0.equals(result1));
        }
        long estimatedTime = System.currentTimeMillis() - startTime;

        System.out.println(estimatedTime / 1000.0);

    }

    @Test
    void testStrEq() {
        var fn = "var0 == var1";
        String result0;
        HashMap<String, String> strVars;

        strVars = new HashMap<>();
        strVars.put("var0", "text1");
        strVars.put("var1", "text1");
        result0 = ExpressionUtil.evaluate(fn, null, strVars).toPlainString();
        System.out.println(result0);
        assertTrue(result0.equals("1"));

        strVars = new HashMap<>();
        strVars.put("var0", "text1");
        strVars.put("var1", "text11");
        result0 = ExpressionUtil.evaluate(fn, null, strVars).toPlainString();
        System.out.println(result0);
        assertTrue(result0.equals("0"));
    }

    void t1(HashMap<String, String> strVars) {
        String result0;
        result0 = ExpressionUtil.evaluate("YEAR(var0)", null, strVars).toPlainString();
        assertTrue(result0.equals("2015"));
        result0 = ExpressionUtil.evaluate("MONTH(var0)", null, strVars).toPlainString();
//        System.out.println(result0);
        assertTrue(result0.equals("12"));
        result0 = ExpressionUtil.evaluate("DAY(var0)", null, strVars).toPlainString();
//        System.out.println(result0);
        assertTrue(result0.equals("30"));

    }
    @Test
    void testDate() {
        t1(new HashMap<>() {{ put("var0", "20151230"); }});
        t1(new HashMap<>() {{ put("var0", "2015-12-30"); }});
        t1(new HashMap<>() {{ put("var0", "2015/12/30"); }});
        t1(new HashMap<>() {{ put("var0", "2015/12-30"); }});
        t1(new HashMap<>() {{ put("var0", "201512-30"); }});
        t1(new HashMap<>() {{ put("var0", "2015/1230"); }});
    }

    @Test
    void testIn() {
        var fn = "IN(var0, var1)";
        String result0;
        HashMap<String, String> strVars;

        strVars = new HashMap<>();
        strVars.put("var0", "text1");
        strVars.put("var1", "text0,text1,text2");
        result0 = ExpressionUtil.evaluate(fn, null, strVars).toPlainString();
        assertTrue(result0.equals("1"));

        strVars = new HashMap<>();
        strVars.put("var0", "text3");
        strVars.put("var1", "text0,text1,text2");
        result0 = ExpressionUtil.evaluate(fn, null, strVars).toPlainString();
        assertTrue(result0.equals("0"));

        strVars.put("var0", "一");
        strVars.put("var1", "三，二，一，零");
        result0 = ExpressionUtil.evaluate(fn, null, strVars).toPlainString();
        assertTrue(result0.equals("1"));
    }

    @Test
    void test1() throws Exception {
        List<String> formulae = new ArrayList<>();
        formulae.add("if(var0 && var1 && var2, 1, 0)");
        formulae.add("CASE(var3,\"一,二,三\", \"100,200,300.05,0\") + CASE(var4,\"一,二,三\", \"100,200,300.05,0\") + CASE(var5,\"一,二,三\", \"100,200,300.05,0\")");

        List<String> resultHolders = new ArrayList<>();
        resultHolders.add("temp0");

        HashMap<String, String> variables = new HashMap<>();
        variables.put("var0", "1");
        variables.put("var1", "1");
        variables.put("var2", "1");
        variables.put("var3", "一");
        variables.put("var4", "一");
        variables.put("var5", "一");


        CalculationModel calculationModel = CalculationModel.create(formulae, resultHolders);
        BigDecimal result = CalculationEngine.evaluate(calculationModel, ModelVariables.create(variables));
        System.out.println(result);


//        var fn = "CASE(var0,\"一,二,三\", \"100,200,300.05,0\") + CASE(var1,\"一,二,三\", \"100,200,300.05,0\") + CASE(var2,\"一,二,三\", \"100,200,300.05,0\")";
////        var numVars = new HashMap<String, String>();
////        numVars.put("var1", "200");
//        var strVars = new HashMap<String, String>();
//        strVars.put("var0", "一");   //var = 1 获取到100 ，在加上var1 ==> var1 + 200 = 300
//        strVars.put("var1", "1");
//        strVars.put("var2", "2");
//        assertTrue(ExpressionUtil.evaluate(fn, null, strVars).toPlainString().equals("100"));
    }

}