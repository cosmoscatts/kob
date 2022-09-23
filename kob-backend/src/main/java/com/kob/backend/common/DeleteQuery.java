package com.kob.backend.common;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class DeleteQuery {
    @NotNull(message = "id 不可为空")
    private Integer id;
}
