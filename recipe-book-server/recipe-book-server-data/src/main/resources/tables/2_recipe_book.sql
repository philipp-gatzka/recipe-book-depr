create table recipe_book
(
    id                     int          not null auto_increment,
    creation_timestamp     timestamp    not null default current_timestamp,
    modification_timestamp timestamp    not null default current_timestamp on update current_timestamp,
    creation_user_id       int          not null,
    modification_user_id   int          not null,
    name                   varchar(50)  not null,
    identifier             varchar(255) not null,
    constraint pk_recipe_book__id primary key (id),
    constraint uk_recipe_book__identifier unique (identifier),
    constraint fk_recipe_book__creation_user_id foreign key (creation_user_id) references `user` (id),
    constraint fk_recipe_book__modification_user_id foreign key (modification_user_id) references `user` (id)
)