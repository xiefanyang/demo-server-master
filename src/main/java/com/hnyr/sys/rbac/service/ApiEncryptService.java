package com.hnyr.sys.rbac.service;

public interface ApiEncryptService {
    /**
     * 检查是否为需要加密响应的 uri
     *
     * @param uri
     * @return
     */
    boolean checkEncryptResponseApi(String uri);
}
