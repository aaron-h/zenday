# 禅修功课管理系统 (Zenday)

![Logo](./assets/logo.svg)

一个面向个人与管理端的禅修功课管理系统，支持功课创建、每日计划编排、执行计时与提醒、历史统计、推荐等功能。

## 📖 项目简介

禅修功课管理系统是一个全栈应用，旨在帮助修行者更好地管理和追踪自己的禅修功课。系统采用微拟物化视觉风格，提供简洁友好的用户体验。

### 主要功能

- ✅ **功课项目管理**：创建、编辑、删除功课，支持分类和自定义参数
- 📅 **每日计划编排**：可视化编排每日功课计划，支持批量生成
- ⏱ **执行计时**：带呼吸球动画的计时器，支持禅音、节奏调节
- 🔔 **提醒通知**：按计划时间触发浏览器通知
- 📊 **历史统计**：多维度统计修行数据，可视化展示
- 💡 **推荐功课**：一键添加预设的推荐功课

## 🎨 设计特色

### 禅意Logo
- 简约线条设计
- 融合禅圆（Enso）与莲花元素
- 宁静的蓝绿渐变色调

### 视觉规范
- **色彩**：禅意青 (#4A90E2) + 禅意绿 (#67B26F)
- **字体**：标题用衬线体，正文用无衬线体
- **动画**：柔和的缓动效果，呼吸球同步动画
- **微拟物化**：适度阴影和渐变，增加质感

详见 [设计规范文档](./docs/DESIGN_SPEC.md)

## 🏗 技术栈

### 后端
- **Java 17** + **Spring Boot 3**
- **Spring Data JPA** - 数据持久化
- **H2 Database** - 内存数据库（可迁移至 PostgreSQL）
- **Lombok** - 简化代码
- **Maven** - 依赖管理

### 前端
- **uniapp** - 跨平台框架
- **Vue 3** - 渐进式框架
- **自定义组件** - 呼吸球、功课卡片等

## 📂 项目结构

```
zenday/
├── backend/                 # 后端代码
│   ├── src/
│   │   └── main/
│   │       ├── java/com/zenday/
│   │       │   ├── entity/           # 实体类
│   │       │   ├── repository/       # 数据访问层
│   │       │   ├── service/          # 业务逻辑层
│   │       │   ├── controller/       # 控制器层
│   │       │   ├── dto/              # 数据传输对象
│   │       │   └── config/           # 配置类
│   │       └── resources/
│   │           └── application.yml   # 应用配置
│   └── pom.xml                       # Maven配置
│
├── frontend/                # 前端代码
│   ├── pages/               # 页面
│   │   ├── practice/        # 功课管理
│   │   ├── plan/            # 每日计划
│   │   ├── execute/         # 执行页
│   │   ├── stats/           # 统计页
│   │   └── profile/         # 个人中心
│   ├── components/          # 组件
│   │   ├── breathing-ball/  # 呼吸球
│   │   └── practice-card/   # 功课卡片
│   ├── api/                 # API封装
│   ├── utils/               # 工具函数
│   ├── pages.json           # 页面配置
│   └── manifest.json        # 应用配置
│
├── docs/                    # 文档
│   └── DESIGN_SPEC.md       # 设计规范
│
├── assets/                  # 资源文件
│   └── logo.svg             # Logo
│
└── README.md                # 本文件
```

## 🚀 快速开始

### 前置要求

- **Java 17+**
- **Maven 3.6+**
- **Node.js 16+**
- **HBuilderX** (推荐) 或其他支持 uniapp 的开发工具

### 后端启动

```bash
# 进入后端目录
cd backend

# 安装依赖并启动
mvn spring-boot:run
```

后端服务将在 `http://localhost:8080` 启动

访问 H2 控制台：`http://localhost:8080/api/h2-console`

### 前端启动

```bash
# 进入前端目录
cd frontend

# 安装依赖
npm install

# 启动开发服务器 (H5)
npm run dev:h5

# 或编译到微信小程序
npm run dev:mp-weixin
```

前端服务将在 `http://localhost:8081` 启动

## 📱 功能使用指南

### 1. 功课管理

- **添加功课**：点击右下角 "➕" 按钮
- **添加推荐功课**：点击 "💡 推荐" 按钮，一键添加预设功课
- **编辑功课**：长按功课卡片
- **开始执行**：点击功课卡片的 "开始" 按钮

### 2. 每日计划

- **创建计划**：选择日期 → 点击 "编辑" → 勾选功课并设置时长
- **批量生成**：基于当天计划，一键生成未来 1-60 天的计划
- **执行计划**：点击 "🚀 开始今日功课"，自动按序执行
- **标记完成**：点击计划项前的复选框

### 3. 执行计时

- **呼吸引导**：跟随呼吸球动画进行呼吸练习
- **调整参数**：点击 "⚙ 设置" 调整时长、禅音、节奏
- **保存偏好**：在设置中点击 "保存为默认"

### 4. 统计查看

- **切换范围**：今日 / 本周 / 本月 / 全部
- **查看分布**：按功课分类查看时长分布
- **历史记录**：查看最近的修行会话

## 🗄 数据模型

### 核心实体

- **User** - 用户
  - id, username, nickname, avatar
  - totalMinutes (累计时长)

- **Practice** - 功课项目
  - name, category, icon
  - defaultDuration, defaultSound, defaultBreathingRhythm

- **DailyPlan** - 每日计划
  - planDate, startTime
  - totalCount, completedCount, completionPercentage

- **PlanItem** - 计划项
  - practiceId, startTime, endTime, duration
  - completed, completedAt

- **PracticeSession** - 修行会话
  - practiceId, startTime, endTime, duration
  - sound, breathingRhythm, note

## 🔌 API 接口

### 功课相关
- `GET /api/practices/user/{userId}` - 获取用户功课列表
- `POST /api/practices` - 创建功课
- `PUT /api/practices/{id}` - 更新功课
- `DELETE /api/practices/{id}` - 删除功课
- `POST /api/practices/user/{userId}/recommended` - 添加推荐功课

### 计划相关
- `GET /api/plans/user/{userId}/date/{date}` - 获取每日计划
- `POST /api/plans` - 保存每日计划
- `POST /api/plans/user/{userId}/batch` - 批量生成计划
- `PUT /api/plans/items/{itemId}/complete` - 标记计划项完成

### 会话相关
- `POST /api/sessions` - 创建会话
- `PUT /api/sessions/{id}/complete` - 完成会话
- `GET /api/sessions/user/{userId}` - 获取会话历史

### 统计相关
- `GET /api/stats/user/{userId}?range={range}` - 获取统计数据

## 🎯 优化建议

### 已实现
- ✅ 禅意Logo设计
- ✅ UI/UX设计规范
- ✅ 完整的后端API
- ✅ 功课管理页面
- ✅ 每日计划编排
- ✅ 执行计时与呼吸球动画
- ✅ 历史统计可视化
- ✅ 推荐功课功能

### 待优化
- ⏳ 用户认证与多用户支持
- ⏳ 提醒通知（浏览器通知 / 推送）
- ⏳ 音频播放（禅音背景音乐）
- ⏳ 数据导出/导入
- ⏳ 深色模式
- ⏳ 多语言支持
- ⏳ PWA 离线支持
- ⏳ 成就系统与打卡分享

## 📄 许可证

MIT License

## 🙏 致谢

感谢所有为禅修事业做出贡献的人们。

愿一切众生离苦得乐，智慧圆满。🌸

---

**版本**: v1.0.0
**更新时间**: 2024-01-15
**作者**: Zenday Team
