# Stylelens - 风格透镜

> 风格透镜，分析您的最佳搭配。

## 项目简介

Stylelens 是一个基于 Spring Boot 3.0 的三层架构应用，采用行业标准设计，实现精简且可扩展的后端服务。集成 Spring AI Alibaba，支持大模型文本生成、图片生成和智能对话交互。

## 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Java | 17 | 编程语言 |
| Spring Boot | 3.0.2 | 应用框架 |
| Spring AI Alibaba | 1.0.0-M5.1 | AI大模型集成框架 |
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
│   ├── AiConfig.java         # AI配置类
│   ├── RetryConfig.java      # 重试机制配置
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
├── service/                   # 业务逻辑层
│   ├── UserService.java      # 服务接口
│   └── impl/
│       └── UserServiceImpl.java # 服务实现
└── ai/                        # AI功能模块
    ├── controller/
    │   └── AiController.java # AI接口控制器
    ├── dto/
    │   └── ChatRequest.java  # 对话请求DTO
    └── service/
        ├── AiService.java    # AI服务接口
        └── impl/
            └── AiServiceImpl.java # AI服务实现

src/main/resources/
├── application.yml           # 主配置文件（含AI配置）
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
- 阿里云百炼平台 API Key

### 数据库初始化

```bash
mysql -u root -p < src/main/resources/db/init.sql
```

### AI配置

编辑 `src/main/resources/application.yml`，只需替换以下两个占位符：

```yaml
spring.ai.dashscope:
  api-key: ${api_key}        # 替换为您的百炼平台API Key
  chat:
    options:
      model: ${model_name}   # 替换为模型名称，如 qwen-turbo
```

**获取API Key**：
1. 登录 [阿里云百炼平台](https://bailian.aliyun.com/)
2. 进入"API Key 管理"页面
3. 创建新的 API Key 并复制保存

**可用模型**：
- 文本生成：`qwen-turbo`、`qwen-plus`、`qwen-max`
- 图片生成：`wanx-v1`

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

### AI 智能服务

| 方法 | 路径 | 说明 | 请求参数 |
|------|------|------|----------|
| POST | `/api/ai/chat` | 智能对话（支持系统提示词） | ChatRequest |
| POST | `/api/ai/chat/simple` | 简单文本生成 | message |
| POST | `/api/ai/image/generate` | 图片生成 | prompt |

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

**智能对话**
```bash
curl -X POST http://localhost:8080/api/ai/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "请介绍一下人工智能的应用场景",
    "systemPrompt": "你是一个专业的AI技术顾问"
  }'
```

**简单文本生成**
```bash
curl -X POST "http://localhost:8080/api/ai/chat/simple?message=讲一个笑话"
```

**图片生成**
```bash
curl -X POST "http://localhost:8080/api/ai/image/generate?prompt=一只可爱的猫咪在草地上玩耍"
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

### AI配置详解

| 配置项 | 说明 | 默认值 | 取值范围 |
|--------|------|--------|----------|
| spring.ai.dashscope.api-key | 百炼平台API密钥 | ${api_key} | 必填 |
| spring.ai.dashscope.chat.options.model | 文本生成模型 | ${model_name} | qwen-turbo/qwen-plus/qwen-max |
| spring.ai.dashscope.chat.options.temperature | 生成随机性 | 0.7 | 0-1 |
| spring.ai.dashscope.chat.options.top-p | 核采样参数 | 0.8 | 0-1 |
| spring.ai.dashscope.chat.timeout | 文本请求超时(ms) | 30000 | >0 |
| spring.ai.dashscope.image.options.model | 图片生成模型 | wanx-v1 | wanx-v1 |
| spring.ai.dashscope.image.timeout | 图片请求超时(ms) | 60000 | >0 |
| spring.ai.dashscope.base-url | 百炼服务端点 | https://dashscope.aliyuncs.com | - |
| spring.ai.dashscope.retry.max-attempts | 最大重试次数 | 3 | 1-5 |
| spring.ai.dashscope.retry.backoff.initial-interval | 初始重试间隔(ms) | 1000 | >0 |
| spring.ai.dashscope.retry.backoff.multiplier | 重试间隔倍数 | 2.0 | >1 |
| spring.ai.dashscope.retry.backoff.max-interval | 最大重试间隔(ms) | 5000 | >0 |

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
- **AI大模型集成**：Spring AI Alibaba 标准接口
- **请求重试机制**：指数退避重试策略
- **超时控制**：细粒度请求超时配置

## 开发规范

本项目遵循《阿里巴巴Java开发手册》规范：

- 类名使用 UpperCamelCase
- 方法名使用 lowerCamelCase
- 常量全部大写并用下划线分隔
- 统一使用 Result 封装响应结果
- 使用 DTO/VO 分离实体类

## License

MIT License
