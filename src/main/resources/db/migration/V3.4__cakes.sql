alter table present add column gift_menu_id int
constraint gift_menu_present references gift_menu(gift_menu_id)
on update cascade on delete cascade;