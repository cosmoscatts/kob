package com.kob.backend.common;

import java.util.Objects;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PageQuery {

    /** 当前页码 */
    private Integer page;

    /** 每页数量 */
    private Integer pageSize;

    public Integer getPage() {
        return Objects.isNull(page) ? 1 : page;
    }

    public Integer getPageSize() {
        return Objects.isNull(pageSize) ? -1 : pageSize;
    }
}
