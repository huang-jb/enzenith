package com.enzenith.engine;

//import com.udojava.evalex.Expression;
//import com.udojava.evalex.LazyFunction;

import com.enzenith.engine.functions.*;
import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.config.ExpressionConfiguration;
import com.ezylang.evalex.parser.ParseException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 *  表达式工具
 */
public class ExpressionUtil {

    private static ExpressionConfiguration configuration = ExpressionConfiguration.defaultConfiguration()
                    .withAdditionalFunctions(
                            Map.entry("CASE", new CaseFunction()),
                            Map.entry("YEAR", new YearFunction()),
                            Map.entry("MONTH", new MonthFunction()),
                            Map.entry("DAY", new DayFunction()),
                            Map.entry("IN", new InFunction())
                    );

    /**
     * 公式整合
     * @param expression       自定义公式
     * @param numberVariables       数字型 var 变量
     * @param stringVariables       字符串型 var 变量
     * @return java.math.BigDecimal
     * @author LinShuPeng
     * @date 2021-02-01  16:47
     **/
    public static BigDecimal evaluate(String expression, HashMap<String, String> numberVariables, HashMap<String, String> stringVariables) {
        //字符串型 var 变量，如果不为空进行以下处理
        if (stringVariables != null) {
            for (String key : stringVariables.keySet()) {
                String value = stringVariables.get(key);
                expression = expression.replaceAll(key, "\"" + value + "\"");
            }
        }

        //将自定义公式放入 公式库中
        Expression expr = new Expression(expression, configuration);

        //数字型 var 变量，如果不为空进行一下处理
        if (numberVariables != null) {
            for (String key : numberVariables.keySet()) {
                expr.with(key, new BigDecimal(numberVariables.get(key)));
            }
        }

//        //遍历自定义方法，并放入方法库中
//        for (LazyFunction func: LazyFunctions.Functions) {
//            expr.addLazyFunction(func);
//        }
//        ExpressionConfiguration configuration =
//                ExpressionConfiguration.defaultConfiguration()
//                        .withAdditionalFunctions(
//                                Map.entry("MAX_VALUE", new TestFunction()));


        try {
            return expr.evaluate().getNumberValue();
        } catch (EvaluationException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }
}
