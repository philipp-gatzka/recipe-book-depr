create table recipe_book_member
(
    id                     int                        not null auto_increment,
    creation_timestamp     timestamp                  not null default current_timestamp,
    modification_timestamp timestamp                  not null default current_timestamp on update current_timestamp,
    creation_user_id       int                        not null,
    modification_user_id   int                        not null,
    recipe_book_id         int                        not null,
    user_id                int                        not null,
    state                  enum ('INVITED', 'JOINED') not null default 'INVITED',
    role                   enum ('OWNER', 'MEMBER')   not null default 'MEMBER',
    constraint pk_recipe_book_member__id primary key (id),
    constraint uk_recipe_book_member__identifier unique (recipe_book_id, user_id),
    constraint fk_recipe_book_member__creation_user_id foreign key (creation_user_id) references `user` (id),
    constraint fk_recipe_book_member__modification_user_id foreign key (modification_user_id) references `user` (id),
    constraint fk_recipe_book_member__recipe_book_id foreign key (recipe_book_id) references recipe_book (id),
    constraint fk_recipe_book_member__user_id foreign key (user_id) references `user` (id)
)