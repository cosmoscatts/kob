package com.kob.controller.remark;

import com.kob.common.PageMap;
import com.kob.common.PageQuery;
import com.kob.common.RateLimit;
import com.kob.common.Result;
import com.kob.model.vo.request.RemarkReqVO;
import com.kob.model.vo.response.RemarkRespVO;
import com.kob.service.RemarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/discuss")
@RequiredArgsConstructor
public class RemarkController {
    private final RemarkService remarkService;

    @RateLimit(window = 60, maxRequests = 10, dimension = "user", message = "发言过于频繁")
    @PostMapping("/remark")
    public Result<?> addRemark(@Valid @RequestBody RemarkReqVO remarkReqVO) {
        String errorMessage = remarkService.addRemark(remarkReqVO);
        if (!Objects.isNull(errorMessage))
            return Result.error(errorMessage);
        return Result.success("提交成功");
    }

    @GetMapping("/remark/list")
    public Result<PageMap<RemarkRespVO>> getRemarkList(PageQuery pageQuery) {
        return Result.success(remarkService.getRemarkList(pageQuery));
    }

    @GetMapping("/remark/auth/likes")
    public Result<List<Integer>> getCurrentUserLikes() {
        return Result.success(remarkService.getCurrentUserLikes());
    }

    @GetMapping("/remark/like")
    public Result<?> likeRemark(@RequestParam Integer remarkId) {
        String errorMessage = remarkService.likeRemark(remarkId);
        if (!Objects.isNull(errorMessage))
            return Result.error(errorMessage);
        return Result.success("支持成功");
    }

    @GetMapping("/remark/dislike")
    public Result<?> dislikeRemark(@RequestParam Integer remarkId) {
        String errorMessage = remarkService.dislikeRemark(remarkId);
        if (!Objects.isNull(errorMessage))
            return Result.error(errorMessage);
        return Result.success("取消支持成功");
    }
}
