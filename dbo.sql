/*
 Navicat Premium Data Transfer

 Source Server         : localdb
 Source Server Type    : SQL Server
 Source Server Version : 13004001
 Source Host           : (localdb)\MSSQLLocalDB:1433
 Source Catalog        : SSS
 Source Schema         : dbo

 Target Server Type    : SQL Server
 Target Server Version : 13004001
 File Encoding         : 65001

 Date: 19/06/2019 17:31:04
*/


-- ----------------------------
-- Table structure for Articel
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[Articel]') AND type IN ('U'))
	DROP TABLE [dbo].[Articel]
GO

CREATE TABLE [dbo].[Articel] (
  [Id] nvarchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS  NOT NULL,
  [Title] nvarchar(255) COLLATE SQL_Latin1_General_CP1_CI_AS  NULL,
  [CreateTime] datetime  NULL,
  [IsDelete] int  NULL,
  [Content] ntext COLLATE SQL_Latin1_General_CP1_CI_AS  NULL,
  [Sort] int  NULL,
  [IsMain] int  NULL,
  [ContentType] int  NULL,
  [MainImage] nvarchar(255) COLLATE SQL_Latin1_General_CP1_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[Articel] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'ID',
'SCHEMA', N'dbo',
'TABLE', N'Articel',
'COLUMN', N'Id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'标题',
'SCHEMA', N'dbo',
'TABLE', N'Articel',
'COLUMN', N'Title'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建时间',
'SCHEMA', N'dbo',
'TABLE', N'Articel',
'COLUMN', N'CreateTime'
GO

EXEC sp_addextendedproperty
'MS_Description', N'是否删除',
'SCHEMA', N'dbo',
'TABLE', N'Articel',
'COLUMN', N'IsDelete'
GO

EXEC sp_addextendedproperty
'MS_Description', N'内容',
'SCHEMA', N'dbo',
'TABLE', N'Articel',
'COLUMN', N'Content'
GO

EXEC sp_addextendedproperty
'MS_Description', N'排序',
'SCHEMA', N'dbo',
'TABLE', N'Articel',
'COLUMN', N'Sort'
GO

EXEC sp_addextendedproperty
'MS_Description', N'是否展示首页',
'SCHEMA', N'dbo',
'TABLE', N'Articel',
'COLUMN', N'IsMain'
GO

EXEC sp_addextendedproperty
'MS_Description', N'文章类型',
'SCHEMA', N'dbo',
'TABLE', N'Articel',
'COLUMN', N'ContentType'
GO

EXEC sp_addextendedproperty
'MS_Description', N'首图',
'SCHEMA', N'dbo',
'TABLE', N'Articel',
'COLUMN', N'MainImage'
GO


-- ----------------------------
-- Table structure for Eventstore
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[Eventstore]') AND type IN ('U'))
	DROP TABLE [dbo].[Eventstore]
GO

CREATE TABLE [dbo].[Eventstore] (
  [Id] varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS  NOT NULL,
  [AggregateId] varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS  NULL,
  [MsgType] nvarchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS  NULL,
  [CreateTime] datetime  NULL,
  [Data] ntext COLLATE SQL_Latin1_General_CP1_CI_AS  NULL,
  [User] nvarchar(255) COLLATE SQL_Latin1_General_CP1_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[Eventstore] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for Student
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[Student]') AND type IN ('U'))
	DROP TABLE [dbo].[Student]
GO

CREATE TABLE [dbo].[Student] (
  [Name] nvarchar(255) COLLATE SQL_Latin1_General_CP1_CI_AS  NOT NULL,
  [Age] int  NOT NULL,
  [Id] uniqueidentifier  NOT NULL,
  [CreateTime] datetime  NOT NULL,
  [IsDelete] int DEFAULT ((0)) NOT NULL
)
GO

ALTER TABLE [dbo].[Student] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for Trade
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[Trade]') AND type IN ('U'))
	DROP TABLE [dbo].[Trade]
GO

CREATE TABLE [dbo].[Trade] (
  [Coin] nvarchar(255) COLLATE SQL_Latin1_General_CP1_CI_AS  NOT NULL,
  [Side] nvarchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS  NOT NULL,
  [Id] uniqueidentifier  NOT NULL,
  [CreateTime] datetime  NOT NULL,
  [IsDelete] int DEFAULT ((0)) NOT NULL,
  [First_Trade_No] nvarchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS  NULL,
  [First_Trade_Status] int  NULL,
  [Size] float(53)  NULL,
  [First_Price] float(53)  NULL,
  [Last_Trade_No] nvarchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS  NULL,
  [Last_Price] float(53)  NULL,
  [Last_Trade_Status] int  NULL,
  [First_Time] datetime  NULL,
  [Last_Time] datetime  NULL,
  [KTime] int  NULL
)
GO

ALTER TABLE [dbo].[Trade] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for UserInfo
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[UserInfo]') AND type IN ('U'))
	DROP TABLE [dbo].[UserInfo]
GO

CREATE TABLE [dbo].[UserInfo] (
  [IsDelete] int  NULL,
  [CreateTime] datetime  NULL,
  [Id] nvarchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS  NOT NULL
)
GO

ALTER TABLE [dbo].[UserInfo] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Primary Key structure for table Articel
-- ----------------------------
ALTER TABLE [dbo].[Articel] ADD CONSTRAINT [PK__Articel__3214EC07666C7980] PRIMARY KEY CLUSTERED ([Id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table Eventstore
-- ----------------------------
ALTER TABLE [dbo].[Eventstore] ADD CONSTRAINT [PK__eventsto__3214EC0760AEE79B] PRIMARY KEY CLUSTERED ([Id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table Student
-- ----------------------------
ALTER TABLE [dbo].[Student] ADD CONSTRAINT [PK__Student__3214EC07651C1A2E] PRIMARY KEY CLUSTERED ([Id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table Trade
-- ----------------------------
ALTER TABLE [dbo].[Trade] ADD CONSTRAINT [PK__Student__3214EC07651C1A2E_copy1] PRIMARY KEY CLUSTERED ([Id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table UserInfo
-- ----------------------------
ALTER TABLE [dbo].[UserInfo] ADD CONSTRAINT [PK__UserInfo__3214EC07D6FAE624] PRIMARY KEY CLUSTERED ([Id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO

