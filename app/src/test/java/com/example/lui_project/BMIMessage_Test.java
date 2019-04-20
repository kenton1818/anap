package com.example.lui_project;

import com.example.lui_project.utils.GetBMIValuesHelper;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class BMIMessage_Test {
    @Test
    public void addition_isCorrect() {
        GetBMIValuesHelper BMI = new GetBMIValuesHelper();
        Map<String,Double> resultmap = new HashMap<>();
        Map<String,Double> map = new HashMap<>();
        resultmap = BMI.getNormalWeightRange(180);
        map.put("max",65.7);
        map.put("min",57.7);
        assertEquals(map,resultmap);
    }
}
