# Parking Service

Сервис для управления парковкой транспортных средств.

## Возможности

- Регистрация въезда/выезда транспортных средств
- Поддержка разных типов транспорта (легковые, грузовые, автобусы, мотоциклы)
- Ограничение количества парковочных мест
- Генерация отчетов по парковке

## API Endpoints

### Регистрация въезда
```http
POST /api/parking/entry
Content-Type: application/json

{
    "number": "A123BC",
    "type": "PASSENGER"
}
```

### Регистрация выезда
```http
POST /api/parking/exit
Content-Type: application/json

{
    "number": "A123BC"
}
```

### Получение отчета
```http
GET /api/parking/report?startDate=2024-03-08T10:00:00&endDate=2024-03-08T18:00:00
```

## Типы транспортных средств

- `PASSENGER` - легковой автомобиль
- `TRUCK` - грузовой автомобиль
- `BUS` - автобус
- `MOTORCYCLE` - мотоцикл

## Конфигурация

Основные настройки в `application.properties`:

```properties
# Количество парковочных мест
parking.total-spots=3

# Настройки базы данных
quarkus.datasource.db-kind=postgresql
quarkus.datasource.username=parking_user
quarkus.datasource.password=parking_pass
quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/parking_db
```

### Локальный запуск
1. Запустить PostgreSQL
2. Создать базу данных `parking`
3. Запустить приложение:
```bash
./mvnw quarkus:dev
```

### Docker
```bash
docker-compose up -d
```

## Тестирование

Запуск тестов:
```bash
./mvnw test
```

Тесты используют H2 in-memory базу данных и не требуют PostgreSQL.

## Обработка ошибок

Сервис возвращает следующие HTTP статусы:
- `200 OK` - успешное выполнение
- `400 Bad Request` - некорректные входные данные
- `404 Not Found` - транспортное средство не найдено
- `409 Conflict` - попытка повторной парковки/выезда
- `500 Internal Server Error` - внутренняя ошибка сервера
