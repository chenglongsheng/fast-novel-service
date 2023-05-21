package com.loong.novel.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * @author rosen
 * @date 2023/5/21 23:32
 */
@Data
@Builder
public class ChapterContentRespDto {

    /**
     * 章节标题
     */
    @Schema(description = "章节名")
    private String chapterName;

    /**
     * 章节内容
     */
    @Schema(description = "章节内容")
    private String chapterContent;

    /**
     * 是否收费;1-收费 0-免费
     */
    @Schema(description = "是否收费;1-收费 0-免费")
    private Integer isVip;

}
