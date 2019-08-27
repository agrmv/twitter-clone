create table message_likes (
    user_id int not null references user,
    message_id int not null references message,
    primary key (user_id, message_id)
)