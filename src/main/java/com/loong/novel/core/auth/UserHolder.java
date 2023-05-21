package com.loong.novel.core.auth;

/**
 * 用户信息 持有类
 *
 * @author rosen
 * @date 2023/5/21 8:55
 */
public final class UserHolder {

    /**
     * 当前线程用户ID
     */
    private static final ThreadLocal<Long> userIdTL = new ThreadLocal<>();

    /**
     * 当前线程作家ID
     */
    private static final ThreadLocal<Long> authorIdTL = new ThreadLocal<>();

    private UserHolder() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static Long getUserId() {
        return userIdTL.get();
    }

    public static void setUserId(Long userId) {
        userIdTL.set(userId);
    }

    public static Long getAuthorId() {
        return authorIdTL.get();
    }

    public static void setAuthorId(Long authorId) {
        authorIdTL.set(authorId);
    }

    public static void clear() {
        userIdTL.remove();
        authorIdTL.remove();
    }

}
