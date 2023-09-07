package com.enzenith.engine.functions;

import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.functions.AbstractFunction;
import com.ezylang.evalex.functions.FunctionParameter;
import com.ezylang.evalex.parser.Token;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@FunctionParameter(name = "date")
public class YearFunction extends AbstractFunction {
    @Override
    public EvaluationValue evaluate(
            Expression expression, Token functionToken, EvaluationValue... parameterValues) {

        EvaluationValue date = parameterValues[0];
        return new EvaluationValue(eval(date.getStringValue()));
    }
    public static BigDecimal eval(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        try {
            date = date.replaceAll("-|/", "");
            Date d = format.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(d);
            return new BigDecimal(calendar.get(Calendar.YEAR));
        } catch (ParseException e) {
            return null;
        }
    }
}
