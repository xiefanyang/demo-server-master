package com.hnyr.sys.rbac.service.impl;

import com.google.common.collect.Sets;
import com.hnyr.sys.config.Constant;
import com.hnyr.sys.rbac.service.ApiEncryptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ApiEncryptServiceImpl implements ApiEncryptService {

    @Value("${hnyr.encrypt.uri}")
    private String responseUris;

    @Override
    public boolean checkEncryptResponseApi(String uri) {
        if (org.springframework.util.StringUtils.hasText(responseUris)) {
            if (Sets.newHashSet(responseUris.split(Constant.COMMA)).contains(uri)) {
                return true;
            }
        }
        return false;
    }
}
