package me.taot.mcache2.mvcc;

public class Transaction {

    private final long id;
    
    Transaction(long id) {
        this.id = id;
    }
    
    long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Transaction #" + id;
    }
}
