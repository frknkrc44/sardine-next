package com.xayah.libsardine.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

public class CountingRequestBody extends RequestBody {
    private final RequestBody delegate;
    private final OnWriteListener onWriteListener;

    public CountingRequestBody(RequestBody delegate, OnWriteListener onWriteListener) {
        this.delegate = delegate;
        this.onWriteListener = onWriteListener;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return delegate.contentType();
    }

    @Override
    public void writeTo(@NotNull BufferedSink bufferedSink) throws IOException {
        long size = bufferedSink.getBuffer().size();
        delegate.writeTo(bufferedSink);
        onWriteListener.onWrite(size, contentLength());
    }

    public interface OnWriteListener {
        void onWrite(long value, long max);
    }
}
