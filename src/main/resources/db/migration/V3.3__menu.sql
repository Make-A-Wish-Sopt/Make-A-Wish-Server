create table gift_menu
(
    menu_id   bigserial primary key,
    name      varchar(255),
    price     integer not null
);

insert into gift_menu(name, price) values ('coffee', 4900);
insert into gift_menu(name, price) values ('vitamin', 9900);
insert into gift_menu(name, price) values ('chicken', 17900);
insert into gift_menu(name, price) values ('handcream', 18900);
insert into gift_menu(name, price) values ('sushi', 25900);
insert into gift_menu(name, price) values ('cake', 32900);