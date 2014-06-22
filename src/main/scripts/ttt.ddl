
    create table authorities (
        id int4 not null,
        authority varchar(255),
        username varchar(255) not null,
        primary key (id)
    );

    create table games (
        id int4 not null,
        end_time timestamp,
        game_type int4,
        outcome varchar(255),
        position varchar(255),
        save_time timestamp,
        start_time timestamp,
        player1_id int4,
        player2_id int4,
        primary key (id)
    );

    create table users (
        id int4 not null,
        email varchar(255),
        enabled boolean not null,
        password varchar(255),
        username varchar(255) not null,
        primary key (id)
    );

    alter table authorities 
        add constraint UK_baahryprcge2u172egph1qwur unique (username);

    alter table users 
        add constraint UK_r43af9ap4edm43mmtq01oddj6 unique (username);

    alter table games 
        add constraint FK_gf5dddpb3pb14c1057sbdgn43 
        foreign key (player1_id) 
        references users;

    alter table games 
        add constraint FK_bv9atljedlpewpf76urp88mqv 
        foreign key (player2_id) 
        references users;

    create sequence hibernate_sequence;
