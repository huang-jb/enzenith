package com.enzenith.engine.business;

import com.enzenith.engine.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author LinShuPeng
 * @date 2021-04-07 15:15
 */
public class EngineUtil {


    /**
     * 计算金额（有临时值）
     *
     * 例子：
     * “企业技术中心”区级奖励
     *     if (企业与高等院校、科研院所共建国家级研发中心   = True , 500,000   , 0 )
     * +    if (企业与高等院校、科研院所共建省级研发中心    = True , 300,000   , 0 )
     * +   if (企业与高等院校、科研院所共建市级研发中心    = True , 150,000   , 0 )
     * <p>
     * 公式：
     * if(var0 && var1, 500000, 0)
     * +
     * if(var0 && var2, 300000, 0)
     * +
     * if(var0 && var3, 150000, 0)
     * +
     * (temp0+temp1+temp2)
     * <p>
     * <p>
     * 公式中值含义：
     * var0：企业与高等院校
     * var1：科研院所共建国家级研发中心
     * var2: 科研院所共建省级研发中心
     * var3:  科研院所共建市级研发中心
     * temp0：存放表达式1的值
     * temp1：存放表达式2的值
     * temp2: 存放表达式3的值
     * </p>
     *
     * @param formulae          公式集合
     * @param resultHolders     临时值存放
     * @param variables         公式中参数所对应的值
     * @return java.math.BigDecimal
     * @author LinShuPeng
     * @date 2021-04-07  15:20
     **/
    public static BigDecimal calculationAmount(List<String> formulae, List<String> resultHolders, HashMap<String, String> variables){
        try {
            CalculationModel calculationModel =  CalculationModel.create(formulae, resultHolders);
        return  CalculationEngine.evaluate(calculationModel, ModelVariables.create(variables));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    /**
     * 计算金额（无临时值）
     *
     * 例子：
     * 旅游专业人才培养奖励(一线导游工作类）
     *
     * <p>
     * 政策条件：
     * 对考取中级、高级导游证且在我区从事一线导游工作的给予全额补助培训考试费。
     * </p>
     * <p>
     * 公式：
     * if((var0 || var1) && var2, 10000, 0)
     * </p>
     * <p>
     * 公式中值含义：
     * var0：考取中级导游证
     * var1：考取高级导游证
     * var2：在我区从事一线导游工作
     * </p>
     *
     * @param expression         单个公式
     * @param variables         公式中参数所对应的值
     * @return java.math.BigDecimal
     * @author LinShuPeng
     * @date 2021-04-07  15:25
     **/
    public static BigDecimal calculationAmount(String expression, HashMap<String, String> variables){
        try {
            CalculationModel calculationModel = CalculationModel.create(expression);
            return CalculationEngine.evaluate(calculationModel, ModelVariables.create(variables));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    /**
     *
     * 计算占比（适用于是否满足条件匹配）
     * 例子：
     * <p>
     * 公式：formulae
     *      var0 > 10
     *      var1 > 20
     *      var2 > 30
     *      var3 > 40
     * </p>
     * <p>
     *  参数：variables
     *      "var0", "15"
     *      "var1", "25"
     *      "var2", "35"
 *          "var3", "30"
     * </p>
     * 结果： 0.75
     *
     * @param formulae      公示集合
     * @param variables     公式中参数所对应的值
     * @return java.math.BigDecimal
     * @author LinShuPeng
     * @date 2021-04-07  15:32
     **/
    public static BigDecimal calculationMatch(List<String> formulae, HashMap<String, String> variables){
        try {
            List<CalculationModel> conditions = new ArrayList<>();
            formulae.forEach(expression -> conditions.add(CalculationModel.create(expression)));
            WeightedMatchModel weightedMatchModel = WeightedMatchModel.create(conditions);

            return MatchEngine.evaluate(weightedMatchModel, ModelVariables.create(variables));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }


    /**
     *  计算匹配程度高低
     *  满足必要条件 且 全部满足额外条件   精准匹配
     *  满足必要条件 但 不全部满足额外条件  疑似匹配
     *  不满足必要条件 不匹配
     *
     * @param mandatoryFormulae     强制条件（必要条件）公式
     * @param mandatoryResultHolders    强制条件（必要条件）结果持有者（临时存放对象）
     * @param additionalFormulae    额外条件（非必要条件）公式
     * @param additionalResultHolders   额外条件（非必要条件）结果持有者（临时存放对象）
     * @param variables
     * @return com.enzenith.engine.MatchResult
     * @author LinShuPeng
     * @date 2021-04-07  15:47
     **/
    public static MatchResult calculationBasicMatchModel(
            List<String> mandatoryFormulae, List<String> mandatoryResultHolders,
            List<String> additionalFormulae, List<String> additionalResultHolders,
            HashMap<String, String> variables){
        try {
            BasicMatchModel model = BasicMatchModel.create(mandatoryFormulae, mandatoryResultHolders,
                    additionalFormulae, additionalResultHolders);

            return MatchEngine.evaluate(model, ModelVariables.create(variables));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return MatchResult.NONE;
    }

    /**
     *  计算匹配程度高低
     *  满足必要条件  精准匹配
     *  不满足必要条件 不匹配
     *
     * @param mandatoryFormulae     强制条件（必要条件）公式
     * @param mandatoryResultHolders    强制条件（必要条件）结果持有者（临时存放对象）
     * @param variables
     * @return com.enzenith.engine.MatchResult
     * @author LinShuPeng
     * @date 2021-04-07  15:47
     **/
    public static MatchResult calculationBasicMatchModel(
            List<String> mandatoryFormulae, List<String> mandatoryResultHolders,
            HashMap<String, String> variables){
        try {
            BasicMatchModel model = BasicMatchModel.create(mandatoryFormulae, mandatoryResultHolders);

            return MatchEngine.evaluate(model, ModelVariables.create(variables));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return MatchResult.NONE;
    }

    /**
     * 获取所在配率
     *      返回匹配率区间以及匹配结果
     * 例子：
     * 需查看，所对应政策在区间 0.2-0.7 哪个区间内（可分为：0.0-0.2  0.2-。07  0.7-1.0 三个区间）
     *
     * <p>
     *     公式：
     *      "var0 > 10"
     *      "var1 > 20"
     *      "var2 > 30"
     *      "var3 > 40"
     * </p>
     * <p>
     *     参数：
     *     "var0", "5"
     *      "var1", "5"
     *      "var2", "5"
     *      "var3", "5"
     * </p>
     * <p>
     *     结果：
     *      WeightedRange ； {
     *          "lowerBound": "0.0",    最低限度
     *          "upperBound": "0.2",    最高限度
     *          "matchResult": "0.0"    计算结果
     *      }
     * </p>
     *
     * @param formulae      公式
     * @param lowerBound    最低匹配度（如：0.2）
     * @param upperBound    做高匹配度（如：0.7）
     * @param variables     参数
     * @return com.enzenith.engine.MatchResult
     * @author LinShuPeng
     * @date 2021-04-07  15:55
     **/
    public static WeightedRange calculationWeightedRange(
            List<String> formulae, String lowerBound, String upperBound,
            HashMap<String, String> variables){
        try {
            WeightedRange weightedRange = WeightedRange.create(lowerBound, upperBound);
            List<CalculationModel> conditions = new ArrayList<>();
            formulae.forEach(expression -> conditions.add(CalculationModel.create(expression)));
            WeightedMatchModel weightedMatchModel = WeightedMatchModel.create(conditions);

            return MatchEngine.evaluate(weightedMatchModel, ModelVariables.create(variables), weightedRange);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
