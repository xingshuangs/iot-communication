package com.github.xingshuangs.iot.utils;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;


public class TimesUtilTest {

    @Test
    public void getNtpTime() {
        LocalDateTime ntpTime = TimesUtil.getNTPDateTime(3891134116L);
        System.out.println(ntpTime);
    }

    @Test
    public void getUTCTotalSecond() {
        LocalDateTime dateTime = LocalDateTime.of(1970, 1, 1, 0, 0, 3);
        long second = TimesUtil.getUTCTotalSecond(dateTime);
        assertEquals(3, second);
    }
}