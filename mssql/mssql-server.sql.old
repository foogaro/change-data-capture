create database debezium;
use debezium;
CREATE TABLE debezium.dbo.users (
 user_id INT IDENTITY (1, 1) PRIMARY KEY,
 name VARCHAR (255) NOT NULL,
 lastname VARCHAR (255) NOT NULL,
 username VARCHAR (255) NOT NULL,
 email VARCHAR (255)
);


USE master  
GO  
SELECT name, database_id, is_cdc_enabled FROM sys.databases

USE debezium  
GO  
EXEC sys.sp_cdc_enable_db  
GO  
USE debezium  
GO  
EXEC sys.sp_cdc_enable_table  
@source_schema = N'dbo',  
@source_name   = N'users',
@role_name     = NULL,  
@supports_net_changes = 1  
GO  


USE master  
GO  
SELECT name, database_id, is_cdc_enabled FROM sys.databases

USE master
GO  
SELECT srvname AS OldName FROM master.dbo.sysservers
SELECT SERVERPROPERTY('ServerName') AS NewName

USE master
GO  
sp_dropserver 'e170dd6807e3';  
GO  
sp_addserver 'e170dd6807e3', local;  
GO 
 
USE debezium  
GO  
select * from debezium.dbo.users
GO
INSERT INTO debezium.dbo.users (name, lastname, username, email) VALUES ('Luigi','Fugaro','foogaro','luigi@foogaro.com');
GO
select * from debezium.dbo.users
GO

USE debezium  
GO  
insert into debezium.dbo.users (name, lastname, username, email) values ('Gigi','Foo','foogaro','luigi@foogaro.com');
delete from debezium.dbo.users where name = 'Gigi';
select * from debezium.dbo.users


