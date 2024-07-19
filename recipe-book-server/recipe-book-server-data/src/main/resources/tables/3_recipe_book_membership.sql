create table recipe_book_membership
(
    id                     int                          not null auto_increment,
    creation_timestamp     timestamp                    not null default current_timestamp,
    modification_timestamp timestamp                    not null default current_timestamp on update current_timestamp,
    creation_user_id       int                          not null,
    modification_user_id   int                          not null,
    user_id                int                          not null,
    recipe_book_id         int                          not null,
    state                  enum ('INVITED', 'ACCEPTED') not null default 'INVITED',
    permission             enum ('OWNER', 'MEMBER')     not null default 'MEMBER',
    constraint pk_recipe_book_membership__id primary key (id),
    constraint uk_recipe_book_membership__user_id___recipe_boo unique (user_id, recipe_book_id),
    constraint fk_recipe_book_membership__user_id foreign key (user_id) references `user` (id),
    constraint fk_recipe_book_membership__recipe_book_id foreign key (recipe_book_id) references recipe_book (id),
    constraint fk_recipe_book_membership__creation_user_id foreign key (creation_user_id) references `user` (id),
    constraint fk_recipe_book_membership__modification_user_id foreign key (modification_user_id) references `user` (id)
)
