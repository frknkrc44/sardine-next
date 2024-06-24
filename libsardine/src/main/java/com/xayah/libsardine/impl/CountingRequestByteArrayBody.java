package com.xayah.libsardine.impl;

import static com.xayah.libsardine.impl.CountingRequestFileBody.OnWriteListener;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

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
        try {
            inputStream = new ByteArrayInputStream(array);
            byte[] chunk = new byte[BUFFER_SIZE];
            int read;

            while ((read = inputStream.read(chunk, 0, BUFFER_SIZE)) > 0) {
                totalWrite += read;
                bufferedSink.write(chunk, 0, read);
                bufferedSink.flush();
                onWriteListener.onWrite(totalWrite, contentLength());
            }
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Throwable ignored) {}
            }
        }
    }
}
