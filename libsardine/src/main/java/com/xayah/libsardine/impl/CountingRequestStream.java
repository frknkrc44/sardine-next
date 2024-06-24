package com.xayah.libsardine.impl;

import java.io.IOException;
import java.io.OutputStream;

public class CountingRequestStream extends OutputStream {
    private final OutputStream outputStream;
    private final CountingRequestBody.OnWriteListener onWriteListener;

    public CountingRequestStream(OutputStream outputStream, CountingRequestBody.OnWriteListener onWriteListener) {
        this.outputStream = outputStream;
        this.onWriteListener = onWriteListener;

        assert this.outputStream != null : "The OutputStream cannot be null!!!";
    }

    @Override
    public void write(int b) throws IOException {
        outputStream.write(b);
        onWriteListener.onWrite(1, -1);
    }

    @Override
    public void write(byte[] b) throws IOException {
        outputStream.write(b);
        onWriteListener.onWrite(b.length, -1);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        outputStream.write(b, off, len);
        onWriteListener.onWrite(len, -1);
    }

    @Override
    public void flush() throws IOException {
        outputStream.flush();
    }

    @Override
    public void close() throws IOException {
        outputStream.close();
    }
}
