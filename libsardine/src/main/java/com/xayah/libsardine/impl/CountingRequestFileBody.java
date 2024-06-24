package com.xayah.libsardine.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

public class CountingRequestFileBody extends RequestBody {
    private final File file;
    private final OnWriteListener onWriteListener;
    private final MediaType contentType;
    private long totalWrite = 0;
    private final int bufferSize;

    public CountingRequestFileBody(File file, OnWriteListener onWriteListener, MediaType contentType, int bufferSize) {
        this.file = file;
        this.onWriteListener = onWriteListener;
        this.contentType = contentType;
        this.bufferSize = bufferSize;
    }

    @Override
    public long contentLength() throws IOException {
        return file.length();
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return contentType;
    }

    @Override
    public void writeTo(@NotNull BufferedSink bufferedSink) throws IOException {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            byte[] chunk = new byte[bufferSize];
            int read;

            while ((read = inputStream.read(chunk, 0, bufferSize)) > 0) {
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

    public interface OnWriteListener {
        void onWrite(long value, long max);
    }
}
