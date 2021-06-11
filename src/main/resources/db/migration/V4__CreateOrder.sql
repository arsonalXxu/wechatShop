-- PENDING 待付款，PAID 已付款， DELIVERED 物流中，RECEIVED 已收货
create table `ORDER`(
    ID bigint primary key auto_increment,
    USER_ID bigint,
    TOTAL_PRICE decimal,
    ADDRESS varchar(1024),
    EXPRESS_COMPANY varchar(16),
    EXPRESS_ID varchar(128),
    STATUS varchar(16),
    CREATED_AT timestamp not null default now(),
    UPDATED_AT timestamp not null default now()
)ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

create table ORDER_GOODS(
    ID bigint primary key auto_increment,
    GOODS_ID bigint,
    NUMBER decimal
)ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;