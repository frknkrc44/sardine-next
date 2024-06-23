package com.xayah.libsardine.io;

public interface CountingListener {
    void onProgress(long total, long cur);
}
