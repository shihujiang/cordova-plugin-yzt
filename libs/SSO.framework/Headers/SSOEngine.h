//
//  SSOEngine.h
//  sso
//
//  Created by garygong on 16/7/14.
//  Copyright © 2016年 garygong. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SSODef.h"

@interface SSOEngine : NSObject


+ (instancetype)sharedInstance;

/**
 *  设置日志函数，如果设置，则会输出日志，便于定位问题
 */
- (void)setLogFunc:(SSOLogFunc)logFunc;

/**
 *  Debug信息开关, 默认关闭
 */
- (void)enableDebugMode:(BOOL)mode;

/**
 *  使用账号＋密码登录
 *  @param loginName 登录账号
 *  @param password 登录密码
 *  @param isAppUser Y表示loginName是App自己的账号，N表示是一账通账号
 *  @param mamcAppId 任意门ID
 *  @param completed 登录成功或者失败后调用该block
 */
- (BOOL)loginWithAccountAndPassword:(NSString *)loginName
                           password:(NSString *)password
                          isAppUser:(BOOL)isAppUser
                          mamcAppId:(NSString *)mamcAppId
                          completed:(SSOEngineCompleted)completed;

/**
 *  一账通与业务帐号体系并存，业务帐号登录成功后，调用该接口告知一账通业务方帐号的登录信息
 *  @param loginName 业务方登录账号
 *  @param sysId 用户名主键
 *  @param completed 登录成功或者失败后调用该block
 */
- (BOOL)recordAppLoginResult:(NSString *)loginName
                       sysId:(NSString *)sysId
                   completed:(SSOEngineCompleted)completed;

@end
