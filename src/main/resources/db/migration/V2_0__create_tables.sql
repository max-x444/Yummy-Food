create table if not exists dish_category
(
    dish_category_id varchar(255) not null
    primary key,
    image            varchar(255),
    name             varchar(255)
    constraint uk_7d2njk36yguowsk4qoj9ew8kt
    unique
    );

alter table dish_category
    owner to postgres;

create table if not exists dish
(
    dish_id          varchar(255) not null
    primary key,
    calories         double precision,
    description      varchar(255),
    image            varchar(255),
    name             varchar(255)
    constraint uk_r7g2l08wdh3uv3gvurli4s1bx
    unique,
    price            double precision,
    weight           double precision,
    dish_category_id varchar(255)
    constraint fkdexet01io54i3kyi50oo9gip2
    references dish_category
    );

alter table dish
    owner to postgres;

create table if not exists usr
(
    user_id  varchar(255) not null
    primary key,
    avatar   varchar(255),
    email    varchar(20)
    constraint uk_g9l96r670qkidthshajdtxrqf
    unique,
    name     varchar(255),
    password varchar(100),
    phone    varchar(20),
    role     varchar(255),
    surname  varchar(255),
    username varchar(20)
    constraint uk_dfui7gxngrgwn9ewee3ogtgym
    unique
    );

alter table usr
    owner to postgres;

create table if not exists booking
(
    booking_id              varchar(255) not null
    primary key,
    created                 timestamp,
    dishes_preparation_time timestamp,
    status                  varchar(255),
    total_amount            integer,
    total_price             double precision,
    user_id                 varchar(255)
    constraint fkfdvqfukwcova9xb2abcm986yv
    references usr
    );

alter table booking
    owner to postgres;

create table if not exists booking_dish
(
    booking_dish_id varchar(255) not null
    primary key,
    amount          integer      not null,
    booking_id      varchar(255)
    constraint fkpqp49a9on6tvtlsstrrc4fjd8
    references booking,
    dish_id         varchar(255)
    constraint fk3wdq7dtlglhvgy82go5ct6qsm
    references dish
    );

alter table booking_dish
    owner to postgres;

create table if not exists rating
(
    rating_id varchar(255) not null
    primary key,
    comment   varchar(200),
    date      timestamp,
    grade     double precision,
    dish_id   varchar(255)
    constraint fkcs3tgkyf3h7sryovwx4rwro4
    references dish,
    user_id   varchar(255)
    constraint fklgi9t6w691ggmgm5m5s5ebqjl
    references usr
    );

alter table rating
    owner to postgres;