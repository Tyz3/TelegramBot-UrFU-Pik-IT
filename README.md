# TelegramBot-UrFU-Pik-IT

Демо Telegram: @UrFUDailyProblembot

Работает небыстро, ввиду запуска бота на Raspberry PI 4B

**Аккаунты**

Эксперт supovar@imperium.terra:P@ssw0rd

Модератор parmash99@mail.ru:Reer5%C

Супер-пользователь tyz_3@mail.ru:Proxu1!

**Фичи**
* Whitelist расширений загружаемых файлов
* Проверка размера загружаемого файла
* Наличие ролей с разграничением прав доступа
* Ограничение по размеру Заголовка и Описания заявки
* Таймер сброса сессии (по умолчанию 70 минут)
* Кол-во заявок на страницах отображения заявок
* Регулярные выражения на вводимые данные авторизации
* Привязка бота и права доступа настраивается через конфиг
* Изменение отображаемых названий ролей и статусов заявок
* Изменение диалоговых сообщений через конфиг
* Настройка отображаемого формата времени

**TODO**
- Вместо удалении своей заявки и создании новой добавить сценарий редактирования заявки, аналогично сценарию для модератора
- Блокировка пользователей
- Менять статус заявки при достижении лимита голосов, установить пороги За и Против 
- Устранить дилетанский подход в работе с Hibernate или вовсе отказаться от него в пользу нативных запросов и кастомного кэширования
- Обработчик команд для "горячего" администирования системы (cmds: /reload-configs, ...)
- (Linux) Unit для Systemd на автозапуск
- Рефакторинг кода для работы с БД
- Поддержка голосовых сообщений для приложения
- Поддержка видео для приложения
- Добавить вкладку заявок, которые уже набрали установленный лимит голосов
- Подключение Web-сервиса, который бы отвечал на запросы бота и работал напрямую с БД
- Разбиение кода на часть frontend-а (TelegramBot) и часть backend-а (Web-сервис)

# Фильтры на вкладках

**Голосование**
* Статусы голосования: Голосование (студенческий состав) и Голосование (экспертный состав) - зависит от прав роли
* Голосующий не проголосовал за заявку

**Свои заявки**
* Владелец = обозреватель заявок

**Все заявки**
* Не фильтрации

**Управление заявками**
* Заявки, которые не имеют редактора (editor_id)

**Редактируемы заявки**
* Обозреватель является редактором этой заявки

![Схема Пик IT](https://user-images.githubusercontent.com/21179689/114271128-46f91e00-9a29-11eb-81b8-f74e75303a87.png)
