-- 电话请用字符串，id用long会报错请用bigint
create table USER(
    ID bigint primary key auto_increment,
    NAME varchar(100) not null ,
    TEL varchar(20) unique ,
    AVATAR_URL varchar(1024),
    ADDRESS varchar (1024),
    CREATED_AT timestamp not null default now(),
    UPDATED_AT timestamp not null default now()
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;