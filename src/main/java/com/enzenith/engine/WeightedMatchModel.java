package com.enzenith.engine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 权重匹配模型
 */
public class WeightedMatchModel {
    /**
     * 条件
     */
    private List<WeightedCondition> conditions = new ArrayList<>();

    private WeightedMatchModel() { }

    /**
     *
     * @param models        计算模型
     * @return com.enzenith.engine.WeightedMatchModel
     * @author LinShuPeng
     * @date 2021-02-04  9:47
     **/
    public static WeightedMatchModel create(List<CalculationModel> models) {
        WeightedMatchModel weightedMatchModel = new WeightedMatchModel();

        for (int i = 0; i < models.size(); i++) {
            //创建权重模型，每个条件占重比默认为1
            weightedMatchModel.conditions.add(WeightedCondition.create(models.get(i)));
        }

        return weightedMatchModel;
    }

    /**
     * 创建权重模型
     * @param models        计算模型
     * @param weights       占重比例，如（计算模型中有3个条件，其中条件1，占重比为2，则传入的第一个weights为2）
     * @return com.enzenith.engine.WeightedMatchModel
     * @author LinShuPeng
     * @date 2021-02-04  9:44
     **/
    public static WeightedMatchModel create(List<CalculationModel> models, List<BigDecimal> weights) {
        WeightedMatchModel weightedMatchModel = new WeightedMatchModel();

        for (int i = 0; i < models.size(); i++) {
            //创建权重模型，将条件进行占重比例分配
            weightedMatchModel.conditions.add(WeightedCondition.create(models.get(i), weights.get(i)));
        }

        return weightedMatchModel;
    }

    public List<WeightedCondition> getConditions() {
        return conditions;
    }
}
