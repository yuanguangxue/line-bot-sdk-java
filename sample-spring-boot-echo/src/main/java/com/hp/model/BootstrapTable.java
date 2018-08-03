package com.hp.model;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class BootstrapTable<T> {

    private int total;

    private List<T> rows;

    public static <T> BootstrapTable<T> to(Page<T> page){
        BootstrapTable bootstrapTable = new BootstrapTable();
        bootstrapTable.setRows(page.getContent());
        bootstrapTable.setTotal((int) page.getTotalElements());
        return bootstrapTable;
    }
}
