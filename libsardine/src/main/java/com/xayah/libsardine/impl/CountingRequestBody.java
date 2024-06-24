package com.xayah.libsardine.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.Okio;

public class CountingRequestBody extends RequestBody {
    private final RequestBody delegate;
    private final OnWriteListener onWriteListener;
    private long totalWrite = 0;

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
        long contentLength = contentLength();
        CountingRequestStream countingRequestStream = new CountingRequestStream(
                bufferedSink.outputStream(),
                (value, max) -> {
                    totalWrite += value;
                    onWriteListener.onWrite(totalWrite, contentLength);
                }
        );
        BufferedSink progressSink = Okio.buffer(Okio.sink(countingRequestStream));
        delegate.writeTo(progressSink);
        progressSink.flush();
    }

    public interface OnWriteListener {
        void onWrite(long value, long max);
    }
}
