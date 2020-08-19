-- 电话请用字符串，id用long会报错请用bigint
create table USER(
    ID bigint primary key auto_increment,
    NAME varchar(100) not null ,
    TEL varchar(20) unique ,
    AVATAR_URL varchar(1024),
    CREATED_AT datetime,
    UPDATED_AT datetime
);