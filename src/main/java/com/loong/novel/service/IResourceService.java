package com.loong.novel.service;

import com.loong.novel.core.common.resp.RestResp;
import com.loong.novel.dto.resp.ImgVerifyCodeRespDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author rosen
 * @date 2023/5/21 19:24
 */
public interface IResourceService {
    RestResp<ImgVerifyCodeRespDto> getImgVerifyCode() throws IOException;

    RestResp<String> uploadImage(MultipartFile file);
}
