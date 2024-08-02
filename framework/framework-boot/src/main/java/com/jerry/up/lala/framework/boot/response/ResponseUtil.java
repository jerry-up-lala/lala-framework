package com.jerry.up.lala.framework.boot.response;

import cn.hutool.core.util.URLUtil;
import com.jerry.up.lala.framework.common.exception.Errors;
import com.jerry.up.lala.framework.common.exception.ServiceException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.InputStream;

/**
 * <p>Description: 响应工具类
 *
 * @author FMJ
 * @date 2024/2/19 17:55
 */
public class ResponseUtil {

    public static ResponseEntity<InputStreamResource> download(InputStream is, String fileName) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + URLUtil.encode(fileName));
            headers.add(HttpHeaders.PRAGMA, "no-cache");
            headers.add(HttpHeaders.EXPIRES, "0");
            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(is));
        } catch (Exception e) {
            throw ServiceException.error(Errors.DOWNLOAD_ERROR, e);
        }
    }
}
