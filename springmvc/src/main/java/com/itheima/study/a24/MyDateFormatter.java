package com.itheima.study.a24;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Slf4j
public class MyDateFormatter implements Formatter<Date> {
    private final String desc;

    public MyDateFormatter(String desc){
        this.desc=desc;
    }

    @Override
    public Date parse(String text, Locale locale) throws ParseException {
        log.info("======进入了:{}",desc);
        SimpleDateFormat format = new SimpleDateFormat("yyyy|MM|dd");
        return format.parse(text);
    }

    @Override
    public String print(Date date, Locale locale) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy|MM|dd");
        return format.format(date);
    }
}
