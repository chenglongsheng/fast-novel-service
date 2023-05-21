package com.loong.novel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.loong.novel.core.common.constant.ErrorCodeEnum;
import com.loong.novel.core.common.exception.BusinessException;
import com.loong.novel.core.common.resp.RestResp;
import com.loong.novel.core.constant.DatabaseConsts;
import com.loong.novel.core.constant.SystemConfigConsts;
import com.loong.novel.core.util.JwtUtils;
import com.loong.novel.dao.entity.UserFeedback;
import com.loong.novel.dao.entity.UserInfo;
import com.loong.novel.dao.mapper.UserFeedbackMapper;
import com.loong.novel.dao.mapper.UserInfoMapper;
import com.loong.novel.dto.req.UserInfoUptReqDto;
import com.loong.novel.dto.req.UserLoginReqDto;
import com.loong.novel.dto.req.UserRegisterReqDto;
import com.loong.novel.dto.resp.UserInfoRespDto;
import com.loong.novel.dto.resp.UserLoginRespDto;
import com.loong.novel.dto.resp.UserRegisterRespDto;
import com.loong.novel.manager.redis.VerifyCodeManager;
import com.loong.novel.service.IUserService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author rosen
 * @date 2023/5/21 19:58
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private VerifyCodeManager verifyCodeManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private UserFeedbackMapper userFeedbackMapper;

    @Override
    public RestResp<UserRegisterRespDto> register(UserRegisterReqDto dto) {
        String sessionId = dto.getSessionId();
        String velCode = dto.getVelCode();
        String username = dto.getUsername();
        String password = dto.getPassword();
        // 校验验证码
        boolean ok = verifyCodeManager.imgVerifyCodeOk(sessionId, velCode);
        if (!ok) {
            throw new BusinessException(ErrorCodeEnum.USER_VERIFY_CODE_ERROR);
        }
        // 手机号已注册
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq(DatabaseConsts.UserInfoTable.COLUMN_USERNAME, username)
                .last(DatabaseConsts.SqlEnum.LIMIT_1.getSql());
        Long count = userInfoMapper.selectCount(userInfoQueryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCodeEnum.USER_NAME_EXIST);
        }

        // 注册成功，保存用户信息
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(username);
        userInfo.setNickName(username);
        userInfo.setCreateTime(LocalDateTime.now());
        userInfo.setUpdateTime(LocalDateTime.now());
        userInfo.setSalt("0");
        userInfo.setPassword(DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8)));
        userInfoMapper.insert(userInfo);

        // 删除验证码
        verifyCodeManager.removeImgVerifyCode(sessionId);
        // 返回JWT
        return RestResp.ok(UserRegisterRespDto.builder()
                .uid(userInfo.getId())
                .token(jwtUtils.generateToken(userInfo.getId(), SystemConfigConsts.NOVEL_FRONT_KEY))
                .build()
        );
    }

    @Override
    public RestResp<UserLoginRespDto> login(UserLoginReqDto dto) {
        // 查询用户信息
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.UserInfoTable.COLUMN_USERNAME, dto.getUsername())
                .last(DatabaseConsts.SqlEnum.LIMIT_1.getSql());
        UserInfo userInfo = userInfoMapper.selectOne(queryWrapper);
        if (Objects.isNull(userInfo)) {
            // 用户不存在
            throw new BusinessException(ErrorCodeEnum.USER_ACCOUNT_NOT_EXIST);
        }

        // 判断密码是否正确
        if (!Objects.equals(userInfo.getPassword()
                , DigestUtils.md5DigestAsHex(dto.getPassword().getBytes(StandardCharsets.UTF_8)))) {
            // 密码错误
            throw new BusinessException(ErrorCodeEnum.USER_PASSWORD_ERROR);
        }

        // 登录成功，生成JWT并返回
        return RestResp.ok(UserLoginRespDto.builder()
                .token(jwtUtils.generateToken(userInfo.getId(), SystemConfigConsts.NOVEL_FRONT_KEY))
                .uid(userInfo.getId())
                .nickName(userInfo.getNickName()).build());
    }

    @Override
    public RestResp<UserInfoRespDto> getUserInfo(Long userId) {
        UserInfo userInfo = userInfoMapper.selectById(userId);
        return RestResp.ok(UserInfoRespDto.builder()
                .nickName(userInfo.getNickName())
                .userSex(userInfo.getUserSex())
                .userPhoto(userInfo.getUserPhoto())
                .build());
    }

    @Override
    public RestResp<Void> updateUserInfo(UserInfoUptReqDto dto) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(dto.getUserId());
        userInfo.setNickName(dto.getNickName());
        userInfo.setUserPhoto(dto.getUserPhoto());
        userInfo.setUserSex(dto.getUserSex());
        userInfoMapper.updateById(userInfo);
        return RestResp.ok();
    }

    @Override
    public RestResp<Void> saveFeedback(Long userId, String content) {
        UserFeedback userFeedback = new UserFeedback();
        userFeedback.setUserId(userId);
        userFeedback.setContent(content);
        userFeedback.setCreateTime(LocalDateTime.now());
        userFeedback.setUpdateTime(LocalDateTime.now());
        userFeedbackMapper.insert(userFeedback);
        return RestResp.ok();
    }

    @Override
    public RestResp<Void> deleteFeedback(Long userId, Long id) {
        QueryWrapper<UserFeedback> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.CommonColumnEnum.ID.getName(), id)
                .eq(DatabaseConsts.UserFeedBackTable.COLUMN_USER_ID, userId);
        userFeedbackMapper.delete(queryWrapper);
        return RestResp.ok();
    }
}
