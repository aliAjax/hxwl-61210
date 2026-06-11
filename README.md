# hxwl-61210

Java Spring Boot后端：小型展览馆展品巡检记录。

## Port

61210

## Run

```bash
mvn spring-boot:run
```

## API

- `GET /health`
- `POST /exhibits`
- `GET /exhibits`
- `POST /exhibits/{id}/inspections`
- `GET /exhibits/{id}/inspections`
- `GET /inspections/abnormal/latest`
