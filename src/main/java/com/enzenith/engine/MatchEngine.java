package com.enzenith.engine;


import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.parser.ParseException;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 匹配引擎
 */
public class MatchEngine {

    /**
     * 解析Eval结果
     *
     * @param result 输出结果，true 满足，false 不满足
     * @return java.lang.Boolean
     * @author LinShuPeng
     * @date 2021-02-02  16:17
     **/
    private static Boolean parseEvalResult(BigDecimal result) {
        return !result.toPlainString().equals("0");
    }

    /**
     * 计算是否匹配
     * 所要匹配的数据必须满足所有必要条件，否则一致视为不匹配处理
     *
     * @param model     基础匹配模型
     * @param variables 模型参数
     * @return com.enzenith.engine.MatchResult
     * @author LinShuPeng
     * @date 2021-02-02  16:19
     **/
    public static MatchResult evaluate(BasicMatchModel model, ModelVariables variables) throws EvaluationException, ParseException {
        //验证 模型参数 是否满足 强制条件（必要条件）
        Boolean mandatory = parseEvalResult(CalculationEngine.evaluate(model.getMandatoryCondition(), variables));

        //验证 模型参数 是否满足 额外条件（非必要条件）
        Boolean additional = true;
        //如果含有非必要条件，进行匹配是否满足
        if (model.getAdditionalCondition() != null) {
            additional = parseEvalResult(CalculationEngine.evaluate(model.getAdditionalCondition(), variables));
        }
        //必要条件  和 额外条件（非必要条件）  都满足
        if (mandatory && additional)
            return MatchResult.HIGH;        //匹配度高

        if (mandatory)
            return MatchResult.LOW;         //匹配度低

        return MatchResult.NONE;            //不匹配
    }

    /**
     * 计算匹配率
     *
     * @param model     匹配模型（匹配 所需要的条件）
     * @param variables 模型参数（用户所提供的参数）
     * @return java.math.BigDecimal
     * @author LinShuPeng
     * @date 2021-02-02  16:28
     **/
    public static BigDecimal evaluate(WeightedMatchModel model, ModelVariables variables) {
        //总量
        BigDecimal totalWeight = BigDecimal.ZERO;
        //净重（满足条件的数量）
        BigDecimal actualWeight = BigDecimal.ZERO;
        //遍历条件，将条件与所对应的信息数据进行判别是否满足
        for (WeightedCondition condition : model.getConditions()) {
            //获取单个条件所在的权重
            totalWeight = totalWeight.add(condition.getWeight());

            //判断所提供的信息是否会满足条件 ==》 满足 result = true， 不满足 result = false
            Boolean result = null;
            result = parseEvalResult(CalculationEngine.evaluate(condition.getCondition(), variables));
            if (result) {
                //将有满足条件的放入净重
                actualWeight = actualWeight.add(condition.getWeight());
            }
        }
        //占重比（匹配率） = 净重（满足条件的） / 总量
        return actualWeight.divide(totalWeight, 2, RoundingMode.DOWN);
    }


    /**
     * 计算匹配率
     *      返回 匹配率 区间 以及匹配率
     *
     * @param model         匹配模型（匹配 所需要的条件）
     * @param variables     模型参数（用户所提供的参数）
     * @param weightedRange 匹配区间层度 （如：0.00 —— 0.20，0.00 —— 0.70，0.70 —— 1.00）
     * @return com.enzenith.engine.WeightedRange
     * @author LinShuPeng
     * @date 2021-02-03  15:26
     **/
    public static WeightedRange evaluate(WeightedMatchModel model, ModelVariables variables, WeightedRange weightedRange) {
        //计算结果
        BigDecimal result = evaluate(model, variables);
        //判断计算结果在什么区间
        //a.compareTo(b) > -1  ==>  a >= b
        if (result.compareTo(new BigDecimal(weightedRange.getUpperBound())) > -1) {
            return WeightedRange.create(weightedRange.getUpperBound(), result.toPlainString(), result.toPlainString());
        }
        //a.compareTo(b) == -1  ==> a < b
        else if (result.compareTo(new BigDecimal(weightedRange.getLowerBound())) == -1) {

            return WeightedRange.create(BigDecimal.ZERO.setScale(2).toPlainString(), weightedRange.getLowerBound(), result.toPlainString());
        }
        return WeightedRange.create(weightedRange.getLowerBound(), weightedRange.getUpperBound(), result.toPlainString());
    }

}