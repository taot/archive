package me.taot.mcache2.mvcc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

public class TransactionManager {

    private static Logger LOGGER = LoggerFactory.getLogger(TransactionManager.class);
    
    private ThreadLocal<Long> currentTransactionThreadLocal = new ThreadLocal<>();

    private final AtomicLong nextTxId = new AtomicLong(1L);
    
    private final ConcurrentMap<Long, Transaction> activeTransactions = new ConcurrentHashMap<>();
    
    TransactionManager() {
    }

    public Transaction beginTransaction() {
        Transaction tx = current();
        if (tx != null) {
            throw new TransactionException("Transaction already exists in current thread. Failed to begin transaction");
        }
        long id = getNextTransactionId();
        tx = new Transaction(id);
        activeTransactions.put(id, tx);
        currentTransactionThreadLocal.set(id);
        LOGGER.debug("Transaction #{} started", tx.getId());
        return tx;
    }

    public void commit() {
        Transaction tx = current();
        if (tx == null) {
            throw new TransactionException("No transaction exists in current thread. Failed to commit transaction");
        }
        activeTransactions.remove(tx.getId());
        currentTransactionThreadLocal.remove();
        LOGGER.debug("Transaction #{} commited", tx.getId());
    }

    public void rollback() {
        throw new UnsupportedOperationException();
    }

    public Transaction current() {
        Long txId = currentTransactionThreadLocal.get();
        if (txId == null) {
            return null;
        }
        Transaction tx = activeTransactions.get(txId);
        if (tx == null) {
            throw new TransactionException("Invalid transaction id: " + txId);
        }
        return tx;
    }

    long getNextTransactionId() {
        return nextTxId.getAndIncrement();
    }

    boolean isActive(long txId) {
        Transaction tx = activeTransactions.get(txId);
        return (tx == null);
    }
}
