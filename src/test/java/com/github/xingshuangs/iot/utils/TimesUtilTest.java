package com.github.xingshuangs.iot.utils;

import org.junit.Test;

import java.time.LocalDateTime;


public class TimesUtilTest {

    @Test
    public void getNtpTime() {
        LocalDateTime ntpTime = TimesUtil.getNTPDateTime(3891134116L);
        System.out.println(ntpTime);
    }
}