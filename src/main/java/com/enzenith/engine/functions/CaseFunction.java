package com.enzenith.engine.functions;

import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.functions.AbstractFunction;
import com.ezylang.evalex.functions.FunctionParameter;
import com.ezylang.evalex.parser.Token;

import java.math.BigDecimal;

@FunctionParameter(name = "input")
@FunctionParameter(name = "cases")
@FunctionParameter(name = "values")
public class CaseFunction extends AbstractFunction {
    @Override
    public EvaluationValue evaluate(
            Expression expression, Token functionToken, EvaluationValue... parameterValues) {

        EvaluationValue input = parameterValues[0];
        EvaluationValue cases = parameterValues[1];
        EvaluationValue values = parameterValues[2];
        return new EvaluationValue(eval(input.getStringValue(), cases.getStringValue(), values.getStringValue()));
    }

    public static BigDecimal eval(String input, String cases, String values) {
        var caseList = cases.split(",|，");
        var valueList = values.split(",|，");

        for (int i = 0; i < caseList.length; i++) {
            if (input.equals(caseList[i])) {
                return new BigDecimal(valueList[i]);
            }
        }
        return new BigDecimal(valueList[valueList.length-1]);
    }

}
