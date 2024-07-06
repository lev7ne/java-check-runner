# CheckRunner

---
>Консольное приложение, реализующее функционал формирования чека в магазине, принимает параметры в виде аргументов запуска и пишет результат в .csv


#### Перед запуском, необходимо скомпилировать:

```bash
javac -d src $(find . -name "*.java")
```

#### Пример консольной команды для запуска:

```bash
java -cp src ./src/main/java/ru/clevertec/check/CheckRunner.java 3-1 2-5 5-1 discountCard=1111 balanceDebitCard=100
```


#### Обязательные аргументы:

`id-quantity` — идентификатор товара и его количество, может быть несколько таких пар, разделенных пробелом, например, "1-3" `(id=1, quantity=3)`

`balanceDebitCard=xxxx` — баланс на дебетовой карте, например, `balanceDebitCard=100.00`

#### Необязательные параметры:

`discountCard=xxxx` — номер дисконтной карты, например, `discountCard=1111`

