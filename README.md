## 工程说明
> 本工程为基础集成开发模版工程，用于标准化前后台分离快速开发，额外前台配套web端、移动端h5（）；
> 基于java1.8，采用springboot及相关框架，集成mysql5.7、redis6+、mongoDB4+、kafka2.8+；
> 提供基本用户登录、角色资源管理授权、权限拦截、xss/sql注入拦截、日志审计、文件上传下载管理、登录安全等基础能力。



## 运行条件
* 环境软件：java1.8、mysql5.7、redis6+、mongodb4+、kafka2.8+
* 框架：spring-boot2.7+（主要）
* 服务器环境：linux（推荐uos欧拉版）、openjdk1.8（可使用华为jdk1.8)
* 开发环境：windows/linux/macos、maven3、git


## 运行说明
* 依据运行条件要求，安装各依赖软件；
* 创建mysql数据库（server/client编码均为utf8mb4,collect为utf8mb4_general_ci），设置账号密码；执行初始化sql脚本/resource/sql/init.sql；
* 创建mongodb数据库及对应账号密码；
* 修改application-dev.properties中的配置（从application.properties复制相关数据库、中间件的配置，进行对应修改）；
* 运行Application.java


## 使用限制
* 安全：本开发工程归属企业，不得未经允许对外转发或公布在开源社区；
* 开发：com.hnyr.sys包为平台系统基础代码，原则上不得进行改动，避免影响后续升级；
* 开发：业务系统代码包，在com.hnyr下创建，如com.hnyr.xy；
* 开发：controller api均以/api开头；提供给h5访问的以/api/wap开头；
* 开发：响应数据包应采用统一的数据包装类返回ResponseResult；
* 开发：其他开发细节限制和使用方法，可参见开发帮助文档。
