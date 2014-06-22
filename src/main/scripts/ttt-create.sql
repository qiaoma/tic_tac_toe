
    create table games (
        id int4 not null,
        game_type int4,
        player1_id int4,
        player2_id int4,
        start_time timestamp,
        end_time timestamp,
        save_time timestamp,
        position varchar(255),
        outcome varchar(255),
        primary key (id)
    );

    create table users (
        id int4 not null,
        username varchar(255) not null,
        password varchar(255),
        email varchar(255),
        enabled boolean not null,
        primary key (id)
    );

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

    create sequence hibernate_sequence minvalue 100;
    
    create table authorities (
    	id int4 not null,
	    username    varchar(255) not null references users(username),
	    authority   varchar(255),
	    primary key (id)
	);

insert into users values (1, 'cysun', 'abcd', 'cysun@localhost.localdomain', 't');

insert into games (id, game_type, player1_id, start_time, end_time, outcome) values (1, 1, 1, 'Thu Apr 24 13:50:26 PDT 2014', 'Thu Apr 24 13:57:26 PDT 2014', 'win');
insert into games (id, game_type, player1_id, start_time, end_time, outcome) values (2, 1, 1, 'Thu Apr 24 14:52:26 PDT 2014', 'Thu Apr 24 14:57:26 PDT 2014', 'loss');
insert into games values (3, 1, 1, null, 'Thu Apr 24 14:52:26 PDT 2014', null, 'Thu Apr 24 14:52:46 PDT 2014', 'X---O----', 'unfinish');

insert into authorities values(1, 'cysun', 'ROLE_USER');
