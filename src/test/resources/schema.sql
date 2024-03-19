drop table if exists goods cascade;

create table goods
(
    created_time  timestamp     not null,
    modified_time timestamp     not null,
    id            uuid primary key,
    name          text unique   not null,
    price         decimal(5, 2) not null
);

insert into goods (created_time, modified_time, id, name, price)
values (now(), now(), gen_random_uuid(), 'golden_apple', 1.00),
       (now(), now(), gen_random_uuid(), 'baked_potato', 0.50),
       (now(), now(), gen_random_uuid(), 'cooked_beef', 1.00),
       (now(), now(), gen_random_uuid(), 'cooked_mutton', 1.00),
       (now(), now(), gen_random_uuid(), 'cooked_chicken', 1.00),
       (now(), now(), gen_random_uuid(), 'cooked_porkchop', 1.00),
       (now(), now(), gen_random_uuid(), 'cooked_rabbit', 1.00);

drop type if exists trade_status cascade;

create type trade_status as enum ('WAIT_BUYER_PAY', 'TRADE_CLOSED', 'TRADE_SUCCESS', 'TRADE_FINISHED');

drop table if exists trade cascade;

create table trade
(
    created_time  timestamp                  not null,
    modified_time timestamp                  not null,
    id            uuid primary key,
    goods_id      uuid references goods (id) not null,
    player_id     uuid                       not null,
    status        trade_status               not null
);