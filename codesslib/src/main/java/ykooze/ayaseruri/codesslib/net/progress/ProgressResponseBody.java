package ykooze.ayaseruri.codesslib.net.progress;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by wufeiyang on 2017/1/12.
 */

public class ProgressResponseBody extends ResponseBody {

    private ResponseBody mResponseBody;
    private ProgressListener mProgressListener;
    private volatile BufferedSource mBufferedSource;

    public ProgressResponseBody(ResponseBody responseBody,
                                ProgressListener progressListener) {
        mResponseBody = responseBody;
        mProgressListener = progressListener;
    }

    @Override
    public MediaType contentType() {
        return mResponseBody.contentType();
    }

    @Override
    public long contentLength() {
        return mResponseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if(null == mBufferedSource){
            synchronized(ProgressResponseBody.class){
                if(null == mBufferedSource){
                    mBufferedSource = Okio.buffer(source(mResponseBody.source()));
                }
            }
        }
        return mBufferedSource;
    }

    private Source source(Source source){
        return new ForwardingSource(source) {
            long totalByte = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                //增加当前读取的字节数，如果读取完成了bytesRead会返回-1
                totalByte += bytesRead != -1 ? bytesRead : 0;
                //回调，如果contentLength()不知道长度，会返回-1
                mProgressListener.onResponseProgress(totalByte, mResponseBody.contentLength(), bytesRead == -1);
                return bytesRead;
            }
        };
    }
}
