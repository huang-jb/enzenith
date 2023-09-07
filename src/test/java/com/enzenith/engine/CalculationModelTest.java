package com.enzenith.engine;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CalculationModelTest {

    @Test
    void create() throws Exception {


        List<String> formulae = new ArrayList<>();
        //下面公式中 var0 代表（1-true，0-false） 只能用0 1 表示
        formulae.add("if(var0 && var1 > 200, var1 * 20, 0)");
        formulae.add("if(temp0 * 12 > 300000, 300000, temp0 * 12)");
        formulae.add("(temp0 + temp1) * 3");

        List<String> resultHolders = new ArrayList<>();
        resultHolders.add("temp0");

        try {
            CalculationModel.create(formulae, resultHolders);
        }
        catch (Exception e)
        {
            var message = e.getMessage();
            System.out.println(message);
            assertTrue(message.equals("expressions size [3] should be one more than resultHolders size [1]"));
        }


        HashMap<String, String> variables = new HashMap<>();
        variables.put("var0", "111");
        variables.put("var1", "222.222");
        variables.put("var2", "ccc");

        resultHolders.add("temp1");

        var model = CalculationModel.create(formulae, resultHolders);
        var modelVariables = ModelVariables.create(variables);
        assertTrue(modelVariables.getNumberVariables().size() == 2);
        assertTrue(modelVariables.getStringVariables().size() == 1);

        assertTrue(modelVariables.getNumberVariables().get("var0").equals("111"));
        assertTrue(modelVariables.getNumberVariables().get("var1").equals("222.222"));
        assertTrue(modelVariables.getStringVariables().get("var2").equals("ccc"));
   }
}