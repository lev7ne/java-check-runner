# CheckRunner

---
>Консольное приложение, реализующее функционал формирования чека в магазине, принимает параметры в виде аргументов запуска и пишет результат в .csv


#### Перед запуском, необходимо скомпилировать:

```bash
javac -d src $(find . -name "*.java")
```

#### Пример консольных команд для запуска:

```bash
java -cp src ./src/main/java/ru/clevertec/check/CheckRunner.java 1-1 discountCard=1111 balanceDebitCard=12.1 saveToFile=./error_result.csv
```
```bash
java -cp src ./src/main/java/ru/clevertec/check/CheckRunner.java 1-2 3-4 5-6 discountCard=4444 balanceDebitCard=12967 pathToFile=src/main/resources/products.csv saveToFile=./check.csv
```
```bash
java -cp src ./src/main/java/ru/clevertec/check/CheckRunner.java 1-1 discountCard=1111 balanceDebitCard=100 pathToFile=./products.csv
```


#### Обязательные аргументы:

`id-quantity` — идентификатор товара и его количество, может быть несколько таких пар, разделенных пробелом, например, `1-3` (id=1, quantity=3)

`balanceDebitCard` — баланс на дебетовой карте, например, `balanceDebitCard=100.00`

`pathToFile` — путь к файлу с исходными данными о товарах, например, `./products.csv`

#### Необязательные параметры:

`saveToFile=` — путь указывающий, куда необходимо сохранить результат, например, `./error_result.csv`

`discountCard=xxxx` — номер дисконтной карты, например, `discountCard=1111`
