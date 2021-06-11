create table SHOP(
    ID bigint primary key auto_increment,
    NAME varchar(100),
    DESCRIPTION varchar(1024),
    IMG_URL varchar(1024),
    OWNER_USER_ID bigint,
    STATUS varchar(16),
    CREATED_AT timestamp not null default now(),
    UPDATED_AT timestamp not null default now()
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;