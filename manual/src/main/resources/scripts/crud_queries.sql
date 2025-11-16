-- ЛР5, ветка manual
-- CRUD-запросы для таблиц:
-- app_user, app_function, point, permission
-- Диалект: PostgreSQL

------------------------------------------------------------
-- 1. Таблица app_user (пользователи)
------------------------------------------------------------

-- Вставка нового пользователя
-- Параметры:
--   :username  - имя пользователя
--   :password  - хэш пароля
--   :role      - роль (0 = USER, 1 = ADMIN)
INSERT INTO app_user (username, password, role)
VALUES (:username, :password, :role)
    RETURNING user_id;

-- Поиск пользователя по идентификатору
-- :user_id
SELECT user_id, username, password, role
FROM app_user
WHERE user_id = :user_id;

-- Поиск пользователя по имени
-- :username
SELECT user_id, username, password, role
FROM app_user
WHERE username = :username;

-- Обновление данных пользователя (логин, пароль, роль)
-- :user_id, :username, :password, :role
UPDATE app_user
SET username = :username,
    password = :password,
    role      = :role
WHERE user_id = :user_id;

-- Удаление пользователя по идентификатору
-- :user_id
DELETE FROM app_user
WHERE user_id = :user_id;


------------------------------------------------------------
-- 2. Таблица app_function (табулированные функции)
------------------------------------------------------------

-- Вставка новой функции
-- :user_id      - владелец
-- :name         - название функции
-- :description  - текстовое описание (может быть NULL)
INSERT INTO app_function (user_id, name, description)
VALUES (:user_id, :name, :description)
    RETURNING function_id;

-- Поиск функции по идентификатору
-- :function_id
SELECT function_id, user_id, name, description
FROM app_function
WHERE function_id = :function_id;

-- Поиск всех функций конкретного пользователя
-- :user_id
SELECT function_id, user_id, name, description
FROM app_function
WHERE user_id = :user_id
ORDER BY name;

-- Обновление данных функции (имя и описание)
-- :function_id, :name, :description
UPDATE app_function
SET name        = :name,
    description = :description
WHERE function_id = :function_id;

-- Удаление функции по идентификатору
-- :function_id
DELETE FROM app_function
WHERE function_id = :function_id;


------------------------------------------------------------
-- 3. Таблица point (точки табулированной функции)
------------------------------------------------------------

-- Вставка точки
-- :function_id  - идентификатор функции
-- :idx          - индекс точки
-- :x, :y        - координаты
INSERT INTO point (function_id, idx, x, y)
VALUES (:function_id, :idx, :x, :y)
    RETURNING point_id;

-- Поиск всех точек конкретной функции
-- :function_id
SELECT point_id, function_id, idx, x, y
FROM point
WHERE function_id = :function_id
ORDER BY idx;

-- Поиск одной точки по идентификатору
-- :point_id
SELECT point_id, function_id, idx, x, y
FROM point
WHERE point_id = :point_id;

-- Обновление координат точки
-- :point_id, :x, :y
UPDATE point
SET x = :x,
    y = :y
WHERE point_id = :point_id;

-- Удаление точки по идентификатору
-- :point_id
DELETE FROM point
WHERE point_id = :point_id;


------------------------------------------------------------
-- 4. Таблица permission (права доступа к функциям)
------------------------------------------------------------

-- Вставка разрешения
-- :user_id      - пользователь, которому даётся доступ
-- :function_id  - функция
-- :access       - тип доступа (0, 1, 2, 4)
INSERT INTO permission (user_id, function_id, access)
VALUES (:user_id, :function_id, :access)
    RETURNING permission_id;

-- Поиск разрешения по (user_id, function_id)
-- :user_id, :function_id
SELECT permission_id, user_id, function_id, access
FROM permission
WHERE user_id = :user_id
  AND function_id = :function_id;

-- Поиск всех разрешений пользователя
-- :user_id
SELECT permission_id, user_id, function_id, access
FROM permission
WHERE user_id = :user_id;

-- Обновление прав доступа
-- :permission_id, :access
UPDATE permission
SET access = :access
WHERE permission_id = :permission_id;

-- Удаление разрешения по идентификатору
-- :permission_id
DELETE FROM permission
WHERE permission_id = :permission_id;
