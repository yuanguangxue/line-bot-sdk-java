package com.hp.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ExcelData implements Serializable {

    private static final long serialVersionUID = 4444017239100620999L;

    // 表头
    private List<Head> heads;

    // 数据
    private List<List<Object>> rows;

    // 页签名称
    private String name;

    @Data
    @Builder
    public static class Head {

        private String title;

        private int length;

    }
}
