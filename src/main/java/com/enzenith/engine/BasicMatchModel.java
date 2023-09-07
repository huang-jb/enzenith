package com.enzenith.engine;

import java.util.List;

/**
 * 基础权重匹配
 */
public class BasicMatchModel {
    /**
     * 强制性条件
     */
    private CalculationModel mandatoryCondition;

    /**
     * 附加条件
     */
    private CalculationModel additionalCondition;

    private BasicMatchModel(CalculationModel mandatoryCondition, CalculationModel additionalCondition) {
        this.mandatoryCondition = mandatoryCondition;
        this.additionalCondition = additionalCondition;
    }

    public static BasicMatchModel create(CalculationModel mandatoryCondition, CalculationModel additionalCondition){
        return new BasicMatchModel(mandatoryCondition, additionalCondition);
    }

    /**
     * 创建基础匹配模型
     * @param mandatoryFormulae     强制条件（必要条件）公式
     * @param mandatoryResultHolders    强制条件（必要条件）结果持有者（临时存放对象）
     * @param additionalFormulae    额外条件（非必要条件）公式
     * @param additionalResultHolders   额外条件（非必要条件）结果持有者（临时存放对象）
     * @return com.enzenith.engine.BasicMatchModel
     * @author LinShuPeng
     * @date 2021-02-02  16:44
     **/
    public static BasicMatchModel create(List<String> mandatoryFormulae, List<String> mandatoryResultHolders,
                                         List<String> additionalFormulae, List<String> additionalResultHolders) throws Exception {
        return create(CalculationModel.create(mandatoryFormulae, mandatoryResultHolders),
                CalculationModel.create(additionalFormulae, additionalResultHolders));
    }

    /**
     * 创建基础匹配模型
     * @param mandatoryFormulae     强制条件（必要条件）公式
     * @param mandatoryResultHolders    强制条件（必要条件）结果持有者（临时存放对象）
     * @return com.enzenith.engine.BasicMatchModel
     * @author LinShuPeng
     * @date 2021-02-02  16:44
     **/
    public static BasicMatchModel create(List<String> mandatoryFormulae, List<String> mandatoryResultHolders) throws Exception {
        return create(CalculationModel.create(mandatoryFormulae, mandatoryResultHolders), null);
    }

    /**
     * 获取强制性条件
     */
    public CalculationModel getMandatoryCondition() {
        return mandatoryCondition;
    }

    /**
     * 获取附加条件
     */
    public CalculationModel getAdditionalCondition() {
        return additionalCondition;
    }

}
