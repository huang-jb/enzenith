package com.enzenith.engine;

/**
 * 计算步骤
 *  将 表达式 与 临时结果值 进行 绑定。
 */
public class CalculationStep {
    /**
     * 公式
     */
    private String expression;

    /**
     * 结果值
     */
    private String resultHolder;

    private CalculationStep(String expression, String resultHolder) {
        this.expression = expression;
        this.resultHolder = resultHolder;
    }

    public static CalculationStep create(String expression, String resultHolder) {
        return new CalculationStep(expression, resultHolder);
    }

    public static CalculationStep create(String expression) {
        return new CalculationStep(expression, null);
    }

    public String getExpression() {
        return expression;
    }

    public String getResultHolder() {
        return resultHolder;
    }
}
