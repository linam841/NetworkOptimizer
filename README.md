# Network Optimization Lambda

Инструмент для автоматизации сетевых конфигураций с использованием Terraform и Java.

## Оглавление
1. [Требования](#требования)
2. [Установка](#установка)
3. [Запуск](#запуск)
4. [Структура проекта](#структура-проекта)
5. [Лицензия](#лицензия)

## Требования
- Terraform >= 1.5.0
- Java JDK 17
- Maven >= 3.8.0

## Установка
```bash
git clone https://github.com/linam841/NetworkOptimizer.git
cd NetworkOptimizer
```

## Запуск
1. Инициализация Terraform:
```bash
terraform init
```

2. Применение конфигурации:
```bash
terraform apply -var-file="terraform.tfvars"
```

3. Сборка Java-приложения:
```bash
mvn clean package
java -jar target/network-optimizer-1.0.jar
```

## Структура проекта
```
NetworkOptimizer/
├── src/                    # Java-исходники
│   └── main/
├── templates/              # Шаблоны конфигураций
├── terraform/              # Инфраструктура как код
│   ├── main.tf
│   ├── providers.tf
│   └── variables.tf
├── pom.xml                 # Конфигурация Maven
├── .gitignore
└── README.md
```

## Лицензия
[MIT License](LICENSE)
