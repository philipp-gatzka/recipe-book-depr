create table `user`
(
    id                      int          not null auto_increment,
    creation_timestamp      timestamp    not null default current_timestamp,
    modification_timestamp  timestamp    not null default current_timestamp on update current_timestamp,
    firstname               varchar(50)  not null,
    lastname                varchar(50)  not null,
    email                   varchar(100) not null,
    password                varchar(255) not null,
    password_reset_code     varchar(255),
    email_verification_code varchar(255),
    constraint pk_user__id primary key (id),
    constraint uk_user__email unique (email),
    constraint uk_user__password_reset_code unique (password_reset_code),
    constraint uk_user__email_verification_code unique (email_verification_code)
)