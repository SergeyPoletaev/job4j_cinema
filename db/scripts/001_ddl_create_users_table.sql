create table if not exists users
(
    id    serial primary key,
    name  varchar not null,
    email varchar not null unique,
    phone varchar not null unique
);

comment on table users is 'Пользователи';
comment on column users.id is 'Идентификатор пользователя';
comment on column users.name is 'Имя пользователя';
comment on column users.email is 'Эл.почта пользователя';
comment on column users.phone is 'Телефон пользователя';