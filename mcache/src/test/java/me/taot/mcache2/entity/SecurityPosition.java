package me.taot.mcache2.entity;


import me.taot.mcache2.util.BeanUtil;

public class SecurityPosition extends Position {

    private String security;

    protected SecurityPosition() {
    }

    public SecurityPosition(long positionId, Long accountId, int ledgerId, String security) {
        super(positionId, accountId, ledgerId);
        this.security = security;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    @Override
    public boolean equals(Object o) {
        return BeanUtil.equals(this, o);
    }

    @Override
    public String toString() {
        return BeanUtil.toString(this);
    }
}
