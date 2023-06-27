# 图书管理系统
## 简单介绍
基于 spring-boot 搭建的后端服务，提供对图书的增删查改功能，用户需登录获取 token 后，才能访问接口。
### 相关技术
- mybatis 操作数据库，引入通用 Mapper 减少 sql语句书写
- spring-boot-security + Jwt 实现用户鉴权，登录注册
- caffeine 实现简单本地缓存，对查询书籍接口缓存
- swagger 文档
- 日志分环境配置，见 logback-dev.xml

### 启动体验
- 创建数据库
    - 建表语句见 resource/sql 下
- 启动服务
- 调用注册接口添加一个用户
- 调用登录接口获取 access-token
  - http://localhost:8081/user/login?userName= `xxx` &password= `xxx`
- 在 swagger 文档中测试接口
  - http://localhost:8081/swagger-ui.html?access-token= `xxx`