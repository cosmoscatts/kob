package com.kob.controller.record;

import com.kob.common.DeleteQuery;
import com.kob.common.PageMap;
import com.kob.common.PageQuery;
import com.kob.common.Result;
import com.kob.model.vo.response.RecordRespVO;
import com.kob.model.vo.request.RecordSearchVO;
import com.kob.model.converter.RecordConverter;
import com.kob.model.entity.Record;
import com.kob.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping("/api/record")
@RequiredArgsConstructor
public class RecordController {
    private final RecordService recordService;

    @GetMapping("/list")
    public Result<PageMap<RecordRespVO>> getList(PageQuery pageQuery, RecordSearchVO searchVO) {
        return Result.success(recordService.getList(pageQuery, searchVO));
    }

    @DeleteMapping("/delete")
    public Result<?> deleteRecord(@Valid DeleteQuery query) {
        String errorMessage = recordService.delete(query);
        if (!Objects.isNull(errorMessage))
            return Result.error(errorMessage);
        return Result.success("对局删除成功");
    }

    @GetMapping("/getById")
    public Result<RecordRespVO> getById(@RequestParam Integer id) {
        Record record = recordService.getById(id);
        if (!Objects.isNull(record)) {
            return  Result.success(RecordConverter.INSTANCE.do2vo(record));
        }
        return Result.success(null);
    }
}
