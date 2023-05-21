package com.loong.novel.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.loong.novel.core.common.constant.ErrorCodeEnum;
import com.loong.novel.core.common.exception.BusinessException;
import com.loong.novel.core.common.resp.RestResp;
import com.loong.novel.core.constant.SystemConfigConsts;
import com.loong.novel.dto.resp.ImgVerifyCodeRespDto;
import com.loong.novel.manager.redis.VerifyCodeManager;
import com.loong.novel.service.IResourceService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * @author rosen
 * @date 2023/5/21 19:25
 */
@Slf4j
@Service
public class ResourceServiceImpl implements IResourceService {

    @Autowired
    private VerifyCodeManager verifyCodeManager;

    @Value("${novel.file.upload.path}")
    private String fileUploadPath;


    @Override
    public RestResp<ImgVerifyCodeRespDto> getImgVerifyCode() throws IOException {
        String sessionId = IdWorker.get32UUID();
        String img = verifyCodeManager.genImgVerifyCode(sessionId);
        log.info("验证码的sessionId::{}", sessionId);
        return RestResp.ok(ImgVerifyCodeRespDto.builder()
                .sessionId(sessionId)
                .img(img)
                .build()
        );
    }

    @SneakyThrows
    @Override
    public RestResp<String> uploadImage(MultipartFile file) {
        LocalDateTime now = LocalDateTime.now();
        String savePath = SystemConfigConsts.IMAGE_UPLOAD_DIRECTORY +
                now.format(DateTimeFormatter.ofPattern("yyyy")) + File.separator +
                now.format(DateTimeFormatter.ofPattern("MM")) + File.separator +
                now.format(DateTimeFormatter.ofPattern("dd"));

        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String saveFileName = IdWorker.get32UUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
        File saveFile = new File(fileUploadPath + savePath, saveFileName);
        if (!saveFile.getParentFile().exists()) {
            boolean isSuccess = saveFile.getParentFile().mkdirs();
            if (!isSuccess) {
                throw new BusinessException(ErrorCodeEnum.USER_UPLOAD_FILE_ERROR);
            }
        }
        file.transferTo(saveFile);
        if (Objects.isNull(ImageIO.read(saveFile))) {
            // 上传的文件不是图片
            Files.delete(saveFile.toPath());
            throw new BusinessException(ErrorCodeEnum.USER_UPLOAD_FILE_TYPE_NOT_MATCH);
        }
        return RestResp.ok(savePath + File.separator + saveFileName);
    }
}
