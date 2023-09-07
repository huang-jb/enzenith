package com.enzenith.engine;

import java.util.ArrayList;
import java.util.List;

public class CalculationModel {
    private List<CalculationStep> calculationSteps = new ArrayList<>();

    /**
     * 计算模型
     *
     * @param expressions   计算公式
     * @param resultHolders 结果持有者（临时数值）
     *                      如：if(var0 > 100, 100, 0)
     *                      会有将 值放入  resultHolder 中，按顺序放入。
     * @author LinShuPeng
     * @date 2021-02-02  11:06
     **/
    private CalculationModel(List<String> expressions, List<String> resultHolders) throws Exception {

        if (expressions.size() != resultHolders.size() + 1) {
            //结果持有者（临时值）数量，不能大于所传入公式数量。
            throw new Exception("expressions size [" + expressions.size() + "] should be one more than resultHolders size [" + resultHolders.size() + "]");
        }

        for (int i = 0; i < expressions.size(); i++) {
            //如果存在临时值持有者
            if (i < resultHolders.size()) {
                //将计算公式与所对用的临时值持有者绑定到一起。
                calculationSteps.add(CalculationStep.create(expressions.get(i), resultHolders.get(i)));
            } else {
                calculationSteps.add(CalculationStep.create(expressions.get(i)));
            }
        }
    }

    private CalculationModel(String expression) {
        calculationSteps.add(CalculationStep.create(expression));
    }

    /**
     * 创建计算模型（用于公式大于 1 个的情况）
     *
     * @param expressions   计算公式
     * @param resultHolders 结果持有者（临时数值）
     * @return com.enzenith.engine.CalculationModel
     * @author LinShuPeng
     * @date 2021-02-02  11:12
     **/
    public static CalculationModel create(List<String> expressions, List<String> resultHolders) throws Exception {
        return new CalculationModel(expressions, resultHolders);
    }

    /**
     * 创建计算模型（计算公式只存在1个）
     *
     * @param expression 计算公式
     * @return com.enzenith.engine.CalculationModel
     * @author LinShuPeng
     * @date 2021-02-02  11:32
     **/
    public static CalculationModel create(String expression) {
        return new CalculationModel(expression);
    }

    public List<CalculationStep> getCalculationSteps() {
        return calculationSteps;
    }
}
