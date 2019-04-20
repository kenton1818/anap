package com.example.lui_project.utils;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;


public class GetBMIValuesHelper {
    public static final int VALUES_NULL = 0;
    public static final int EXTENUATION = 1;
    public static final int NORMAL_WEIGHT = 2;
    public static final int WEIGHT_OVER_WEIGHT = 3;
    public static final int SEVERLY_OBESE_ONE = 4;
    public static final int SEVERLY_OBESE_TWO = 5;
    public static final int SEVERLY_OBESE_THREE = 6;
    /**
     * Get the BMI value to judge the body fatness of this person
     *
     *  @param height
     *  *  * @param weight
     *  *  * @return  hight
     */
    // BMI（Body Mass Index）

    public double getBMI_Value(int height, int weight) {
        //  BMI = kg/m2
        double height2 = height * 0.01;
        double value = weight / (Math.pow(height2, 2));
        DecimalFormat format = new DecimalFormat("#.0");

        String str = format.format(value);
        double bmi = Double.parseDouble(str);
        return bmi;
    }



    /**
     * Get the range of normal weight
     * @param height
     * @return
     */
    //The standard range of body refers to 18.5~24
    public Map<String,Double> getNormalWeightRange(int height){
        Map<String,Double> map = new HashMap<>();
        //Get the maximum weight in the normal range
        Double maxNormalWeight = Math.sqrt(24 * height);
        DecimalFormat maxFormat = new DecimalFormat("#.0");
        String maxStr = maxFormat.format(maxNormalWeight);
        Double maxNormalWeightValues = Double.parseDouble(maxStr);
        //Get the minimum weight in the normal range
        Double minNormalWeight = Math.sqrt(18.5 * height);
        DecimalFormat minFormat = new DecimalFormat("#.0");
        String minStr = minFormat.format(minNormalWeight);
        Double minNormalWeightValues = Double.parseDouble(minStr);
        //Add the required parameters
        map.put("max",maxNormalWeightValues);
        map.put("min",minNormalWeightValues);
        return map;
    }
}
