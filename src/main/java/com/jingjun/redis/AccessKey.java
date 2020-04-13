package com.jingjun.redis;

public class AccessKey extends BasePrefix {
    public AccessKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static AccessKey getAccessCount = new AccessKey(1,"ac");
}
