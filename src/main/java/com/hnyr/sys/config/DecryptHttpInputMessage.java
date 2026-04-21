package com.hnyr.sys.config;

import com.hnyr.sys.utils.SmUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @ClassName: DecryptHttpInputMessage
 * @Description: 请求包解密处理
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
public class DecryptHttpInputMessage implements HttpInputMessage {
    private HttpInputMessage inputMessage;

    public DecryptHttpInputMessage(HttpInputMessage inputMessage) {
        this.inputMessage = inputMessage;
    }

    @Override
    public InputStream getBody() throws IOException {
        return new ByteArrayInputStream(SmUtils.sm2DecRequest(inputMessage.getBody()));
    }

    @Override
    public HttpHeaders getHeaders() {
        return inputMessage.getHeaders();
    }
}