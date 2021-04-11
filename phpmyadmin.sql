-- phpMyAdmin SQL Dump
-- version 4.9.0.1
-- https://www.phpmyadmin.net/
--
-- Хост: localhost
-- Время создания: Апр 11 2021 г., 18:36
-- Версия сервера: 10.3.27-MariaDB-0+deb10u1
-- Версия PHP: 7.3.27-1~deb10u1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- База данных: `phpmyadmin`
--

-- --------------------------------------------------------

--
-- Структура таблицы `bans`
--

CREATE TABLE `bans` (
  `id` int(11) NOT NULL,
  `punisher_id` int(11) NOT NULL,
  `intruder_id` int(11) NOT NULL,
  `reason` varchar(256) NOT NULL DEFAULT 'Не указана',
  `time` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Структура таблицы `tasks`
--

CREATE TABLE `tasks` (
  `id` int(11) NOT NULL,
  `status` int(11) DEFAULT 0 COMMENT '0: Новая\n1: Модерация\n2: Голосование (студенческий состав)\n3: Голосование (экспертный состав)\n4: Осуществление работ\n5: Выполнена\n6: Неактивна',
  `description` text DEFAULT NULL,
  `cost` varchar(64) DEFAULT 'Неизвестно',
  `owner_id` int(11) DEFAULT -1,
  `title` varchar(128) DEFAULT 'Неуказано',
  `address` varchar(128) DEFAULT 'Неуказано',
  `files` mediumtext DEFAULT NULL,
  `editor_id` int(11) DEFAULT -1,
  `create_at` datetime DEFAULT current_timestamp(),
  `done_at` datetime DEFAULT NULL,
  `vote_count_pros` int(11) DEFAULT 0,
  `vote_count_cons` int(11) DEFAULT 0,
  `vote_count_limit` int(11) DEFAULT 500
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `tasks`
--

INSERT INTO `tasks` (`id`, `status`, `description`, `cost`, `owner_id`, `title`, `address`, `files`, `editor_id`, `create_at`, `done_at`, `vote_count_pros`, `vote_count_cons`, `vote_count_limit`) VALUES
(14, 2, 'Хлам в компьютерном классе на 4 этаже', '1.000.000$', 19, 'В компьютерном классе', 'У быка на рогах,39', 'img:jpg:AgACAgIAAxkBAAIP0GBwjagzSdA7TaQ_-aph6PQlFS7tAALEsDEb3O-IS3jInyMgF3upXW0AAZ8uAAMBAAMCAAN5AAMIyQIAAR4E,img:jpg:AgACAgIAAxkBAAIP0WBwjaj_AAHWWGYTS-WVGTXOIHvXtQACxbAxG9zviEt_hEjvUjWPAkyXgJ8uAAMBAAMCAAN5AAO3zAIAAR4E,', -1, '2021-04-09 22:22:18', NULL, 3, 1, 500),
(18, 2, 'Равным образом консультация с широким активом требуют определения и уточнения модели развития\\. Товарищи! постоянное информационно\\-пропагандистское обеспечение нашей деятельности позволяет выполнять важные задания по разработке модели развития\\. Разнообразный и богатый опыт консультация с широким активом обеспечивает широкому кругу\\.', '1000Р', 18, 'Равным образом к...', 'ул Пушников, д 20, под 4', 'img:jpg:AgACAgIAAxkBAAISImBwrHM1ROrlm90jea-84e8Wfpd9AAJztTEb7fyBS7_VwsXLNVBfoziGoi4AAwEAAwIAA3kAA1XqAAIeBA,', -1, '2021-04-10 00:34:45', NULL, 3, 1, 500),
(19, 2, 'Аааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааапчххиииииииииииииииииииииии', '1 сопля в одногруппника', 19, 'Чих', 'Афганистан', 'img:jpg:AgACAgIAAxkBAAISNWBwrNeSHSYYU5Q3jH8tvqlHb7wtAAIIsTEb3O-IS7wzzuHdSo0pDVk3ny4AAwEAAwIAA3gAA6WaAgABHgQ,', -1, '2021-04-10 00:35:38', NULL, 3, 0, 3),
(20, 5, 'DreamTeam', 'R7393vv', 19, 'Paidet', 'Страна сосисок', 'img:jpg:AgACAgIAAxkBAAITUWBwsHW-OK0yeNz3F-amjwu_L6hbAAIQsTEb3O-IS_oZdGrE5As5C-gymy4AAwEAAwIAA3kAA1QRBgABHgQ,', -1, '2021-04-10 00:52:02', NULL, 0, 0, 3),
(21, 0, 'Необходимо построить столицу Ардерона. Строение большое, 200х300 блоков. Требуется 3 билдера и 1 терраформер.', '5000Р', 18, 'Столица Эрзурум', 'Неуказано', 'img:jpg:AgACAgIAAxkBAAITzmBxr9DqS5F3i1HIoAyISkaFXiWlAAJfsTEb7fyJS-0Npr5IeSLLKcuKoi4AAwEAAwIAA20AA3v2AAIeBA,doc:image/png:2018-02-27_10.07.52.png:BQACAgIAAxkBAAIT0GBxr-H3kSvG0mFWE7dNIHo-791LAAInDAAC7fyJS9gFxK_7zSwFHgQ,doc:image/jpeg:2018-02-27_10.08.18.jpg:BQACAgIAAxkBAAIT0mBxsCTe6zXsob88AywM47ihvCseAAIoDAAC7fyJSzh852gKTMnMHgQ,doc:image/bmp:2018-02-27_10.08.37.bmp:BQACAgIAAxkBAAIT1GBxsFYvRIXUghMxndlk6rnqAQ1vAAIpDAAC7fyJSz9mqS0VRBAoHgQ,doc:application/vnd.openxmlformats-officedocument.presentationml.presentation:Startup.pptx:BQACAgIAAxkBAAIT1mBxsLYEQdgIoCN4PNHhbJxvvfI9AAIqDAAC7fyJS-_vJO4yV3tpHgQ,doc:application/vnd.openxmlformats-officedocument.spreadsheetml.sheet:Расчёт атрибутов экипировки.xlsx:BQACAgIAAxkBAAIT2GBxsNYjszdABhOlgbtg0QoTe9JFAAIrDAAC7fyJSzNFHGdVdd0bHgQ,doc:application/vnd.openxmlformats-officedocument.wordprocessingml.document:Описания зачарований.docx:BQACAgIAAxkBAAIT2mBxsPTLvTnpeLTT0PkxQAgwPZS3AAIsDAAC7fyJS3qQq9qqSkDNHgQ,doc:application/msword:Описания зачарований.doc:BQACAgIAAxkBAAIT3GBxsSCAfaCmnc9EFJp2_VoeorpIAAIuDAAC7fyJS1GquEuCq069HgQ,doc:text/plain:eula.txt:BQACAgIAAxkBAAIT3mBxsTYRD0HrYHbFYpZgHuhiDN4ZAAIwDAAC7fyJSzHHnvPU4X-4HgQ,doc:application/x-yaml:config.yml:BQACAgIAAxkBAAIT4GBxsUZBcNDjFOHRsJpUlUF82JF8AAIxDAAC7fyJSxo1m1du6w0uHgQ,doc:application/json:settings.json:BQACAgIAAxkBAAIT4mBxsVSBSfdDwKVxovjvYPVuTGMXAAIyDAAC7fyJS7qBmq39SMaSHgQ,doc:application/zip:РИ-471220_Ульянихин_КМЗИ_ПР-5.zip:BQACAgIAAxkBAAIT5GBxsXUddyCKb4bXKxVycr9hHMgoAAIzDAAC7fyJS461SnjNtCb2HgQ,doc:application/pdf:Брюс_Шнайер_Прикладная_криптография_2016.pdf:BQACAgIAAxkBAAIT5mBxsZB_HImOPdX53-liulGyoet6AAI0DAAC7fyJS-zrFcx9SUVcHgQ,doc:application/rtf:Zadanie_1_2021.rtf:BQACAgIAAxkBAAIT6WBxspxGJzmlY2y82FXhgZHNQ-AVAAI5DAAC7fyJSxNwIYoHsDulHgQ,', -1, '2021-04-10 19:01:19', NULL, 0, 0, 500),
(22, 0, 'Повседневная практика показывает, что укрепление и развитие структуры обеспечивает широкому кругу (специалистов) участие в формировании дальнейших направлений развития.', 'Неуказано', 18, 'Повседневная пра...', 'ул Ленина, д 23', 'img:jpg:AgACAgIAAxkBAAIUEmBxttUydx87WIXCo2bfwHSdaEsgAAJ4sTEb7fyJS6KcRoF9vAipp39rly4AAwEAAwIAA20AA3uqBwABHgQ,img:jpg:AgACAgIAAxkBAAIUFGBxtuStb6Zl3DDoGAABG1Q66GdSgQACebExG-38iUt5BAIcf05cN8VmiZ4uAAMBAAMCAANtAAPEBwUAAR4E,doc:application/vnd.openxmlformats-officedocument.wordprocessingml.document:Формат генерации предмета.docx:BQACAgIAAxkBAAIUGWBxt5ECk6gO0uunU44oszcL7ORbAAJFDAAC7fyJS-PIH887TcoLHgQ,', -1, '2021-04-10 19:30:56', NULL, 0, 0, 500),
(23, 0, 'Среди студентов есть инициативные люди, которые замечают проблемы внутри университета. Они хотят предложить идеи для решения этих проблем, но не всегда знают, куда можно обратиться. В университете также есть запрос на инициативы студентов для улучшения условий обучения и пребывания в нем.', '25.000-50.000Р', 18, 'Инициативные люди', 'Онлайн', 'img:jpg:AgACAgIAAxkBAAIUdWBx0hrMgTfHrALy8_EUPoTV3OViAALCsTEb7fyJS6w-4hPs8M1FrLMHni4AAwEAAwIAA20AA2HfBAABHgQ,', -1, '2021-04-10 21:27:20', NULL, 0, 0, 500),
(24, 3, 'НАВАРИЛИ ВЕЛИКИЙ СУП, А ОН ХОЛОДНЫЙ!!!', 'Бюджет РФ за 5 лет', 20, 'Трындец какой-та', 'Цитадель Ультрамаринов, планета Ультрамар, Сегментам Ультима, Империум Человечества и ООО СУПОВАРОВ', 'img:jpg:AgACAgIAAxkBAAIU_WBx6_ePG450ifvZOBGvRnMWQA_nAAKetTEb8AABkEtZLFhLhtx44CdJT6IuAAMBAAMCAANtAAMT-wACHgQ,', -1, '2021-04-10 23:17:03', NULL, 0, 0, 3),
(25, 0, 'Леньленьленьленьлень', 'Лень', 19, 'Лень', 'Дома', '', -1, '2021-04-10 23:19:37', NULL, 0, 0, 500),
(26, 0, 'ХАЧУ ПАСТУКАТЬ ЮДИШЕК, А АНИ НИ СТУКАЮЦА', 'МНОГА ЖУБОВ', 20, 'ПАСТУК', 'КАКОЙ ТЕБЕ АДРЕС, ЗОГ? Я ТИБЕ ЩА ЖУБЫ ВСЕ ВЫБЬЮ, ЕСЛИ НЕ ПЕРИСТАНЕШЬ ЧУДИТЬ!', 'img:jpg:AgACAgIAAxkBAAIVXWBx7Xx-ZEIDZL2XaUNWvKjHx0BxAAKgtTEb8AABkEswoNYCQG_0lRXyAZ8uAAMBAAMCAANtAAP97wIAAR4E,', -1, '2021-04-10 23:21:52', NULL, 0, 0, 500),
(27, 0, 'Нит инвентаря, потому что не играю в кмпры', 'Одно бренное тело с мозгами', 21, 'Праблема', 'СиБирь', 'img:jpg:AgACAgIAAxkBAAIVrWBx71L8WzWKSiZIon1QU4lXSY2LAAL4szEb3O-QS_VMNPzJpSgmOlcRmy4AAwEAAwIAA20AA5ziBAABHgQ,', -1, '2021-04-10 23:31:56', NULL, 0, 0, 500),
(28, 3, 'В моём аниме про накаченных мужиков, занимающихся чистой мужской борьбой, появилась женщина.', '2 банки пива и протухший бутер', 20, 'УБЕРИТЕ ЖЕНЩИН ИЗ АНИМЕ!', 'Неуказано', 'img:jpg:AgACAgIAAxkBAAIVxmBx75wY-gxc5TsYSv7JiVx_y7n2AAKltTEb8AABkEueGfPau8vXo0yinqIuAAMBAAMCAANtAAMh4gACHgQ,', -1, '2021-04-10 23:28:21', NULL, 0, 0, 20),
(29, 0, '- Пожалуйста. Хватит. Я больше не вынесу...\n- Что с тобой произошло?\n- Я давно мертва, незнакомец. И все это время я страдаю из-за своего мужа, Нериен\'эта. Даэдра вцепились в его душу своими мерзкими когтями и извратили его. Обернули против меня.\nВсе, что ты здесь видишь... результат этого.\n- Я могу чем-нибудь помочь?\n- Мой муж - монстр, которым он стал, проклял меня. Он разделил мою душу на три части и запечатал их в филактериях.\nЕсли филактерии уничтожить, то я обрету покой.', '2000 Крон', 18, '- Пожалуйста. Хв...', 'Крипта Сердец', 'img:jpg:AgACAgIAAxkBAAIWH2Bx8MF7DhDKAAF9rZibtc9Xnft2FAACGrIxG-38iUugXLvgu6T0hDnuzaIuAAMBAAMCAANtAAP67AACHgQ,', -1, '2021-04-10 23:38:14', NULL, 0, 0, 500),
(30, 1, 'Мне нужен один настоящий маскулиный гетеросексуал, для походов в ♂️GYM♂️, сеансов ♂️FISTING♂️, обсуждения смыслов Евангелиона и ожидания 6 сезона жожи.', '♂️300 BUCKS♂️', 22, 'Нужен ♂️BOSS OF THIS GYM♂️', '♂️TWO BLOCKS DOWN♂️', 'img:jpg:AgACAgIAAxkBAAIYCWBx9btYcEvBFA3Ofpg6yYxhJG93AALFsTEbPdiJSzo9I1JIHIzP-xF_ny4AAwEAAwIAA20AA7LeAgABHgQ,', -1, '2021-04-10 23:57:27', NULL, 0, 0, 100);

-- --------------------------------------------------------

--
-- Структура таблицы `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `name` varchar(128) NOT NULL,
  `student_id` varchar(8) NOT NULL DEFAULT '12345678',
  `role` int(11) DEFAULT 0 COMMENT '0: Пользователь\n1: Модератор\n2: Эксперт\n3: Супер-пользователь',
  `email` varchar(1024) NOT NULL,
  `password` varchar(32) NOT NULL,
  `tg_id` bigint(20) NOT NULL,
  `create_at` datetime DEFAULT current_timestamp(),
  `last_seen` datetime DEFAULT current_timestamp(),
  `ban` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Пользователи';

--
-- Дамп данных таблицы `users`
--

INSERT INTO `users` (`id`, `name`, `student_id`, `role`, `email`, `password`, `tg_id`, `create_at`, `last_seen`, `ban`) VALUES
(18, 'Ульянихин Евгений Ильич', '09701334', 3, 'tyz_3@mail.ru', 'Proxu1!', 312859877, '2021-04-09 21:47:08', '2021-04-11 14:41:11', 0),
(19, 'Парфилова Мария Александровна', '57843275', 2, 'parmash99@mail.ru', 'Reer5%C', 822348598, '2021-04-09 22:20:21', '2021-04-10 20:55:41', 0),
(20, 'Жиллиман Робаут Императорович', '12341488', 1, 'supovar@imperium.terra', 'P@ssw0rd', 457461704, '2021-04-10 23:16:18', '2021-04-10 20:54:23', 0),
(21, 'Орк Любовь Иванна', '12345678', 1, 'nyashk@ya.ru', 'Yep#34dear', 822348598, '2021-04-10 23:30:30', '2021-04-10 23:41:36', 0),
(22, 'Жмышенко Сергей Георгиевич', '88005553', 1, 'zevran152@gmail.com', 'P@ssw0rd', 809140900, '2021-04-10 23:52:50', '2021-04-11 00:11:09', 0);

-- --------------------------------------------------------

--
-- Структура таблицы `votes`
--

CREATE TABLE `votes` (
  `id` int(11) NOT NULL,
  `task_id` int(11) NOT NULL,
  `voteer_id` int(11) NOT NULL,
  `approved` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Учёт голосов заявок';

--
-- Дамп данных таблицы `votes`
--

INSERT INTO `votes` (`id`, `task_id`, `voteer_id`, `approved`) VALUES
(20, 18, 19, 1),
(21, 14, 18, 0),
(22, 19, 18, 1),
(23, 14, 21, 1),
(24, 18, 21, 1),
(25, 19, 21, 1),
(26, 14, 20, 1),
(27, 18, 20, 0),
(28, 19, 20, 1),
(29, 14, 22, 1),
(30, 18, 22, 1);

--
-- Индексы сохранённых таблиц
--

--
-- Индексы таблицы `bans`
--
ALTER TABLE `bans`
  ADD PRIMARY KEY (`id`),
  ADD KEY `bans_users_intruder_id_fk` (`intruder_id`),
  ADD KEY `bans_users_punisher_id_fk` (`punisher_id`);

--
-- Индексы таблицы `tasks`
--
ALTER TABLE `tasks`
  ADD PRIMARY KEY (`id`);

--
-- Индексы таблицы `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `users_email_uindex` (`email`),
  ADD UNIQUE KEY `users_student_id_uindex` (`student_id`);

--
-- Индексы таблицы `votes`
--
ALTER TABLE `votes`
  ADD PRIMARY KEY (`id`),
  ADD KEY `votes_tasks_id_fk` (`task_id`),
  ADD KEY `votes_users_id_fk` (`voteer_id`);

--
-- AUTO_INCREMENT для сохранённых таблиц
--

--
-- AUTO_INCREMENT для таблицы `bans`
--
ALTER TABLE `bans`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT для таблицы `tasks`
--
ALTER TABLE `tasks`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT для таблицы `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT для таблицы `votes`
--
ALTER TABLE `votes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- Ограничения внешнего ключа сохраненных таблиц
--

--
-- Ограничения внешнего ключа таблицы `bans`
--
ALTER TABLE `bans`
  ADD CONSTRAINT `bans_users_intruder_id_fk` FOREIGN KEY (`intruder_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `bans_users_punisher_id_fk` FOREIGN KEY (`punisher_id`) REFERENCES `users` (`id`);

--
-- Ограничения внешнего ключа таблицы `votes`
--
ALTER TABLE `votes`
  ADD CONSTRAINT `votes_tasks_id_fk` FOREIGN KEY (`task_id`) REFERENCES `tasks` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `votes_users_id_fk` FOREIGN KEY (`voteer_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
