//
//  SSODef.h
//  sso
//
//  Created by garygong on 16/7/26.
//  Copyright © 2016年 garygong. All rights reserved.
//

#ifndef SSODef_h
#define SSODef_h

typedef NS_ENUM(NSInteger, SSOError) {
    SSO_ERROR_SUCCESS               = 0, // 成功
    SSO_ERROR_INVALID_SIGN          = 1, // 验签失败
    SSO_ERROR_REQUEST_TIMEOUT       = 2, // 请求已超时
    SSO_ERROR_ACCOUNT_EXCEPTION     = 6, // 账户异常，暂不能使用一账通
    SSO_ERROR_FATAL                 = 9,
    
    SSO_ERROR_GEN_VCODE_FAILURE     = 11, // 生成图形验证码错误
    
    SSO_ERROR_WRONG_MOBILE          = 21, // 手机号码错误
    SSO_ERROR_INVALID_MOBILE_FORMAT = 22, // 手机号码格式不正确
    SSO_ERROR_NEED_LOGIN            = 23, // 没有登录
    SSO_ERROR_OTP_EXPIRED           = 24, // 动态码已过期
    SSO_ERROR_INVALID_OTP           = 25, // 动态码无效
    SSO_ERROR_WRONG_OTP             = 26, // 动态码错误
    SSO_ERROR_FREQ_LIMIT            = 27, // 手机接受动态码已超过规定次数
    SSO_ERROR_WRONG_VCODE           = 28, // 图形验证码错误
    SSO_ERROR_SEND_FAILURE          = 29, // 发送OTP失败
    SSO_ERROR_OTP_CONTENT_EMPTY     = 30, // 发送otp内容不能为空
    SSO_ERROR_VCODE_ID_EMPTY        = 31, // 图形验证码ID为空
    SSO_ERROR_ACCOUNT_LOCKED        = 32, // 帐户已被锁定
    SSO_ERROR_OTP_EMPTY             = 33, // OTP不能为空
    SSO_ERROR_MOBILE_EMPTY          = 34, // 手机号码为空
    SSO_ERROR_REPEAT_VCODE          = 35, // 30秒内不能重复发送OTP
    SSO_ERROR_VCODE_EXPIRED         = 37, // 图片验证码验证码过期
    
    SSO_ERROR_MOBILE_ALREADY_REG    = 51, // 手机号码已注册
    SSO_ERROR_MOBILE_SET_ALIAS      = 52, // 手机号码已被设置别名
    SSO_ERROR_MOBILE_LOGINNAME      = 53, // 手机号码已被设置为登录名称
    SSO_ERROR_MOBILE_BLACKLIST      = 54, // 手机号码列入黑名单
    SSO_ERROR_INVALID_TOKEN         = 57, // token失效,此时需要重新登录,方能从事
    SSO_ERROR_VICIOUSLY             = 62, // 恶意操作
    SSO_ERROR_MOBILE_REG_EXCEPTION  = 63, // 注册异常
    SSO_ERROR_BEYOND_5TIMES         = 65, // 连续5次输入错误密码，24小时内无法再登录
    SSO_ERROR_AUTH_ERROR            = 66, // 认证失败
    SSO_ERROR_ACCOUNT_EMPTY         = 68, // 帐号不存在
    SSO_ERROR_BEYOND_4TIMES         = 69, // 连续4次输入错误密码，账户即将被锁定,请找回密码
    
    SSO_ERROR_INVALID_RESP          = 201, // 返回内容格式错误
    SSO_ERROR_DECRYPT_ERROR         = 202, // 解密失败
    SSO_ERROR_CANNOT_AUTH           = 203, // 不能鉴权
    SSO_ERROR_NOT_LOGIN             = 204, // 没有登录过
    SSO_ERROR_RESP_EMPTY            = 205, // 响应为空
    SSO_ERROR_PRASE_FAIL            = 406, // 解析响应失败
};

FOUNDATION_EXPORT NSString * const SSOVersion;
FOUNDATION_EXPORT NSString * const SSOErrorDomain;

typedef void (^SSOEngineCompleted) (NSError *error, id object);

typedef void(^SSOLogFunc)(NSString *log);

#endif /* SSODef_h */
