# west2-learn-java-work4
西二在线java考核作业4-合作作业稀土掘金

[接口文档在线链接](https://apifox.com/apidoc/shared-b3b425f1-a75d-4ae7-9fbd-f6bd93660c7b)

- 基于`spring-boot-3.2.2`写的程序
- mysql使用了三个表，[`User`](#User)、[`Comment`](#Comment)、[`Article`](#Article)
- 评论的id是通过雪花算法生成，其他是普通的id
- 没有用`mybatis-plus`，因为`mybatis-plus`是后面学的，和`mybatis`一起用的时候有冲突
- 部署在`8.134.219.139:6666`
- 完成了基本的内容 ~(应该没有遗漏吧)~
- 用了安全框架`springsecurity`
- 通过`spring mvc`和`redis`设置了拦截器实现ip的频率访问限制 ~(但是逻辑实现有优化空间)~
- 头像上传是变成`base64`后储存在`mysql`的
- 对于文章的点击次数，有改进空间，对每个用户的每次点击设置一个过期时间，点击的时候放进`redis`，过期前不会再增加这个用户对这篇文章的点击次数
- 然后token的设置，也可以放在`redis`中 ~(但是我没放)~
- [这是测试接口的截图](./test.md)
- 对于头像的存储，原本是想把文件保存在服务器上的，但是不知道为什么显示文件不存在(在本地、直接在服务器运行都是，[controller.AvatarController](.\main\java\com\qsadxs\project\controller\AvatarController.java)中的注释部分有保存为文件的代码)，最后选择转成`base64`丢`mysql`里面
- tree

```
├─src
│  ├─main
│  │  ├─java
│  │  │  └─com
│  │  │      └─qsadxs
│  │  │          └─project
│  │  │              ├─bo
│  │  │              │      UserDetailsServiceImpl.java
│  │  │              │
│  │  │              ├─component
│  │  │              │      RateLimitInterceptor.java
│  │  │              │      TokenFilter.java
│  │  │              │
│  │  │              ├─config
│  │  │              │      CommonSecurityConfig.java
│  │  │              │      RedisConfig.java
│  │  │              │      SecurityConfig.java
│  │  │              │      WebConfig.java
│  │  │              │
│  │  │              ├─controller
│  │  │              │      ArticleController.java
│  │  │              │      AvatarController.java
│  │  │              │      CommentController.java
│  │  │              │      HomeController.java
│  │  │              │      UserController.java
│  │  │              │      welcome.java
│  │  │              │
│  │  │              ├─Dao
│  │  │              │      ArticleMapper.java
│  │  │              │      CommentMapper.java
│  │  │              │      RedisServer.java
│  │  │              │      UserMapper.java
│  │  │              │
│  │  │              ├─pojo
│  │  │              │      Article.java
│  │  │              │      Comment.java
│  │  │              │      ResultMap.java
│  │  │              │      User.java
│  │  │              │
│  │  │              └─util
│  │  │                      JwtUtils.java
│  │  │                      SnowflakeIdGenerator.java
│  │  │
│  │  └─resources
│  │          application.yaml
│  │          banner.txt
```

- `Article`表

  <a id="Article"></a>

  ![image-20240306201250233](https://s2.loli.net/2024/03/06/oqs1YwTUQcjvyfr.png)

- `Comment`表

  <a id="Comment"></a>

  ![image-20240306201324891](https://s2.loli.net/2024/03/06/esH5xB6KLU21Squ.png)

- `User`表

  <a id="User"></a>

  ![image-20240306201939971](https://s2.loli.net/2024/03/06/nPNy5X2togl1AQG.png)
