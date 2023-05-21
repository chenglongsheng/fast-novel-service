package com.loong.novel.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author rosen
 * @date 2023/5/21 20:34
 */
@Data
public class UserInfoUptReqDto {

    private Long userId;

    @Schema(description = "昵称")
    @Length(min = 2, max = 10)
    private String nickName;

    @Schema(description = "头像地址")
    @Pattern(regexp = "^/[^\s]{10,}\\.(png|PNG|jpg|JPG|jpeg|JPEG|gif|GIF|bpm|BPM)$")
    private String userPhoto;

    @Schema(description = "性别")
    @Min(value = 0)
    @Max(value = 1)
    private Integer userSex;

}
