package com.xayah.libsardine.impl;

import static com.xayah.libsardine.impl.CountingRequestFileBody.OnWriteListener;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

public class CountingRequestByteArrayBody extends RequestBody {
    private static final int BUFFER_SIZE = 128 * 1024;

    private final byte[] array;
    private final OnWriteListener onWriteListener;
    private final MediaType contentType;
    private long totalWrite = 0;

    public CountingRequestByteArrayBody(byte[] array, OnWriteListener onWriteListener, MediaType contentType) {
        this.array = array;
        this.onWriteListener = onWriteListener;
        this.contentType = contentType;
    }

    @Override
    public long contentLength() throws IOException {
        return array.length;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return contentType;
    }

    @Override
    public void writeTo(@NotNull BufferedSink bufferedSink) throws IOException {
        ByteArrayInputStream inputStream = null;
        Source source = null;
        try {
            inputStream = new ByteArrayInputStream(array);
            source = Okio.source(inputStream);
            long read;

            while ((read = source.read(bufferedSink.getBuffer(), BUFFER_SIZE)) >= 0) {
                totalWrite += read;
                bufferedSink.flush();
                onWriteListener.onWrite(totalWrite, contentLength());
            }
        } finally {
            if (source != null) {
                Util.closeQuietly(source);
            }

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Throwable ignored) {}
            }
        }
    }
}
