create table sys_user
(
    id int auto_increment primary key,
    email varchar(32) not null comment '用户名，（邮箱）',
    password varchar(64) null,
    phone varchar(16) default '',
    nick_name varchar(64) default '',
    create_time datetime default CURRENT_TIMESTAMP not null,
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    constraint udx_on_username unique (email)
);
