create table if not exists tickets
(
    id         serial primary key,
    session_id int references sessions (id),
    pos_row    int not null,
    cell       int not null,
    user_id    int references users (id),
    constraint seat_unique unique (session_id, pos_row, cell)
);

comment on table tickets is 'Билеты';
comment on column tickets.id is 'Идентификатор билета';
comment on column tickets.session_id is 'Внешний ключ на купленный сеанс';
comment on column tickets.pos_row is 'Ряд в кинозале соответствующий купленному билету';
comment on column tickets.cell is 'Место в кинозале соответствующее купленному билету';