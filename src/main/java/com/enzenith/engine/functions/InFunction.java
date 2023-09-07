package com.enzenith.engine.functions;

import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.functions.AbstractFunction;
import com.ezylang.evalex.functions.FunctionParameter;
import com.ezylang.evalex.parser.Token;

@FunctionParameter(name = "input")
@FunctionParameter(name = "values")
public class InFunction extends AbstractFunction {
    @Override
    public EvaluationValue evaluate(
            Expression expression, Token functionToken, EvaluationValue... parameterValues) {
        EvaluationValue input = parameterValues[0];
        EvaluationValue values = parameterValues[1];
        return new EvaluationValue(eval(input.getStringValue(), values.getStringValue()));
    }

    public static Boolean eval(String input, String values) {
        String[] valueList = values.split(",|，");

        for (String val : valueList) {
            if (input.equals(val)) {
                return true;
            }
        }
        return false;
    }
}
