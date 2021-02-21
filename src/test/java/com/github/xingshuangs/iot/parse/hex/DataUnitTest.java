package com.github.xingshuangs.iot.parse.hex;

import org.junit.Test;


import static org.junit.Assert.*;

public class DataUnitTest {

    @Test
    public void toBoolean() {
        DataUnit.DataTypeEm em = DataUnit.DataTypeEm.valueFrom("double");
        assertEquals(DataUnit.DataTypeEm.FLOAT64, em);
    }
}
