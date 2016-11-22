package me.taot.mcache2.mvcc;

class Revision {

    private final long creationTxId;
    
    private long deletionTxId;    // deletionId < 0 means deletionId is not set
    
    private Object value;
    
    Revision(long txId, Object value) {
        this.creationTxId = txId;
        this.deletionTxId = -1;
        this.value = value;
    }
    
    long getCreationTxId() {
        return creationTxId;
    }
    
    void setDeleteTxId(long txId) {
        this.deletionTxId = txId;
    }
    
    long getDeletionTxId() {
        return deletionTxId;
    }
    
    Object getValue() {
        return value;
    }
}
