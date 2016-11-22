namespace java com.taot.pms.rpc.thrift

struct TLocalDate {
    1: required i32 year;
    2: required i32 month;
    3: required i32 day;
}

struct TAccount {
    1: required i64 id;
    2: required string name;
    3: TLocalDate openDate;
}

struct TAccountCreate {
    1: TAccount account;
    2: i64 initialCash;
}

service PMSService {
    i64 createAccount(1:TAccountCreate request);
    list<TAccount> loadAllAccounts();
}