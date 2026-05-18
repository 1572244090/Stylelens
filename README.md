# Stylelens - 风格透镜

> 风格透镜，分析您的最佳搭配。

## 项目简介

Stylelens 是一个基于 Spring Boot 3.0 的三层架构应用，采用行业标准设计，实现精简且可扩展的后端服务。

## 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Java | 17 | 编程语言 |
| Spring Boot | 3.0.2 | 应用框架 |
| MyBatis-Plus | 3.5.5 | ORM框架 |
| MySQL | 8.0+ | 关系型数据库 |
| Druid | 1.2.20 | 数据库连接池 |
| Redis | - | 缓存服务 |
| Lombok | 1.18.30 | 代码简化 |
| Hutool | 5.8.25 | Java工具库 |

## 项目结构

```
src/main/java/com/stylelens/www/
├── common/                    # 通用类
│   ├── Result.java           # 统一响应结果封装
│   └── PageRequest.java      # 分页请求参数
├── config/                    # 配置类
│   └── MybatisPlusConfig.java # MyBatis-Plus配置
├── controller/                # 控制层（RESTful API）
│   └── UserController.java   # 用户接口
├── dao/                       # 数据访问层
│   └── UserMapper.java       # Mapper接口
├── dto/                       # 数据传输对象
│   ├── UserCreateDTO.java    # 创建用户DTO
│   ├── UserUpdateDTO.java    # 更新用户DTO
│   └── UserVO.java           # 用户视图对象
├── entity/                    # 实体类
│   └── User.java             # 用户实体
├── exception/                 # 异常处理
│   ├── BusinessException.java # 业务异常
│   └── GlobalExceptionHandler.java # 全局异常处理器
└── service/                   # 业务逻辑层
    ├── UserService.java      # 服务接口
    └── impl/
        └── UserServiceImpl.java # 服务实现

src/main/resources/
├── application.yml           # 主配置文件
├── application-dev.yml       # 开发环境配置
├── application-prod.yml      # 生产环境配置
├── db/
│   └── init.sql              # 数据库初始化脚本
└── mapper/                   # MyBatis XML映射文件
```

## 快速开始

### 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Redis（可选）

### 数据库初始化

```bash
mysql -u root -p < src/main/resources/db/init.sql
```

### 配置修改

编辑 `src/main/resources/application-dev.yml`，修改数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/stylelens?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
```

### 启动应用

```bash
mvn spring-boot:run
```

应用启动后访问：`http://localhost:8080/api`

## API 接口

### 用户管理

| 方法 | 路径 | 说明 | 请求体 |
|------|------|------|--------|
| POST | `/api/users` | 创建用户 | UserCreateDTO |
| GET | `/api/users/{id}` | 获取用户详情 | - |
| GET | `/api/users` | 分页查询用户列表 | pageNum, pageSize |
| PUT | `/api/users` | 更新用户 | UserUpdateDTO |
| DELETE | `/api/users/{id}` | 删除用户 | - |

### 请求示例

**创建用户**
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "phone": "13800138000"
  }'
```

**查询用户列表**
```bash
curl http://localhost:8080/api/users?pageNum=1&pageSize=10
```

### 响应格式

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

## 配置说明

### 环境切换

通过修改 `application.yml` 中的 `spring.profiles.active` 切换环境：

```yaml
spring:
  profiles:
    active: dev  # dev: 开发, prod: 生产
```

### 主要配置项

| 配置项 | 说明 | 默认值 |
|--------|------|--------|
| server.port | 服务端口 | 8080 |
| server.servlet.context-path | 上下文路径 | /api |
| spring.datasource.url | 数据库连接URL | - |
| spring.datasource.druid.max-active | 最大连接数 | 20 |
| mybatis-plus.mapper-locations | Mapper XML路径 | classpath:mapper/*.xml |

## 架构特性

- **依赖注入**：通过 Spring DI 实现层间解耦
- **面向接口编程**：Service 层定义清晰的业务契约
- **统一异常处理**：`@RestControllerAdvice` 全局异常捕获
- **参数校验**：Jakarta Validation 注解校验
- **事务管理**：`@Transactional` 声明式事务
- **逻辑删除**：MyBatis-Plus 逻辑删除支持
- **自动填充**：创建/更新时间自动填充
- **分页查询**：MyBatis-Plus 分页插件

## 开发规范

本项目遵循《阿里巴巴Java开发手册》规范：

- 类名使用 UpperCamelCase
- 方法名使用 lowerCamelCase
- 常量全部大写并用下划线分隔
- 统一使用 Result 封装响应结果
- 使用 DTO/VO 分离实体类

## License

MIT License
