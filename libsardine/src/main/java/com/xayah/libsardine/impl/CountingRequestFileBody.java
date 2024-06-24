package com.xayah.libsardine.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

public class CountingRequestFileBody extends RequestBody {
    private static final int BUFFER_SIZE = 128 * 1024;

    private final File file;
    private final OnWriteListener onWriteListener;
    private final MediaType contentType;
    private long totalWrite = 0;

    public CountingRequestFileBody(File file, OnWriteListener onWriteListener, MediaType contentType) {
        this.file = file;
        this.onWriteListener = onWriteListener;
        this.contentType = contentType;
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
        Source source = null;
        try {
            source = Okio.source(file);
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
        }
    }

    public interface OnWriteListener {
        void onWrite(long value, long max);
    }
}
