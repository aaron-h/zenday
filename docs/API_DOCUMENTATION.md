# API 接口文档

## 基础信息

- **Base URL**: `http://localhost:8080/api`
- **Content-Type**: `application/json`
- **响应格式**: 统一使用 `ApiResponse<T>` 包装

### 响应格式

```json
{
  "code": 200,
  "message": "success",
  "data": { ... }
}
```

---

## 用户管理 `/users`

### 获取所有用户
```
GET /users
```

**响应**:
```json
{
  "code": 200,
  "data": [
    {
      "id": 1,
      "username": "default",
      "nickname": "修行者",
      "avatar": null,
      "isAdmin": false,
      "totalMinutes": 120,
      "createdAt": "2024-01-01T00:00:00",
      "updatedAt": "2024-01-15T12:00:00"
    }
  ]
}
```

### 获取或创建默认用户
```
GET /users/default
```

### 创建用户
```
POST /users
Content-Type: application/json

{
  "username": "user1",
  "nickname": "修行者一",
  "avatar": "https://..."
}
```

---

## 功课管理 `/practices`

### 获取用户功课列表
```
GET /practices/user/{userId}?enabledOnly=true
```

**参数**:
- `userId`: 用户ID (path)
- `enabledOnly`: 是否只返回启用的功课 (query, 默认true)

**响应**:
```json
{
  "code": 200,
  "data": [
    {
      "id": 1,
      "name": "晨起梦观",
      "category": "观想类",
      "defaultDuration": 30,
      "defaultReminderTime": "07:00",
      "defaultSound": "qing",
      "defaultVolume": 50,
      "defaultBreathingRhythm": "medium",
      "icon": "🌅",
      "description": "清晨醒来后，回忆并观察梦境",
      "sortOrder": 1,
      "enabled": true
    }
  ]
}
```

### 创建功课
```
POST /practices
Content-Type: application/json

{
  "userId": 1,
  "name": "打坐",
  "category": "打坐类",
  "defaultDuration": 30,
  "defaultReminderTime": "19:00",
  "defaultSound": "qing",
  "defaultVolume": 50,
  "defaultBreathingRhythm": "slow",
  "icon": "🪷",
  "description": "静坐冥想，观照内心",
  "sortOrder": 1,
  "enabled": true
}
```

### 更新功课
```
PUT /practices/{id}
Content-Type: application/json

{
  "name": "打坐（更新）",
  "defaultDuration": 45,
  ...
}
```

### 删除功课
```
DELETE /practices/{id}
```

### 批量创建推荐功课
```
POST /practices/user/{userId}/recommended
```

**功能**: 一键创建 8 个预设的推荐功课

---

## 每日计划 `/plans`

### 获取每日计划
```
GET /plans/user/{userId}/date/{date}
```

**参数**:
- `date`: 日期，格式 `YYYY-MM-DD`

**响应**:
```json
{
  "code": 200,
  "data": {
    "id": 1,
    "planDate": "2024-01-15",
    "startTime": "07:00",
    "totalDuration": 90,
    "completedDuration": 30,
    "completedCount": 1,
    "totalCount": 3,
    "completionPercentage": 33.33,
    "items": [
      {
        "id": 1,
        "practiceId": 1,
        "practiceName": "晨起梦观",
        "practiceIcon": "🌅",
        "startTime": "07:00",
        "endTime": "07:30",
        "duration": 30,
        "sortOrder": 0,
        "completed": true,
        "completedAt": "2024-01-15T07:30:00"
      },
      {
        "id": 2,
        "practiceId": 2,
        "practiceName": "祈祷",
        "practiceIcon": "🙏",
        "startTime": "07:30",
        "endTime": "07:40",
        "duration": 10,
        "sortOrder": 1,
        "completed": false,
        "completedAt": null
      }
    ]
  }
}
```

### 保存每日计划
```
POST /plans
Content-Type: application/json

{
  "planDate": "2024-01-15",
  "startTime": "07:00",
  "items": [
    {
      "practiceId": 1,
      "duration": 30
    },
    {
      "practiceId": 2,
      "duration": 10
    }
  ]
}
```

### 批量生成计划
```
POST /plans/user/{userId}/batch?baseDate=2024-01-15&days=30
```

**参数**:
- `baseDate`: 基准日期
- `days`: 生成天数 (1-60)

**功能**: 基于基准日期的计划，复制生成未来 N 天的计划

### 标记计划项完成
```
PUT /plans/items/{itemId}/complete
```

---

## 修行会话 `/sessions`

### 创建修行会话
```
POST /sessions
Content-Type: application/json

{
  "userId": 1,
  "practiceId": 1,
  "startTime": "2024-01-15T07:00:00",
  "duration": 30,
  "sound": "qing",
  "volume": 50,
  "breathingRhythm": "medium",
  "planItemId": 1
}
```

**响应**:
```json
{
  "code": 200,
  "data": {
    "id": 1,
    "userId": 1,
    "practiceId": 1,
    "practiceName": "晨起梦观",
    "practiceCategory": "观想类",
    "practiceDate": "2024-01-15",
    "startTime": "2024-01-15T07:00:00",
    "endTime": null,
    "duration": 30,
    "actualDuration": null,
    "sound": "qing",
    "volume": 50,
    "breathingRhythm": "medium",
    "note": null,
    "planItemId": 1,
    "createdAt": "2024-01-15T07:00:00"
  }
}
```

### 完成修行会话
```
PUT /sessions/{id}/complete
Content-Type: application/json

{
  "endTime": "2024-01-15T07:30:00",
  "note": "今日状态很好"
}
```

### 获取用户会话历史
```
GET /sessions/user/{userId}
```

### 获取指定日期的会话
```
GET /sessions/user/{userId}/date/{date}
```

### 获取日期范围内的会话
```
GET /sessions/user/{userId}/range?startDate=2024-01-01&endDate=2024-01-31
```

---

## 统计分析 `/stats`

### 获取统计数据
```
GET /stats/user/{userId}?range=week
```

**参数**:
- `range`: 统计范围
  - `today`: 今日
  - `week`: 本周
  - `month`: 本月
  - `all`: 全部

**响应**:
```json
{
  "code": 200,
  "data": {
    "totalMinutes": 180,
    "totalHours": 3.0,
    "sessionCount": 6,
    "categoryStats": {
      "观想类": {
        "category": "观想类",
        "minutes": 60,
        "percentage": 33
      },
      "打坐类": {
        "category": "打坐类",
        "minutes": 60,
        "percentage": 33
      },
      "诵读类": {
        "category": "诵读类",
        "minutes": 60,
        "percentage": 33
      }
    },
    "recentSessions": [
      {
        "date": "2024-01-15",
        "practiceName": "晨起梦观",
        "duration": 30
      },
      {
        "date": "2024-01-15",
        "practiceName": "祈祷",
        "duration": 10
      }
    ]
  }
}
```

---

## 错误码说明

| Code | 说明 |
|------|------|
| 200  | 成功 |
| 400  | 请求参数错误 |
| 404  | 资源不存在 |
| 500  | 服务器内部错误 |

---

## 常见业务流程

### 1. 完整的修行流程

```
1. 获取功课列表
   GET /practices/user/1

2. 开始执行 (创建会话)
   POST /sessions
   {
     "userId": 1,
     "practiceId": 1,
     "startTime": "2024-01-15T07:00:00",
     "duration": 30,
     ...
   }

3. 完成修行 (完成会话)
   PUT /sessions/1/complete
   {
     "endTime": "2024-01-15T07:30:00",
     "note": "感觉很好"
   }

4. 查看统计
   GET /stats/user/1?range=today
```

### 2. 计划编排流程

```
1. 获取功课列表
   GET /practices/user/1

2. 创建每日计划
   POST /plans
   {
     "planDate": "2024-01-16",
     "startTime": "07:00",
     "items": [
       { "practiceId": 1, "duration": 30 },
       { "practiceId": 2, "duration": 20 }
     ]
   }

3. 批量生成未来计划
   POST /plans/user/1/batch?baseDate=2024-01-16&days=30

4. 查看某天计划
   GET /plans/user/1/date/2024-01-17
```

---

## 数据库配置

### H2 控制台访问
```
URL: http://localhost:8080/api/h2-console
JDBC URL: jdbc:h2:file:./data/zenday
Username: sa
Password: (留空)
```

### 数据文件位置
```
./data/zenday.mv.db
```

---

## 跨域配置

后端已配置 CORS，允许所有来源访问：
- 允许的方法: GET, POST, PUT, DELETE, OPTIONS
- 允许的头: *
- 允许凭证: true
