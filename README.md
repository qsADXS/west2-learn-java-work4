# west2-learn-java-work4
西二在线java考核作业4-合作作业稀土掘金

[接口文档](https://apifox.com/apidoc/shared-b3b425f1-a75d-4ae7-9fbd-f6bd93660c7b)

- 基于`spring-boot-3.2.2`写的程序
- mysql使用了三个表，用来放用户信息，文章，和评论
- 评论的id是通过雪花算法生成，其他是普通的id
- 没有用`mybatis-plus`，因为`mybatis-plus`是后面学的，和`mybatis`一起用的时候有冲突
- 部署在`8.134.219.139:6666`
- 完成了基本的内容~(应该没有遗漏吧)~
- 用了安全框架`springsecurity`~（好难，学不懂一点)~
- 头像上传是变成`base64`后储存在`mysql`的
- 感觉没怎么用日志的功能
- 对于文章的点击次数，有改进空间，对每个用户的每次点击设置一个过期时间，点击的时候放进`redis`，过期前不会再增加这个用户对这篇文章的点击次数
- 然后token的设置，也可以放在`redis`中~(但是我没放)~
- [这是测试接口的截图](./test.md)

- tree

```
│  pom.xml       
├─src
│  ├─main
│  │  ├─java
│  │  │  └─com
│  │  │      └─qsadxs
│  │  │          └─project
│  │  │              │  ProjectApplication.java
│  │  │              │  
│  │  │              ├─config
│  │  │              │      CommonSecurityConfig.java
│  │  │              │      RedisConfig.java
│  │  │              │      SecurityConfig.java
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
│  │  │              │      JwtUtils.java
│  │  │              │      RedisServer.java
│  │  │              │      ResultMap.java
│  │  │              │      SnowflakeIdGenerator.java
│  │  │              │      TokenFilter.java
│  │  │              │      UserDetailsServiceImpl.java
│  │  │              │      
│  │  │              ├─Mapper
│  │  │              │      ArticleMapper.java
│  │  │              │      CommentMapper.java
│  │  │              │      UserMapper.java
│  │  │              │      
│  │  │              └─pojo
│  │  │                      Article.java
│  │  │                      Comment.java
│  │  │                      User.java
│  │  │                      
│  │  └─resources
│  │          application.yaml
│  │          banner.txt
```

