# CheckRunner

---
> Веб-приложение, реализующее функционал формирования чека в магазине, принимает параметры в Json-объекта, возвращает
> .csv

#### Перед запуском, необходимо подготовить .war-архив:

```bash
./gradlew build
```

#### Примеры запросов:

Получить чек: POST http://localhost:8080/check

```json
{
  "products": [
    {
      "id": 1,
      "quantity": 5
    },
    {
      "id": 1,
      "quantity": 5
    }
  ],
  "discountCard": 1234,
  "balanceDebitCard": 100
}
```

Вернуть товар из БД: GET http://localhost:8080/products?id=1

```
```

Добавить товар в БД: POST http://localhost:8080/products

```json
{
  "description": "Eat 100g.",
  "price": 3.25,
  "quantity": 5,
  "isWholesale": true
}
```

Обновить товар в БД: PUT http://localhost:8080/products?id=1

```json
{
  "description": "Chocolate Ritter sport 100g.",
  "price": 3.25,
  "quantity": 5,
  "isWholesale ": true
}
```

Удалить товар из БД: DELETE http://localhost:8080/products?id=1

```
```

Вернуть дисконтную карту из БД: GET http://localhost:8080/discountcards?id=1

```
```

Добавить дисконтную карту в БД: POST http://localhost:8080/discountcards

```json
{
  "discountCard": 5265,
  "discountAmount": 2
}
```

Обновить дисконтную карту в БД: PUT http://localhost:8080/discountcards?id=1

```json
{
  "discountCard": 6786,
  "discountAmount": 3
}
```

Удалить дисконтную карту из БД: DELETE http://localhost:8080/discountcards?id=1

```
```

---
_**для тестирования DAO используется H2 (!)_
