-- ЛР5, ветка manual
-- Создание таблиц для дата-модели:
-- User, Function, Point, Permission

-- На всякий случай можно выполнить в своей схеме:
-- CREATE SCHEMA IF NOT EXISTS public;

-- Пользователи системы
CREATE TABLE IF NOT EXISTS app_user (
                                        user_id   BIGSERIAL PRIMARY KEY,
                                        username  VARCHAR(32)  NOT NULL UNIQUE,
    password  VARCHAR(128) NOT NULL,
    -- 0 = обычный пользователь, 1 = администратор
    role      INTEGER      NOT NULL DEFAULT 0
    );

-- Табулированные функции
CREATE TABLE IF NOT EXISTS app_function (
                                            function_id BIGSERIAL PRIMARY KEY,
    -- владелец функции
                                            user_id     BIGINT      NOT NULL,
                                            name        VARCHAR(64) NOT NULL,
    description TEXT,
    CONSTRAINT fk_function_user
    FOREIGN KEY (user_id)
    REFERENCES app_user (user_id)
    ON DELETE CASCADE
    );

-- Точки табулированной функции
CREATE TABLE IF NOT EXISTS point (
                                     point_id    BIGSERIAL PRIMARY KEY,
                                     function_id BIGINT  NOT NULL,
    -- индекс точки в табулированной функции
                                     idx         INTEGER NOT NULL,
                                     x           DOUBLE PRECISION NOT NULL,
                                     y           DOUBLE PRECISION NOT NULL,
                                     CONSTRAINT fk_point_function
                                     FOREIGN KEY (function_id)
    REFERENCES app_function (function_id)
    ON DELETE CASCADE,
    -- в одной функции не должно быть двух точек с одним индексом
    CONSTRAINT uq_point_function_idx UNIQUE (function_id, idx)
    );

-- Разрешения на доступ к функциям
CREATE TABLE IF NOT EXISTS permission (
                                          permission_id BIGSERIAL PRIMARY KEY,
                                          user_id       BIGINT NOT NULL,
                                          function_id   BIGINT NOT NULL,
    -- 0 = NONE, 1 = READ, 2 = WRITE, 4 = DELETE (полный доступ)
                                          access        INTEGER NOT NULL,
                                          CONSTRAINT fk_permission_user
                                          FOREIGN KEY (user_id)
    REFERENCES app_user (user_id)
    ON DELETE CASCADE,
    CONSTRAINT fk_permission_function
    FOREIGN KEY (function_id)
    REFERENCES app_function (function_id)
    ON DELETE CASCADE,
    -- одно разрешение на пару (пользователь, функция)
    CONSTRAINT uq_permission_user_function UNIQUE (user_id, function_id),
    -- защита от случайных значений access
    CONSTRAINT chk_permission_access CHECK (access IN (0, 1, 2, 4))
    );

-- Дополнительные индексы для ускорения поиска

CREATE INDEX IF NOT EXISTS idx_function_user
    ON app_function (user_id);

CREATE INDEX IF NOT EXISTS idx_point_function
    ON point (function_id, idx);

CREATE INDEX IF NOT EXISTS idx_permission_user
    ON permission (user_id);

CREATE INDEX IF NOT EXISTS idx_permission_function
    ON permission (function_id);
