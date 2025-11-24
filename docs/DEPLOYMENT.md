# 部署指南

## 本地开发环境

### 后端部署

#### 1. 前置要求
- Java 17 或更高版本
- Maven 3.6+

#### 2. 启动步骤

```bash
# 进入后端目录
cd backend

# 方式一：使用 Maven 直接运行
mvn spring-boot:run

# 方式二：打包后运行
mvn clean package
java -jar target/zenday-backend-1.0.0.jar
```

#### 3. 验证启动

访问 http://localhost:8080/api/users 应该返回空数组或用户列表

#### 4. H2 数据库控制台

访问 http://localhost:8080/api/h2-console

```
JDBC URL: jdbc:h2:file:./data/zenday
Username: sa
Password: (留空)
```

---

### 前端部署

#### 1. 前置要求
- Node.js 16+
- npm 或 yarn
- HBuilderX (可选，推荐用于小程序开发)

#### 2. H5 网页版启动

```bash
# 进入前端目录
cd frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev:h5

# 访问 http://localhost:8081
```

#### 3. 微信小程序版启动

```bash
# 编译到微信小程序
npm run dev:mp-weixin

# 或使用 HBuilderX
# 打开 frontend 目录
# 运行 -> 运行到小程序模拟器 -> 微信开发者工具
```

#### 4. 打包生产版本

```bash
# H5 版本
npm run build:h5

# 微信小程序版本
npm run build:mp-weixin
```

---

## 生产环境部署

### 后端生产部署

#### 1. 使用 PostgreSQL 替换 H2

修改 `backend/src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/zenday
    driver-class-name: org.postgresql.Driver
    username: your_username
    password: your_password

  jpa:
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    hibernate:
      ddl-auto: update
```

添加 PostgreSQL 依赖到 `pom.xml`:

```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

#### 2. 配置文件优化

创建 `application-prod.yml`:

```yaml
spring:
  jpa:
    show-sql: false
    hibernate:
      ddl-auto: validate

server:
  port: 8080
  compression:
    enabled: true
    min-response-size: 1024

logging:
  level:
    root: WARN
    com.zenday: INFO
```

#### 3. 打包部署

```bash
# 打包
mvn clean package -DskipTests

# 运行生产版本
java -jar target/zenday-backend-1.0.0.jar --spring.profiles.active=prod
```

#### 4. 使用 Docker 部署

创建 `Dockerfile`:

```dockerfile
FROM openjdk:17-alpine
WORKDIR /app
COPY target/zenday-backend-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

构建和运行:

```bash
docker build -t zenday-backend .
docker run -p 8080:8080 zenday-backend
```

#### 5. 使用 Systemd 服务

创建 `/etc/systemd/system/zenday.service`:

```ini
[Unit]
Description=Zenday Backend Service
After=network.target

[Service]
Type=simple
User=zenday
WorkingDirectory=/opt/zenday
ExecStart=/usr/bin/java -jar /opt/zenday/zenday-backend-1.0.0.jar
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

启动服务:

```bash
sudo systemctl daemon-reload
sudo systemctl enable zenday
sudo systemctl start zenday
sudo systemctl status zenday
```

---

### 前端生产部署

#### 1. H5 版本部署

```bash
# 构建生产版本
npm run build:h5

# 构建产物在 dist/build/h5 目录
# 可以部署到任何静态服务器（Nginx、Apache、CDN等）
```

**Nginx 配置示例**:

```nginx
server {
    listen 80;
    server_name zenday.example.com;

    root /var/www/zenday/h5;
    index index.html;

    location / {
        try_files $uri $uri/ /index.html;
    }

    # API 代理
    location /api {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }

    # 开启 gzip 压缩
    gzip on;
    gzip_types text/plain text/css application/json application/javascript;
}
```

#### 2. 微信小程序版本部署

```bash
# 构建生产版本
npm run build:mp-weixin

# 使用微信开发者工具打开 dist/build/mp-weixin 目录
# 上传代码到微信公众平台
# 提交审核
```

#### 3. 修改 API 地址

修改 `frontend/utils/request.js`:

```javascript
// 开发环境
const BASE_URL = process.env.NODE_ENV === 'production'
  ? 'https://api.zenday.example.com/api'
  : 'http://localhost:8080/api'
```

---

## 云服务部署方案

### 阿里云部署

#### 1. ECS + RDS 方案

- **ECS**: 部署 Spring Boot 后端
- **RDS PostgreSQL**: 数据库
- **OSS**: 存储静态资源（前端）
- **CDN**: 加速访问

#### 2. 步骤

```bash
# 1. 购买 ECS 实例 (Ubuntu 20.04)
# 2. 安装 Java 17
sudo apt update
sudo apt install openjdk-17-jdk

# 3. 上传后端 jar 包
scp target/zenday-backend-1.0.0.jar user@your-ip:/opt/zenday/

# 4. 配置数据库连接 (使用 RDS)
# 5. 启动服务
# 6. 前端部署到 OSS
```

### 腾讯云部署

类似阿里云方案：
- **CVM**: 云服务器
- **TencentDB for PostgreSQL**: 数据库
- **COS**: 对象存储
- **CDN**: 内容分发网络

### AWS 部署

- **EC2**: 应用服务器
- **RDS PostgreSQL**: 数据库
- **S3**: 静态托管
- **CloudFront**: CDN

---

## Docker Compose 一键部署

创建 `docker-compose.yml`:

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:14-alpine
    environment:
      POSTGRES_DB: zenday
      POSTGRES_USER: zenday
      POSTGRES_PASSWORD: zenday123
    volumes:
      - postgres-data:/var/lib/postgresql/data
    ports:
      - "5432:5432"

  backend:
    build: ./backend
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/zenday
      SPRING_DATASOURCE_USERNAME: zenday
      SPRING_DATASOURCE_PASSWORD: zenday123
    depends_on:
      - postgres

  nginx:
    image: nginx:alpine
    ports:
      - "80:80"
    volumes:
      - ./frontend/dist/build/h5:/usr/share/nginx/html
      - ./nginx.conf:/etc/nginx/nginx.conf
    depends_on:
      - backend

volumes:
  postgres-data:
```

启动:

```bash
docker-compose up -d
```

---

## 性能优化建议

### 后端优化

1. **启用 Redis 缓存**
2. **配置连接池**
3. **启用 Gzip 压缩**
4. **数据库索引优化**
5. **分页查询**

### 前端优化

1. **图片懒加载**
2. **代码分割**
3. **CDN 加速**
4. **启用 PWA**
5. **压缩静态资源**

---

## 安全配置

### 后端安全

1. **HTTPS**: 使用 SSL/TLS 证书
2. **CORS**: 限制允许的来源
3. **JWT 认证**: 实现用户认证
4. **SQL 注入防护**: 使用 JPA
5. **XSS 防护**: 输入验证

### 前端安全

1. **环境变量**: 敏感信息使用环境变量
2. **Token 存储**: 使用 HttpOnly Cookie
3. **HTTPS Only**: 强制使用 HTTPS

---

## 监控与日志

### 日志配置

使用 Logback 配置日志:

```xml
<!-- logback-spring.xml -->
<configuration>
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>logs/zenday.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>logs/zenday.%d{yyyy-MM-dd}.log</fileNamePattern>
            <maxHistory>30</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <root level="INFO">
        <appender-ref ref="FILE" />
    </root>
</configuration>
```

### 监控工具

- **Spring Boot Actuator**: 健康检查
- **Prometheus + Grafana**: 性能监控
- **ELK Stack**: 日志分析

---

## 备份策略

### 数据库备份

```bash
# PostgreSQL 备份
pg_dump -U zenday -d zenday > backup_$(date +%Y%m%d).sql

# 定时备份 (crontab)
0 2 * * * pg_dump -U zenday -d zenday > /backup/zenday_$(date +\%Y\%m\%d).sql
```

### 应用备份

```bash
# 备份 H2 数据文件
cp -r data/ backup/data_$(date +%Y%m%d)/

# 备份配置文件
tar -czf config_backup.tar.gz backend/src/main/resources/
```

---

## 故障排查

### 常见问题

1. **端口被占用**
   ```bash
   # 查找占用端口的进程
   lsof -i :8080
   # 杀死进程
   kill -9 <PID>
   ```

2. **数据库连接失败**
   - 检查数据库服务是否启动
   - 检查连接字符串、用户名、密码
   - 检查防火墙规则

3. **前端 API 请求失败**
   - 检查 CORS 配置
   - 检查 API 地址配置
   - 检查网络连接

---

## 升级指南

### 后端升级

```bash
# 1. 备份数据
# 2. 停止服务
sudo systemctl stop zenday

# 3. 替换 jar 包
cp zenday-backend-1.1.0.jar /opt/zenday/

# 4. 启动服务
sudo systemctl start zenday

# 5. 验证升级
curl http://localhost:8080/api/users
```

### 前端升级

```bash
# 1. 构建新版本
npm run build:h5

# 2. 备份旧版本
mv /var/www/zenday/h5 /var/www/zenday/h5.backup

# 3. 部署新版本
cp -r dist/build/h5 /var/www/zenday/

# 4. 清理缓存 (如果使用 CDN)
```

---

**祝您部署顺利！** 🚀
