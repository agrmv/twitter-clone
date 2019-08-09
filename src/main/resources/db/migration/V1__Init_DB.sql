create table files (
                       id integer not null,
                       data longblob,
                       file_download_uri varchar(255),
                       file_name varchar(255),
                       file_size bigint,
                       file_type varchar(2048),
                       primary key (id)
) engine=MyISAM;

create table hibernate_sequence (
    next_val bigint
) engine=MyISAM;

insert into hibernate_sequence values ( 1 );

insert into hibernate_sequence values ( 1 );

insert into hibernate_sequence values ( 1 );

create table message (
                         id integer not null,
                         text varchar(255),
                         user_id integer,
                         file_id integer,
                         primary key (id)
) engine=MyISAM;

create table user (
                      id integer not null,
                      active bit not null,
                      password varchar(255),
                      username varchar(255),
                      primary key (id)
) engine=MyISAM;

create table user_role (
                           user_id integer not null,
                           roles varchar(255)
) engine=MyISAM;

alter table message add constraint FKb3y6etti1cfougkdr0qiiemgv foreign key (user_id) references user (id);

alter table message add constraint FKo7bs4yvy3ultsmcyqfsnbifem foreign key (file_id) references files (id);

alter table user_role add constraint FK859n2jvi8ivhui0rl0esws6o foreign key (user_id) references user (id);