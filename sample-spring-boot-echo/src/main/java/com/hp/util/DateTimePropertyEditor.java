package com.hp.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;


import java.beans.PropertyEditorSupport;
import java.text.SimpleDateFormat;
import java.util.Date;


@Slf4j
public class DateTimePropertyEditor extends PropertyEditorSupport {

    private String formatter;

    private SimpleDateFormat format;

    public DateTimePropertyEditor(){
        this("yyyy-MM-dd HH:mm");
    }

    private DateTimePropertyEditor(String formatter) {
        if (null != formatter) {
            this.formatter = formatter;
        }
        format = new SimpleDateFormat(formatter);
    }

    @Override
    public String getAsText() {

        log.debug("===>getAsText:" + getValue());

        Object obj = getValue();
        if (null != obj) {
            return format.format((Date) obj);
        }

        return "";
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {

        log.debug("datetime setAsText:" + text);

        if (StringUtils.isEmpty(text)) {
            setValue(null);
        } else {

            try {
                setValue(format.parse(text));
            } catch (Exception e) {
                log.debug("format datetime error:" + e.getMessage() + ",value:"
                        + text);
            }
        }

    }

    public String getFormatter() {
        return formatter;
    }

    public void setFormatter(String formatter) {
        this.formatter = formatter;
    }
}
