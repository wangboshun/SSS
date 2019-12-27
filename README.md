
#实现功能：

#1.量化计算          （计算量化各类指标，包括：MACD、EMA、SMA、KDJ等）
#2.任务调度管理      （ 根据Cron命令动态配置型任务管理，可通过API进行参数管理、暂停、恢复、添加、删除等，并记录任务详细情况和错误排查）
#3.内容爬虫			  （抓取新闻信息并分析处理，持久化）
#4.权限管理			  （基于RBAC模型思想：用户、用户组、角色、角色组、权限、权限组）
#5.框架代码生成器     （一键生成服务代码，包括CURD以及各种注入）


解决方案：

1.简化DDD分层设计（领域驱动模型设计）
2.数据仓库Repository
3.DTO数据对象映射
4.API版本控制
5.Middleware中间件服务
6.缓存服务
7.Event服务
8.任务调度管理
9.文件型代码生成器

项目层级：

1.Infrastructure：基础设施层
2.Presentation：表示层
3.Domain：领域层
4.Application：应用层
5.SDK：第三方层

技术栈：

1.平台：.Net Core
2.环境：Linux、Nginx、Docker、MySql
3.前端：Uni-App、Vue.js、WebSoket
4.数据框架：Ado.Net、EntityFrameworkCore、Dapper、SqlSugar
5.文档：Swagger
6.文档：Swagger
7.对象映射：AutoMapper
8.实体验证：FluentValidation
9.缓存框架：Enyim.Memcached2、StackExchange.Redis
10.日志框架：NLog、Log4 
11.异常重试框架：Polly
12.定时任务框架：Quartz、IHostServiceHangfire、
13.网页爬虫框架：HtmlAgilityPack
14.其他框架：Ta-Lib
