/********* YiZhangTong.m Cordova Plugin Implementation *******/

#import <Cordova/CDV.h>
#import <SSO/SSODef.h>
#import <SSO/SSOEngine.h>

@interface YiZhangTong : CDVPlugin {
  // Member variables go here.
}

- (void)coolMethod:(CDVInvokedUrlCommand*)command;
@end

@implementation YiZhangTong

- (void)coolMethod:(CDVInvokedUrlCommand*)command
{
    __block CDVPluginResult* pluginResult = nil;
    
    NSString* mid = [command.arguments objectAtIndex:0];
    NSString *account = [command.arguments objectAtIndex:1];
    NSString *pwd = [command.arguments objectAtIndex:2];
    NSString *debugStr = [command.arguments objectAtIndex:3];
    BOOL isDebug = (debugStr.integerValue == 0) ? YES : NO;
    
   // NSLog(@"mid = %@, account = %@, pwd = %@, debugStr = %@",mid,account,pwd,debugStr);
    [[SSOEngine sharedInstance] enableDebugMode:isDebug];
    
//    [[SSOEngine sharedInstance]setLogFunc:^(NSString *log) {
//        NSLog(@"log%@",log);
//    }];
    
    [[SSOEngine sharedInstance] loginWithAccountAndPassword:account password:pwd isAppUser:NO mamcAppId:nil completed:^(NSError *error, id object) {
        NSLog(@"error%@",error);
        NSString *retJsonResult = @"";
        
        if (error) {
            //
            NSString *errCode = [NSString stringWithFormat:@"%zi",error.code];
            NSString *errMsg  = error.domain;
            
            NSMutableDictionary *retDic = [[NSMutableDictionary alloc] init];
            [retDic setObject:errCode forKey:@"result"];
            [retDic setObject:errMsg forKey:@"retmsg"];
            
            NSData *tempJsonData = [NSJSONSerialization dataWithJSONObject:retDic options:NSJSONWritingPrettyPrinted error:nil];
            NSString *resultJsonStr = [[NSString alloc] initWithData:tempJsonData encoding:NSUTF8StringEncoding];
            //需要输出的json字符串
            //NSLog(@"result = %@",resultJsonStr);
            
            retJsonResult = resultJsonStr;
        }else {
            //NSLog(@"%@", object);
            //返回dictionary
            NSMutableDictionary *jsonDic = (NSMutableDictionary *)object;
            NSDictionary *assertionDic = [jsonDic objectForKey:@"assertion"];
            
            NSDictionary *tempResultDic = [assertionDic objectForKey:@"result"];
            NSString *resultCode = [tempResultDic objectForKey:@"text"];//0000success,other fail
            
            NSString *msgStr = [assertionDic objectForKey:@"message"];
            
            
            //需要输出的json字符串
            NSMutableDictionary *retDic = [[NSMutableDictionary alloc] init];
            [retDic setObject:resultCode forKey:@"result"];
            
            if ([resultCode isEqualToString:@"0000"]) {
                
                NSMutableDictionary *attrDic = [assertionDic objectForKey:@"attributes"];;
                NSArray *attrArray = [attrDic objectForKey:@"attribute"];
                
                NSMutableDictionary *dataDic = [[NSMutableDictionary alloc] init];
                NSString *mobileStr = @"";
                NSString *typeStr = @"";
                NSString *noStr = @"";
                NSString *sexStr = @"";
                NSString *cnStr = @"";
                
                for (NSInteger i = 0; i < attrArray.count; i++) {
                    NSDictionary *tempAttrDic = attrArray[i];
                    
                    NSString *tempDicKey = [tempAttrDic objectForKey:@"key"];
                    NSString *tempDicValue = [tempAttrDic objectForKey:@"value"];
                    
                    
                    if ([tempDicKey isEqualToString:@"mobileNo"]) {
                        mobileStr = tempDicValue;
                    }else if ([tempDicKey isEqualToString:@"idType"]){
                        typeStr = tempDicValue;
                    }else if ([tempDicKey isEqualToString:@"idNo"]){
                        noStr = tempDicValue;
                    }else if ([tempDicKey isEqualToString:@"sex"]){
                        if ([tempDicValue isEqualToString:@"M"]) {
                            sexStr = @"男";
                        }else{
                            sexStr = @"女";
                        }
                    }else if ([tempDicKey isEqualToString:@"cnName"]){
                        cnStr = tempDicValue;
                    }
                    
                }
                
                [dataDic setObject:mobileStr forKey:@"mobileNo"];
                [dataDic setObject:typeStr forKey:@"idType"];
                [dataDic setObject:noStr forKey:@"idNo"];
                [dataDic setObject:sexStr forKey:@"sex"];//男,女
                [dataDic setObject:cnStr forKey:@"cnName"];
                
                [retDic setObject:dataDic forKey:@"data"];
            }else{
                //有错误信息 retmsg
                [retDic setObject:msgStr forKey:@"retmsg"];
            }
            
            NSData *tempJsonData = [NSJSONSerialization dataWithJSONObject:retDic options:NSJSONWritingPrettyPrinted error:nil];
            NSString *resultJsonStr = [[NSString alloc] initWithData:tempJsonData encoding:NSUTF8StringEncoding];
            //需要输出的json字符串
            //NSLog(@"result = %@",resultJsonStr);
            
            retJsonResult = resultJsonStr;
            
//            {"result":"0000","data":{"mobileNo":"18513855349","idType":"","idNo":"","sex":"","cnName":""}}
        }
        
        
        if (retJsonResult != nil && [retJsonResult length] > 0) {
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:retJsonResult];
        } else {
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
        }
        
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];


    
}

@end
