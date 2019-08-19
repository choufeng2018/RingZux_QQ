安卓qq协议文档:
https://saint-theana.github.io/Androidqq_Protocol_Doc/

* 发送消息
    * 消息类型
        * - [x] 发送文本消息(大概300多字以内)
        * - [x] 发送长文本消息(大约3500个字左右)
        * - [x] 表情
        * - [x] 艾特
             * - [x] 单体(多个)
             * - [x] 全体
        * - [x] 发送图片消息
            * - [x] jpg
            * - [x] png
            * - [x] gif
        * - [x] 发送语音消息
            * - [x] silk
            * - [x] amr
        * - [x] 发送xml消息
        * - [x] 发送json消息
    * 发送方向
        * - [x] 讨论组
        * - [x] 群
        * - [x] 好友
        * - [x] 群成员临时
        
* 其他功能
    * - [x] 禁言
        * - [x] 单体
        * - [x] 全体
    * - [x] 入群请求
        * - [x] 监听
        * - [x] 处理
    * - [x] 好友请求
        * - [x] 监听
        * - [x] 处理
    * - [x] 加好友
    * - [x] 监听新人入群
    * - [ ] 监听好友增加
    * - [x] 监听消息撤回
    * - [x] 获取群列表
    * - [x] 获取好友列表
    * - [x] 获取群成员列表
    * - [x] 群踢人
    * - [x] 撤回群成员消息
    * - [x] 修改群名片

    






![image](https://github.com/Saint-Theana/RingZux_QQ/raw/master/screenshot/1.png)
![image](https://github.com/Saint-Theana/RingZux_QQ/raw/master/screenshot/2.png)
![image](https://github.com/Saint-Theana/RingZux_QQ/raw/master/screenshot/3.png)
![image](https://github.com/Saint-Theana/RingZux_QQ/raw/master/screenshot/4.png)





声明:

本项目由纯java代码开发，核心代码为pcqq协议，翻译自网络上其他开源的pcqq协议代码，使用本项目代码产生的任何后果与本人无关。

功能:

  静态代码类:

    登录qq，获取群列表/好友列表，发送接受群/好友消息，已插件化，可用java/lua插件
  动态代码类:

    可用tui实现简单聊天

依赖:

    json,luaj,lanterna


说明:本项目qq协议部分不会再更新(pc协议难分析)。如果想要其他功能(发送图片/语音消息)联系我。

