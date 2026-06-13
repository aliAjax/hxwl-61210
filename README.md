# hxwl-61210

Java Spring Boot后端：小型展览馆展品巡检记录。

## Port

61210

## Run

```bash
mvn spring-boot:run
```

## 技术栈

- Spring Boot 3.3.x
- H2 Database (嵌入式文件数据库)
- JdbcTemplate
- Java 17

## API

### 健康检查
- `GET /health`

### 展区管理
- `POST /zones` - 创建展区
  - 请求体: `{ "name": "展区名称", "description": "展区描述" }`
  - 响应: `{ "id": 1, "name": "展区名称", "description": "...", "exhibitCount": 0, "abnormalExhibitCount": 0 }`

- `GET /zones` - 查询展区列表（含展品数量和异常数量统计）
  - 响应: `[{ "id": 1, "name": "展区名称", "description": "...", "exhibitCount": 5, "abnormalExhibitCount": 1 }]`

- `GET /zones/{id}` - 查询单个展区详情
  - 响应: `{ "id": 1, "name": "展区名称", "description": "...", "exhibitCount": 5, "abnormalExhibitCount": 1 }`

### 展品管理
- `POST /exhibits` - 创建展品（自动校验展区是否存在）
  - 请求体: `{ "code": "EX001", "name": "展品名称", "zone": "展区名称", "owner": "负责人" }`

- `GET /exhibits` - 查询展品列表

### 巡检管理
- `POST /exhibits/{id}/inspections` - 提交巡检记录
  - 请求体: `{ "environmentNote": "...", "appearanceStatus": "good", "actionNote": "...", "abnormal": false }`

- `GET /exhibits/{id}/inspections` - 查询展品的巡检历史

- `GET /inspections/abnormal/latest` - 查询最新的异常巡检记录
