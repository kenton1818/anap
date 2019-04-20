package com.example.lui_project;

import org.junit.Test;
import com.example.lui_project.utils.GetBMIValuesHelper;
import static org.junit.Assert.*;

public class BMIValue_Test {
    @Test
    public void addition_isCorrect() {
        GetBMIValuesHelper BMI = new GetBMIValuesHelper();
        assertEquals(43.2, BMI.getBMI_Value(180,140),1);
    }
}
