create table `user`
(
    id                     int          not null auto_increment,
    creation_timestamp     timestamp    not null default current_timestamp,
    modification_timestamp timestamp    not null default current_timestamp on update current_timestamp,
    firstname              varchar(50)  not null,
    lastname               varchar(50)  not null,
    email                  varchar(100) not null,
    password               varchar(255) not null,
    constraint primary key (id),
    constraint unique (email)
)