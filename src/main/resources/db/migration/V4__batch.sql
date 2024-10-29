create table ALARM_TEMPLATE(
    template_id int primary key,
    template_content text,
    template_button_url text,
    template_variables text
);

create table ALARM_TARGET(
    target_id bigint primary key,
    user_id int,
    mobile_no varchar(15),
    reg_date date,
    target_date date
);