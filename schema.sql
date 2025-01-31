# 필수 과제 사용 시
create table if not exists schedules
(
    schedule_id  bigint auto_increment
        primary key,
    author       varchar(45)  not null,
    todo         varchar(200) not null,
    password     varchar(255) not null,
    created_at   datetime     not null,
    last_updated datetime     not null,
    is_active    boolean      not null default true
);

# 도전 과제 사용 시
create table if not exists authors
(
    author_id    bigint      not null auto_increment
        primary key,
    name         varchar(45) not null
        unique,
    email        varchar(45) not null
        unique,
    created_at   datetime    not null,
    last_updated datetime    not null,
    is_active    boolean     not null
);

create table if not exists schedules
(
    schedule_id  bigint       not null auto_increment
        primary key,
    author_id    bigint       not null,
    todo         varchar(200) not null,
    password     varchar(255) not null,
    created_at   datetime     not null,
    last_updated datetime     not null,
    is_active    boolean      not null,

    constraint `FK_authors_TO_schedules_1`
        foreign key (author_id) references authors (author_id)
);

drop table if exists schedules;
drop table if exists authors;
