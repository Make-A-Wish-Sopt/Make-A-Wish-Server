create table tmp_birthday
(
    id   bigserial primary key,
    birth_date varchar(10),
    phone_number varchar(11)
);

insert into tmp_birthday (birth_date, phone_number) values ('980306', '01039326455');