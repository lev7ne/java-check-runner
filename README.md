# CheckRunner

---
>Консольное приложение, реализующее функционал формирования чека в магазине, принимает параметры в виде аргументов запуска и пишет результат в .csv


#### Перед запуском, необходимо подготовить .jar-архив:

```bash
./gradlew clean build
```
_*запуск осуществляется из директории ./build/libs:_


#### Пример консольной команды для запуска:


```bash
java -jar clevertec-check.jar 3-1 2-5 5-1 discountCard=1111 balanceDebitCard=100 saveToFile=./result.csv datasource.url=jdbc:postgresql://localhost:5432/check datasource.username=postgres datasource.password=postgres
```


#### Обязательные аргументы:

`id-quantity` — идентификатор товара и его количество, может быть несколько таких пар, разделенных пробелом, например, `1-3` (id=1, quantity=3)

`balanceDebitCard` — баланс на дебетовой карте, например, `balanceDebitCard=1000`

`datasource.url` — url для подключения к базе данных, например, `jdbc:postgresql://localhost:5432/check`

`datasource.username` — username для подключения к базе данных, например, `username`

`datasource.password` — password для подключения к базе данных, например, `password`

#### Необязательные параметры:

`saveToFile=` — путь, указывающий, куда необходимо сохранить результат, например, `./error_result.csv`

`discountCard=xxxx` — номер дисконтной карты, например, `discountCard=1111`

---
_**для тестирования DAO используется H2 (!)_

_***намеренно не подключаю другие библиотеки, в ТЗ только Java 21 и Gradle 8.5_