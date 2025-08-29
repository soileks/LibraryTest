# Тестовое задание

🚀 Технологии
Backend: Spring Boot 2.7.18, Spring Web, Spring Data JPA

Frontend: Thymeleaf, HTML5, CSS3

Database: PostgreSQL 14

Build Tool: Maven

Java: JDK 1.8

📋 Функциональность

📖 Управление книгами

Просмотр списка книг с пагинацией и поиском

Добавление новых книг (название, автор, ISBN)

Редактирование существующих книг

Удаление книг

👥 Управление клиентами

Просмотр списка клиентов с пагинацией

Добавление новых клиентов (ФИО, дата рождения)

Редактирование клиентов

Удаление клиентов

📝 Управление займами книг

Просмотр всех займов

Выдача книг клиентам

Отметка о возврате книг

Удаление записей о займах

🗄️ Структура базы данных

Приложение автоматически создает следующие таблицы:

books - книги (id, title, author, isbn)

clients - клиенты (id, full_name, birth_date)

loans - займы (id, client_id, book_id, loan_date, returned)

*Особенности: для ускорения работы с большими данными используются индексы


### Предварительные требования

Установите PostgreSQL 14

Установите JDK 1.8

Установите Maven

## Предварительные настройки

Клонируйте репозиторий

Перед запуском приложения небходимо создать базу данных (Например: library_db)

В файле application.properties необходимо указать свои username и password

<img width="746" height="165" alt="2025-08-29_17-27-28" src="https://github.com/user-attachments/assets/b78ccd13-bbe5-4533-9ce8-0ef3c227301a" />

### Сборка и запуск

Откройте консоль, перейдите в папку проекта, введите команду для сборки: 

mvn clean package

Для запуска:

java -jar target/library-1.0.0.jar

*приложение будет доступно на порту 8080 с контекстным путем /library (можно изменить в application.properties)

т.е. http://localhost:8080/library

# Endpoints

Rest интерфейс, который будет возвращает JSON со всеми читающими клиентами доступен по адресу http://localhost:8080/library/loans/active-readers

Можно проверить, например с помощью Postman

<img width="1370" height="711" alt="2025-08-29_17-43-25" src="https://github.com/user-attachments/assets/9a47099b-95d9-4ecf-88bf-11de4a3d24b6" />

<img width="1456" height="683" alt="2025-08-29_17-43-08" src="https://github.com/user-attachments/assets/170b0015-40c5-48a6-983a-855ea0c0934e" />

Интерфейс приложения:

<img width="1171" height="676" alt="2025-08-29_17-45-44" src="https://github.com/user-attachments/assets/ef94dde5-2de9-480f-a052-f51f93e4988b" />

<img width="1171" height="573" alt="2025-08-29_17-45-57" src="https://github.com/user-attachments/assets/5457300a-afd8-4e59-8c5e-20d9b27f4a9f" />




