package com.enzenith.engine;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 业务公式计算
 *
 * @author LinShuPeng
 * @date 2021-02-03 9:39
 */
public class BusinessCalculationEngineTest {

    /**
     * “企业技术中心”区级奖励
     *     if (企业与高等院校、科研院所共建国家级研发中心   = True , 500,000   , 0 )
     * +      if (企业与高等院校、科研院所共建省级研发中心    = True , 300,000   , 0 )
     * +     if (企业与高等院校、科研院所共建市级研发中心    = True , 150,000   , 0 )
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
     */
    @Test
    void test1() throws Exception {
        List<String> formulae = new ArrayList<>();
        formulae.add("if(var0 && var1, 500000, 0)");
        formulae.add("if(var0 && var2, 300000, 0)");
        formulae.add("if(var0 && var3, 150000, 0)");
        formulae.add("(temp0+temp1+temp2)");

        List<String> resultHolders = new ArrayList<>();
        resultHolders.add("temp0");
        resultHolders.add("temp1");
        resultHolders.add("temp2");

        HashMap<String, String> variables = new HashMap<>();
        variables.put("var0", "True");
        variables.put("var1", "True");
        variables.put("var2", "True");
        variables.put("var3", "True");

        CalculationModel calculationModel = CalculationModel.create(formulae, resultHolders);
        BigDecimal result = CalculationEngine.evaluate(calculationModel, ModelVariables.create(variables));
        System.out.println(result.toPlainString());
        assertTrue(result.toPlainString().equals("950000"));


        variables.put("var0", "1");
        variables.put("var1", "0");
        variables.put("var2", "1");
        variables.put("var3", "1");
        calculationModel = CalculationModel.create(formulae, resultHolders);
        result = CalculationEngine.evaluate(calculationModel, ModelVariables.create(variables));
        assertTrue(result.toPlainString().equals("450000"));

        variables.put("var0", "0");
        variables.put("var1", "1");
        variables.put("var2", "1");
        variables.put("var3", "1");
        calculationModel = CalculationModel.create(formulae, resultHolders);
        result = CalculationEngine.evaluate(calculationModel, ModelVariables.create(variables));
        assertTrue(result.toPlainString().equals("0"));
    }

    /**
     * 非法人市场主体成立满3年转制为有限责任公司的企业财政一次性奖励
     * 必要条件：
     * "1.由非法人市场主体（个体工商户、个人独资企业和合伙企业）成立满3年转制为有限责任公司的企业。
     * 2.对2018年12月29日前登记注册的由非法人市场主体在2018年12月30日至2020年12月31日期间办理转型为企业。
     * 3.企业法人被列为失信被执行人或涉黑涉恶当事人的，不得给予政策和资金支持；
     * 4.被列入市场监管部门严重违法失信名单或黑名单，不得给予政策和资金支持；
     * 5.企业的实际住所已不在泉州台商投资区区域内，不得给予政策和资金支持 。"
     * <p>
     * 公式：
     * if(var0 && var1 && var2 && var3 && var4, 1, 0)
     * <p>
     * <p>
     * 公式中值含义：
     * var0：由非法人市场主体（个体工商户、个人独资企业和合伙企业）成立满3年转制为有限责任公司的企业；
     * var1：对2018年12月29日前登记注册的由非法人市场主体在2018年12月30日至2020年12月31日期间办理转型为企业；
     * var2：企业法人被列为失信被执行人或涉黑涉恶当事人；
     * var3：被列入市场监管部门严重违法失信名单或黑名单；
     * var4：企业的实际住所已不在泉州台商投资区区域内；
     * </p>
     */
    @Test
    void test2() throws Exception {
        String expression = "if(var0 && var1 && var2 && var3 && var4, 1, 0)";

        HashMap<String, String> variables = new HashMap<>();
        variables.put("var0", "1");
        variables.put("var1", "1");
        variables.put("var2", "1");
        variables.put("var3", "1");
        variables.put("var4", "1");
        CalculationModel calculationModel = CalculationModel.create(expression);
        BigDecimal result = CalculationEngine.evaluate(calculationModel, ModelVariables.create(variables));
        assertTrue(result.toPlainString().equals("1"));


        variables.put("var0", "1");
        variables.put("var1", "1");
        variables.put("var2", "0");
        variables.put("var3", "1");
        variables.put("var4", "1");
        calculationModel = CalculationModel.create(expression);
        result = CalculationEngine.evaluate(calculationModel, ModelVariables.create(variables));
        assertTrue(result.toPlainString().equals("0"));
    }

    /**
     * 跨境电商发展奖励（海外仓类）
     * 政策条件：
     * 支持海外仓建设：鼓励有条件的跨境电子商务企业布局海外仓、边境仓，建设物流基地。对新建、租赁200平方米以上海外仓，按实际仓储面积每月20元/平方米给予补助，每家企业每年最高补助30万元
     * <p>
     * 公式：
     * if(var0 && var1 > 200, if(var1 * 20 * 12 > 300000, 300000, var1 * 20 * 12), 0)
     * <p>
     * <p>
     * 公式中值含义：
     * var0：跨境电子商务企业
     * var1：新建、租赁海外仓平方米 > 200
     * </p>
     */
    @Test
    void test3() throws Exception {
        String expression = "if(var0 && var1 > 200, if(var1 * 20 * 12 > 300000, 300000, var1 * 20 * 12), 0)";

        HashMap<String, String> variables = new HashMap<>();
        variables.put("var0", "1");
        variables.put("var1", "300");

        CalculationModel calculationModel = CalculationModel.create(expression);
        BigDecimal result = CalculationEngine.evaluate(calculationModel, ModelVariables.create(variables));
        assertTrue(result.toPlainString().equals("72000"));


        variables.put("var0", "1");
        variables.put("var1", "1");
        calculationModel = CalculationModel.create(expression);
        result = CalculationEngine.evaluate(calculationModel, ModelVariables.create(variables));
        assertTrue(result.toPlainString().equals("0"));
    }

    /**
     * 跨境电商发展奖励（跨境电商园区类）
     * 政策条件：
     * 建设跨境电子商务园区：对面积超过5000平方米、入驻企业20家以上的跨境电商园区，按年度新增投资额（公共服务区域建设改造、软件购置、设备投入）给予平台运营主体20%补助，最高不超过100万元。
     * <p>
     * 公式：
     * if(var0 && var1 >= 5000 && var2 >= 20, 1, 0)
     * if(temp0, var3 * 0.2, 0)
     * if(temp1 > 1000000, 1000000, temp0)
     * <p>
     * <p>
     * 公式中值含义：
     * var0：建设跨境电子商务园区；
     * var1：园区面积平方米 >= 5000
     * var2：入驻企业/家 >= 20
     * var3：新增投资额（公共服务区域建设改造、软件购置、设备投入）
     * temp0：表达式 1 的值
     * temp1：表达式 2 的值
     * </p>
     */
    @Test
    void test4() throws Exception {
        List<String> formulae = new ArrayList<>();
        formulae.add("if(var0 && var1 >= 5000 && var2 >= 20, 1, 0)");
        formulae.add("if(temp0, var3 * 0.2, 0)");
        formulae.add("if(temp1 > 1000000, 1000000, temp1)");

        List<String> resultHolders = new ArrayList<>();
        resultHolders.add("temp0");
        resultHolders.add("temp1");

        HashMap<String, String> variables = new HashMap<>();
        variables.put("var0", "1");
        variables.put("var1", "5500");
        variables.put("var2", "21");
        variables.put("var3", "5100000");

        CalculationModel calculationModel = CalculationModel.create(formulae, resultHolders);
        BigDecimal result = CalculationEngine.evaluate(calculationModel, ModelVariables.create(variables));
        assertTrue(result.toPlainString().equals("1000000"));

        variables = new HashMap<>();
        variables.put("var0", "1");
        variables.put("var1", "5500");
        variables.put("var2", "21");
        variables.put("var3", "10000");

        calculationModel = CalculationModel.create(formulae, resultHolders);
        result = CalculationEngine.evaluate(calculationModel, ModelVariables.create(variables));
        assertTrue(result.toPlainString().equals("2000"));
    }

    /**
     * 旅游专业人才培养奖励(旅游行业技能竞赛类）
     *
     * <p>
     * 政策条件：
     * 对在我区从事旅游行业，并在全国、省、泉州市旅游部门组织的技能竞赛中获奖的，给予一次性奖励，具体标准为：
     * 1.获得国家级一、二、三等奖的分别奖励1万元、0.8万元、0.5万元。
     * 2.获省级一、二、三等奖的分别奖励0.5万元、0.4万元、0.3万元
     * 3.获得泉州市级一、二、三等奖的分别奖励0.3万元、0.2万元、0.1万元。
     * </p>
     * <p>
     * 公式：
     * if(var0 || var1 || var2, 1, 0)
     * if(temp0, CASE(var3,"一等奖,二等奖,三等奖", "10000,8000,5000,0") + CASE(var4,"一等奖,二等奖,三等奖", "5000,4000,3000,0") + CASE(var5,"一等奖,二等奖,三等奖", "3000,2000,1000,0"), 0)
     * </p>
     * <p>
     * 公式中值含义：
     * var0：全国旅游部门组织的技能竞赛中获奖
     * var1：福建省旅游部门组织的技能竞赛中获奖
     * var2：泉州市游部门组织的技能竞赛中获奖
     * var3：全国旅游部门组织的技能竞赛中 获奖等级
     * var4：福建省旅游部门组织的技能竞赛中 获奖等级
     * var5：泉州市游部门组织的技能竞赛中 获奖等级
     * temp0：(全国旅游部门组织的技能竞赛中获奖  = True) or
     * (福建省旅游部门组织的技能竞赛中获奖  = True)  or
     * (泉州市游部门组织的技能竞赛中获奖 = True)
     * </p>
     */
    @Test
    void test5() throws Exception {
        List<String> formulae = new ArrayList<>();
        formulae.add("if(var0 || var1 || var2, 1, 0)");
        formulae.add("if(temp0, CASE(var3,\"一等奖,二等奖,三等奖\", \"10000,8000,5000,0\") + CASE(var4,\"一等奖,二等奖,三等奖\", \"5000,4000,3000,0\") + CASE(var5,\"一等奖,二等奖,三等奖\", \"3000,2000,1000,0\"), 0)");
        List<String> resultHolders = new ArrayList<>();
        resultHolders.add("temp0");

        HashMap<String, String> variables = new HashMap<>();
        variables.put("var0", "1");
        variables.put("var1", "1");
        variables.put("var2", "1");
        variables.put("var3", "一等奖");
        variables.put("var4", "一等奖");
        variables.put("var5", "一等奖");

        CalculationModel calculationModel = CalculationModel.create(formulae, resultHolders);
        BigDecimal result = CalculationEngine.evaluate(calculationModel, ModelVariables.create(variables));
        assertTrue(result.toPlainString().equals("18000"));

        variables.put("var0", "1");
        variables.put("var1", "0");
        variables.put("var2", "1");
        variables.put("var3", "一等奖");
        variables.put("var4", "无");
        variables.put("var5", "一等奖");

        calculationModel = CalculationModel.create(formulae, resultHolders);
        result = CalculationEngine.evaluate(calculationModel, ModelVariables.create(variables));
        assertTrue(result.toPlainString().equals("13000"));

        variables.put("var0", "0");
        variables.put("var1", "0");
        variables.put("var2", "0");
        variables.put("var3", "无");
        variables.put("var4", "无");
        variables.put("var5", "无");

        calculationModel = CalculationModel.create(formulae, resultHolders);
        result = CalculationEngine.evaluate(calculationModel, ModelVariables.create(variables));
        assertTrue(result.toPlainString().equals("0"));
    }

    /**
     * 旅游专业人才培养奖励(一线导游工作类）
     *
     * <p>
     * 政策条件：
     * 对考取中级、高级导游证且在我区从事一线导游工作的给予全额补助培训考试费。
     * </p>
     * <p>
     * 公式：
     * if((var0 || var1) && var2)
     * </p>
     * <p>
     * 公式中值含义：
     * var0：考取中级导游证
     * var1：考取高级导游证
     * var2：在我区从事一线导游工作
     * </p>
     */
    @Test
    void test6() throws Exception {

        String expression = "if((var0 || var1) && var2, 1, 0)";

        HashMap<String, String> variables = new HashMap<>();
        variables.put("var0", "0");
        variables.put("var1", "1");
        variables.put("var2", "1");

        CalculationModel calculationModel = CalculationModel.create(expression);
        BigDecimal result = CalculationEngine.evaluate(calculationModel, ModelVariables.create(variables));
        assertTrue(result.toPlainString().equals("1"));


        variables.put("var0", "1");
        variables.put("var1", "0");
        variables.put("var2", "1");

        calculationModel = CalculationModel.create(expression);
        result = CalculationEngine.evaluate(calculationModel, ModelVariables.create(variables));
        assertTrue(result.toPlainString().equals("1"));

        variables.put("var0", "1");
        variables.put("var1", "0");
        variables.put("var2", "0");

        calculationModel = CalculationModel.create(expression);
        result = CalculationEngine.evaluate(calculationModel, ModelVariables.create(variables));
        assertTrue(result.toPlainString().equals("0"));
    }


    /**
     * 蔬菜产业补助
     * <p>
     *     政策条件：
     *     1.对集中连片300亩以上的蔬菜种植基地，每年补助5万元。
     *     2.对集中连片300亩-200亩（含）以上的蔬菜种植基地，每年补助3万元。
     *     3.对集中连片200亩-100亩（含）以上的蔬菜种植基地，每年补助1万元。
     *     4.应该于种植期申请补助。
     * </p>
     * <p>
     *     公式：
     *     if(var0, 1, 0)
     *     if(temp0 && var1 >= 300, 50000, if(200 <= var1 && var1 < 300, 30000, if(100 <= var1 && var1 < 200, 10000, 0)))
     * </p>
     * <p>
     *     公式中值含义：
     *     var0：是否在种植期内
     *     var1：种植亩数
     * </p>
     *
     */
    @Test
    void test7() throws Exception {
        List<String> formulae = new ArrayList<>();
        formulae.add("if(var0, 1, 0)");
        formulae.add("if(temp0 && var1 >= 300, 50000, if(200 <= var1 && var1 < 300, 30000, if(100 <= var1 && var1 < 200, 10000, 0)))");

        HashMap<String, String> variables = new HashMap<>();
        variables.put("var0", "1");
        variables.put("var1", "300");

        List<String> resultHolders = new ArrayList<>();
        resultHolders.add("temp0");

        CalculationModel calculationModel = CalculationModel.create(formulae, resultHolders);
        BigDecimal result = CalculationEngine.evaluate(calculationModel, ModelVariables.create(variables));
        assertTrue(result.toPlainString().equals("50000"));


        variables.put("var0", "1");
        variables.put("var1", "200");

        calculationModel = CalculationModel.create(formulae, resultHolders);
        result = CalculationEngine.evaluate(calculationModel, ModelVariables.create(variables));
        assertTrue(result.toPlainString().equals("30000"));

        variables.put("var0", "1");
        variables.put("var1", "100");

        calculationModel = CalculationModel.create(formulae, resultHolders);
        result = CalculationEngine.evaluate(calculationModel, ModelVariables.create(variables));
        assertTrue(result.toPlainString().equals("10000"));
    }

    /**
     * 设施农业补助
     * <p>
     *     政策条件：
     *     ①补助范围：当年度建成、经专家组评审列入省市资金补助范围、达到建设标准、通过验收公示无异议，符合补贴条件的设施农业项目。
     *     ②补助类型：区级设施农业补助类型为智能温室（造价40万元／亩）、智能温控大棚（造价２0万元／亩）等２种棚型，建造标准按照《福建省财政厅 福建省农业厅关于印发设施农业温室大棚省级补贴专项资金管理办法的通知》（闽财农〔2016〕8号）执行。
     *     ③起补面积：智能温室、智能温控大棚（一）起补面积10亩。
     *     ④补助标准：智能温室每亩补助2万元，智能温控大棚（一）每亩补助1万元
     * </p>
     */
}
