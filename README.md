# 1.需求分析
（1）注册/登录

（2）用户管理（仅管理员可见，对用户的查询或者修改）

（3）用户校验（仅会员用户可见）



# 2.技术选型
后端：
- java
- Spring
- Springmvc
- MyBatis
- MyBatis plus
- SpringBoot
- JUnit5
- MySQL数据库

部署：服务器/容器（平台）

# 3.库表设计
id(主键) bigint

username昵称varchar

userAccount登录账号varchar

avatarUrl头像varchar

gender 性别 tinyint

userPassword密码varchar

phone 电话varchar

email邮箱varchar

userStatus用户状态int 0-正常

createTime 创建时间（数据插入时间) datetime

updateTime更新时间(数据更新时间) datetime

isDelete是否删除01(逻辑删除) tinyint

userRole 用户角色0–普通用户1-管理员

# 4.注册逻辑设计
（1）用户在前端输入账户和密码、以及校验码(todo)

（2）校验用户的账户、密码、校验密码、是否符合要求
- 非空 
- 账户长度不小于4位，密码不小于8位
- 账户不能重复
- 账户不包含特殊字符
- 密码和校验密码相同
（3）对密码进行加密

（4）向数据库插入用户数据

# 5.登录功能
## 5.1 接口设计
接受参数:用户账户、密码

请求类型:POST（请求参数很长时不建议用get）

请求体:JSON格式的数据

返回值:用户信息（脱敏)
## 5.2登录逻辑
（1）校验用户账户和密码是否合法
- 非空
- 账户长度不小于4位，密码就不小于8位
- 账户不包含特殊字符
（2）校验密码是否输入正确，要和数据库中的密文密码去对比
- 
（3）用户信息脱敏，隐藏敏感信息，防止数据库中的字段泄露

（4）我们要记录用户的登录态(session)，将其存到服务器上（用后端SpringBoot框架封装的服务器
tomcat去记录)

（5）返回脱敏后的用户信息





