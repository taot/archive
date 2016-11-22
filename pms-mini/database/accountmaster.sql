create table account (
    account_id               bigint not null auto_increment,
    account_name             varchar(80) not null,
    open_date                date not null,
    primary key (account_id)
) default character set = utf8;

create table asset_class (
    asset_class_id           bigint not null,
    asset_class_code         varchar(20) not null,
    asset_class_desc         varchar(100),
    parent_id                bigint,
    primary key (asset_class_id),
    foreign key (parent_id) references asset_class (asset_class_id)
) default character set = utf8;

create table transaction_class (
    transaction_class_id     bigint not null,
    transaction_class_code   varchar(20) not null,
    transaction_class_desc   varchar(100),
    asset_class_id           bigint not null,
    primary key (transaction_class_id),
    foreign key (asset_class_id) references asset_class (asset_class_id),
    constraint unique (transaction_class_code, asset_class_id)
) default character set = utf8;

create table position (
    position_id              bigint not null auto_increment,
    account_id               bigint not null,
    asset_class_id           bigint not null,
    asset_class_code         varchar(20) not null,
    security_id              bigint,
    currency_code            char(3),
    primary key (position_id),
    foreign key (account_id) references account (account_id),
    foreign key (asset_class_id) references asset_class (asset_class_id),
    index (account_id, asset_class_id, security_id)
) default character set = utf8;

create table position_hist (
    position_id              bigint not null,
    account_id               bigint not null,
    -- asset_class_id           bigint not null,
    -- asset_class_code         varchar(20) not null,
    as_of_date               date not null,
    ledger_id                bigint not null,
    amount                   decimal(31, 11) not null,
    primary key (position_id, as_of_date, ledger_id),
    foreign key (position_id) references position (position_id),
    foreign key (account_id) references account (account_id),
    -- foreign key (asset_class_id) references asset_class (asset_class_id),
    index (account_id, as_of_date)

    -- add index for security id
) default character set = utf8;
