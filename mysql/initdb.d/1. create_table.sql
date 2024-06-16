alter database moais default character set UTF8;



create table tb_user
(
    uid         bigint unsigned not null comment 'uid key'
        primary key,
    name        varchar(255)    not null comment 'nickname',
    id          varchar(255)    not null comment 'account id',
    password    varchar(255)    not null comment 'account password',
    role        varchar(25)     not null comment 'user role',
    create_at   timestamp       not null comment 'create dete',
    withdraw_at timestamp       null comment 'withdraw date',
    dormant_at  timestamp       null comment 'dormant date'
);


create table tb_todos
(
    uid         bigint unsigned                                                not null comment 'todo id'
        primary key,
    user_uid    bigint unsigned                                                not null comment 'user id',
    title       varchar(100)                                                   not null comment 'todo title',
    description text                                                           null comment 'description',
    status      enum ('TODO', 'IN_PROGRESS', 'DONE', 'PENDING') default 'TODO' not null comment 'status',
    create_at   timestamp                                                      null comment 'create date',
    update_at   timestamp                                                      null comment 'update date',
    constraint tb_todos_tb_user_uid_fk
        foreign key (user_uid) references tb_user (uid)
)
    comment 'todo list';


