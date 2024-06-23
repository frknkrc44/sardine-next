package com.xayah.libsardine.io;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;

public class CountingResponseBody extends ResponseBody {
    private final ResponseBody mResponseBody;
    private final CountingListener mListener;
    private long mCurLength;
    private long mTotalLength;

    public CountingResponseBody(ResponseBody mResponseBody, CountingListener mListener) {
        this.mResponseBody = mResponseBody;
        this.mListener = mListener;
    }

    @Override
    public long contentLength() {
        return mResponseBody.contentLength();
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return mResponseBody.contentType();
    }

    @NotNull
    @Override
    public BufferedSource source() {
        mTotalLength = contentLength();
        ForwardingSource forwardingSource = new ForwardingSource(mResponseBody.source()) {
            @Override
            public long read(@NotNull Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                mCurLength += bytesRead != -1 ? bytesRead : 0;
                if (mListener != null) {
                    mListener.onProgress(mTotalLength, mCurLength);
                }
                return bytesRead;
            }
        };
        return Okio.buffer(forwardingSource);
    }
}
