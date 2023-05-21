package com.loong.novel.service;

import com.loong.novel.core.common.resp.RestResp;
import com.loong.novel.dto.req.UserInfoUptReqDto;
import com.loong.novel.dto.req.UserLoginReqDto;
import com.loong.novel.dto.req.UserRegisterReqDto;
import com.loong.novel.dto.resp.UserInfoRespDto;
import com.loong.novel.dto.resp.UserLoginRespDto;
import com.loong.novel.dto.resp.UserRegisterRespDto;

/**
 * @author rosen
 * @date 2023/5/21 19:58
 */
public interface IUserService {
    RestResp<UserRegisterRespDto> register(UserRegisterReqDto dto);

    RestResp<UserLoginRespDto> login(UserLoginReqDto dto);

    RestResp<UserInfoRespDto> getUserInfo(Long userId);

    RestResp<Void> updateUserInfo(UserInfoUptReqDto dto);

    RestResp<Void> saveFeedback(Long userId, String content);

    RestResp<Void> deleteFeedback(Long userId, Long id);
}
