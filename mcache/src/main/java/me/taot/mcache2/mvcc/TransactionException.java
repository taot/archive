package me.taot.mcache2.mvcc;

import me.taot.mcache2.CacheException;

public class TransactionException extends CacheException {

    public TransactionException() {
    }
    
    public TransactionException(String message) {
        super(message);
    }
    
    public TransactionException(Throwable cause) {
        super(cause);
    }
    
    public TransactionException(String message, Throwable cause) {
        super(message, cause);
    }
}
