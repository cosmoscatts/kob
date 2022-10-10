package com.kob.backend.common;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PageMap<T> {
    private Long total;
    private List<T> records;

    public static <T> PageMap<T> empty() {
        return new PageMap<T>().setTotal(0L).setRecords(new ArrayList<>());
    }

    public static <T> PageMap<T> data(Long total, List<T> data) {
        return new PageMap<T>().setTotal(total).setRecords(data);
    }
}
