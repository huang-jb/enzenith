package com.enzenith.engine;

/**
 * @author LinShuPeng
 * @date 2021-02-03 15:00
 */
public class WeightedRange {

    /**
     * 最低限度
     */
    private String lowerBound;

    /**
     * 最高限度
     */
    private String upperBound;

    /**
     * 匹配结果
     */
    private String matchResult;


    public WeightedRange(String lowerBound, String upperBound, String matchResult) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.matchResult = matchResult;
    }

    public static WeightedRange create(String lowerBound, String upperBound, String matchResult){
        return new WeightedRange(lowerBound, upperBound, matchResult);
    }


    public static WeightedRange create(String lowerBound, String upperBound){
        return new WeightedRange(lowerBound, upperBound, null);
    }

    public String getLowerBound() {
        return lowerBound;
    }

    public String getUpperBound() {
        return upperBound;
    }

    public String getMatchResult() {
        return matchResult;
    }
}
