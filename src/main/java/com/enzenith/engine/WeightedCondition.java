package com.enzenith.engine;

import java.math.BigDecimal;

/**
 * 权重匹配条件
 */
public class WeightedCondition {
    /**
     * 条件模型
     */
    private CalculationModel condition;

    /**
     * 占重比（满足比例）
     */
    private BigDecimal weight;

    private WeightedCondition(CalculationModel condition, BigDecimal weight) {
        this.condition = condition;
        this.weight = weight;
    }

    public static WeightedCondition create(CalculationModel condition, BigDecimal weight) {
        return new WeightedCondition(condition, weight);
    }

    public static WeightedCondition create(CalculationModel condition) {
        return new WeightedCondition(condition, BigDecimal.ONE);
    }

    public CalculationModel getCondition() {
        return condition;
    }

    public BigDecimal getWeight() {
        return weight;
    }
}
