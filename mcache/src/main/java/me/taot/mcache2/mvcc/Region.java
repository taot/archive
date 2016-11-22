package me.taot.mcache2.mvcc;

import me.taot.mcache2.Key;
import me.taot.mcache2.util.BeanUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class Region {
    
    private final Class clazz;
    
    private final ConcurrentMap<Key, List<Revision>> store = new ConcurrentHashMap<>();

    private final TransactionManager transactionManager;

    Region(Class clazz, TransactionManager transactionManager) {
        this.clazz = clazz;
        this.transactionManager = transactionManager;
    }
    
    Object get(Key key) {
        Transaction tx = getTransaction();
        return get(key, tx.getId());
    }
    
    void put(Key key, Object value) {
        Transaction tx = getTransaction();
        put(key, value, tx.getId());
    }
    
    void remove(Key key) {
        Transaction tx = getTransaction();
        remove(key, tx.getId());
    }

    private Transaction getTransaction() {
        Transaction tx = transactionManager.current();
        if (tx == null) {
            throw new TransactionException("No active transaction in current thread");
        }
        return tx;
    }

    private Object get(Key key, long txId) {
        if (store.get(key) == null) {
            return null;
        }
        List<Revision> rlist = getRevisionList(key);
        synchronized (rlist) {
            Revision r = findVisibleRevision(rlist, txId);
            if (r == null) {
                return null;
            }
            Object value = r.getValue();
            Object cloned = BeanUtil.clone(value);
            return cloned;
        }
    }

    private void put(Key key, Object value, long txId) {
        List<Revision> rlist = getRevisionList(key);
        synchronized (rlist) {
            Object cloned = BeanUtil.clone(value);

            // find older version and set deletionTxId
            Revision old = findVisibleRevision(rlist, txId);
            if (old != null) {
                old.setDeleteTxId(txId);
            }
            
            // add new version of value
            Revision r = new Revision(txId, cloned);
            rlist.add(r);
        }
    }

    private Revision findVisibleRevision(List<Revision> rlist, long txId) {
        // TODO what if more than one revision is found?
        synchronized (rlist) {
            for (Revision r : rlist) {
                if (isVisible(r, txId)) {
                    return r;
                }
            }
            return null;
        }
    }

    private boolean isVisible(Revision r, long txId) {
        long creationTxId = r.getCreationTxId();
        long deletionTxId = r.getDeletionTxId();
        if (creationTxId == txId) {
            return deletionTxId < 0;
        }
        if (! transactionManager.isActive(creationTxId) || creationTxId > txId) {
            return false;
        }
        if (! transactionManager.isActive(deletionTxId) || (deletionTxId > 0 && deletionTxId <= txId)) {
            return false;
        }
        return true;
    }

    private void remove(Key key, long txId) {
        List<Revision> rlist = getRevisionList(key);
        synchronized (rlist) {
            // find older version and set deletionTxId
            Revision r = findVisibleRevision(rlist, txId);
            r.setDeleteTxId(txId);
        }
    }

    private List<Revision> getRevisionList(Key key) {
        List<Revision> rlist = store.get(key);
        if (rlist == null) {
            List<Revision> newRlist = new ArrayList<>();
            rlist = store.putIfAbsent(key, newRlist);
            if (rlist == null) {
                rlist = newRlist;
            }
        }
        return rlist;
    }
}
