# PushPlugin
push for ionic
使用方法：
1.在自己的项目中使用ionic cordova plugin add cordova-plugin-push-master加载插件
2.在想要注册推送的.ts文件中添加以下操作：
① 在import和@Component添加 declare let cordova: any;
② 实现下面的方法






 // 拿token
      cordova.plugins.PushPlugin.initPushMethod("ios", (message)=> {
        alert("deviceToken = "+message)
        // 存储接收到的deviceToken
        this.storage.set('deviceToken',message);
        // 存储pushServerID
        this.storage.set('pushServeID','1');
      },
      (error)=> {
        // alert(error)
      });


      cordova.plugins.PushPlugin.getPushToken("android",(message)=> {
        alert("deviceToken = "+message)
        // 存储接收到的deviceToken
        this.storage.set('deviceToken',message);
        // 存储pushServerID
        this.storage.set('pushServeID','2');
      },
      (error)=> {
        // alert(error)
      });
        
      // 拿推送消息
      cordova.plugins.PushPlugin.receivePushUrl("common",(message)=> {
        alert("urlStr = "+message)
      },
      (error)=> {
        // alert(error)
      });
      
 3.在ios平台中添加下列代码
 ① 在AppDelegte.h声明下列属性：
            @property (nonatomic,strong)NSDictionary * launchOptionDic;
            
            
            
 ② 在AppDelegate.m的入口类添加下列代码：
         if(launchOptions){
         self.launchOptionDic = launchOptions;
         }
  
   
    
