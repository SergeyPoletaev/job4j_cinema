create table if not exists sessions
(
    id   serial primary key,
    name varchar not null
);

comment on table sessions is 'Кинофильмы';
comment on column sessions.id is 'Идентификатор фильма';
comment on column sessions.name is 'Название фильма';