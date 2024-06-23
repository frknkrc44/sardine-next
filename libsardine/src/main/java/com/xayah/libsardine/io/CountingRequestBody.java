package com.xayah.libsardine.io;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;

public class CountingRequestBody extends RequestBody {
    private final RequestBody mRequestBody;
    private final CountingListener mListener;
    private long mCurLength;
    private long mTotalLength;

    public CountingRequestBody(RequestBody requestBody, CountingListener listener) {
        this.mListener = listener;
        this.mRequestBody = requestBody;
    }

    @Override
    public long contentLength() throws IOException {
        return mRequestBody.contentLength();
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return mRequestBody.contentType();
    }

    @Override
    public void writeTo(@NotNull BufferedSink bufferedSink) throws IOException {
        mTotalLength = contentLength();
        ForwardingSink forwardingSink = new ForwardingSink(bufferedSink) {
            @Override
            public void write(@NotNull Buffer source, long byteCount) throws IOException {
                mCurLength += byteCount;
                if (mListener != null) {
                    mListener.onProgress(mTotalLength, mCurLength);
                }
                super.write(source, byteCount);
            }
        };
        BufferedSink buffer = Okio.buffer(forwardingSink);
        mRequestBody.writeTo(buffer);
        buffer.flush();
    }
}
