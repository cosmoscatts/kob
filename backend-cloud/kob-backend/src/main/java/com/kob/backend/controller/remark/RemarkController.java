package com.kob.backend.controller.remark;

import com.kob.backend.common.PageMap;
import com.kob.backend.common.PageQuery;
import com.kob.backend.common.Result;
import com.kob.backend.controller.remark.vo.RemarkReqVO;
import com.kob.backend.controller.remark.vo.RemarkRespVO;
import com.kob.backend.service.RemarkService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/discuss")
public class RemarkController {
    @Resource
    private RemarkService remarkService;

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
