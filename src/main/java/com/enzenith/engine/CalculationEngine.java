package com.enzenith.engine;

import java.math.BigDecimal;
import java.util.HashMap;


public class CalculationEngine {

    /**
     * 公式计算
     *
     * @param model     计算模型
     * @param variables 模型参数
     * @return java.math.BigDecimal
     * @author LinShuPeng
     * @date 2021-02-02  11:22
     **/
    public static BigDecimal evaluate(CalculationModel model, ModelVariables variables) {
        BigDecimal result = null;
        for (CalculationStep step : model.getCalculationSteps()) {

            result = ExpressionUtil.evaluate(step.getExpression(), variables.getNumberVariables(), variables.getStringVariables());

            //将公式（按顺序）放入到临时值当中
            if (step.getResultHolder() != null) {
                variables.getNumberVariables().put(step.getResultHolder(), result.toPlainString());
            }
        }

        return result;
    }

    /**
     * 公式计算
     *      从库中寻求 计算结果值 所对应的 数据。
     * @param model     计算模型
     * @param variables 模型参数
     * @param resultCandidates      待匹配结果值
     * @return java.math.BigDecimal
     * @author LinShuPeng
     * @date 2021-02-03  14:37
     **/
    public static String evaluate(CalculationModel model, ModelVariables variables, HashMap<String, String> resultCandidates) {
        String evaluate = evaluate(model, variables).toPlainString();
        return resultCandidates.get(evaluate);
    }
}