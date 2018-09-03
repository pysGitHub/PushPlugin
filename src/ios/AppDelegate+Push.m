//
//  AppDelegate+Push.m
//  PTS
//
//  Created by 潘远生 on 2018/6/25.
//  Copyright © 2018年 Wistron. All rights reserved.
//

#define IOS_VERSION      [[[UIDevice currentDevice] systemVersion] floatValue]

#import "AppDelegate+Push.h"
#import  <UserNotifications/UserNotifications.h>
@interface AppDelegate ()<UNUserNotificationCenterDelegate,UIApplicationDelegate>
@end
@implementation AppDelegate (Push)


- (void)panPushLaunchOptions:(NSDictionary *)launchOptions application:(UIApplication *)application{
    if (IOS_VERSION >= 10.0) {
        UNUserNotificationCenter * center = [UNUserNotificationCenter currentNotificationCenter];
        center.delegate = self;
        UNAuthorizationOptions type = UNAuthorizationOptionBadge|UNAuthorizationOptionSound|UNAuthorizationOptionAlert;
        
        [center requestAuthorizationWithOptions:type completionHandler:^(BOOL granted, NSError * _Nullable error) {
            if (granted) {
                NSLog(@"注册成功");
            }else{
                NSLog(@"注册失败");
            }
        }];
    }
    
    
    // 注册获得device Token
    [application registerForRemoteNotifications];
}









// 将得到的deviceToken传给SDK
- (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken{
    
    NSString *deviceTokenStr = [[[[deviceToken description]
                                  stringByReplacingOccurrencesOfString:@"<" withString:@""]
                                 stringByReplacingOccurrencesOfString:@">" withString:@""]
                                stringByReplacingOccurrencesOfString:@" " withString:@""];
    //这里将token传送给服务器
    NSLog(@"将得到的deviceToken传给SDK  1 :\n%@",deviceTokenStr);
    [[NSUserDefaults standardUserDefaults] setObject:deviceTokenStr forKey:@"deviceToken"];
    //[[NSUserDefaults standardUserDefaults] setObject:receiveUrlStr forKey:@"pushUrl"];
    
    //    AppDelegate * app = (AppDelegate*)[UIApplication sharedApplication].delegate;
    //    app.string = deviceTokenStr;
}



// 注册deviceToken失败
- (void)application:(UIApplication *)application didFailToRegisterForRemoteNotificationsWithError:(NSError *)error{
    //NSLog(@"error -- %@",error);
}


//在前台
- (void)userNotificationCenter:(UNUserNotificationCenter *)center willPresentNotification:(UNNotification *)notification withCompletionHandler:(void (^)(UNNotificationPresentationOptions))completionHandler{
    
    NSDictionary * userInfo = notification.request.content.userInfo;
//    NSString * receiveUrlStr = userInfo[@"notifycontent"][@"url"];
    NSString * receiveUrlStr = userInfo[@"notifycontent"][@"url"];
    // receiveUrlStr如果不等于@""，则说明是别人给正在使用得人推送一条消息，此时播放声音；否则没有声音
    if (![receiveUrlStr isEqualToString:@""]) {
        // 需要执行这个方法，选择是否提醒用户，有Badge、Sound、Alert三种类型可以设置
        if (@available(iOS 10.0, *)) {
            completionHandler(UNNotificationPresentationOptionBadge|
                              UNNotificationPresentationOptionSound|
                              UNNotificationPresentationOptionAlert);
        } else {
            // Fallback on earlier versions
        }
    }else{
        // 需要执行这个方法，选择是否提醒用户，有Badge、Sound、Alert三种类型可以设置
        if (@available(iOS 10.0, *)) {
            completionHandler(UNNotificationPresentationOptionBadge | UNNotificationPresentationOptionAlert);
        } else {
            // Fallback on earlier versions
        }
    }
}

//>=ios10的版本，三种情况都会来到这个方法;这个方法是在用户点击了消息栏的通知，进入app后会来到这里。我们可以业务逻辑。比如跳转到相应的页面等。
- (void)userNotificationCenter:(UNUserNotificationCenter *)center didReceiveNotificationResponse:(UNNotificationResponse *)response withCompletionHandler:(void (^)(void))completionHandler{
    
    //在没有启动本App时，收到服务器推送消息，下拉消息会有快捷回复的按钮，点击按钮后调用的方法，根据identifier来判断点击的哪个按钮
    NSDictionary * userInfo = response.notification.request.content.userInfo;
//    NSString * receiveUrlStr =userInfo[@"notifycontent"][@"url"];
    NSString * receiveUrlStr =userInfo[@"notifycontent"][@"url"];

    NSLog(@"receiveUrlStr = %@",receiveUrlStr);
    

    //[PushPlugin fireDocumentEvent:@"openNotification" jsString:receiveUrlStr];
    
    [[NSNotificationCenter defaultCenter] postNotificationName:@"receiveUrlStr" object:userInfo];

    
    
    [[NSUserDefaults standardUserDefaults] setObject:receiveUrlStr forKey:@"pushUrl"];
    //处理推送过来的数据
    completionHandler();
}



@end
