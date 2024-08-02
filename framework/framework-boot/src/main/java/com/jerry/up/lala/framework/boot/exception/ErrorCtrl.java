package com.jerry.up.lala.framework.boot.exception;

import cn.hutool.core.bean.BeanUtil;
import com.jerry.up.lala.framework.common.r.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: 异常直接返回空
 *
 * @author FMJ
 * @date 2023/8/16 10:04
 */
@Slf4j
@RestController
public class ErrorCtrl extends BasicErrorController {

    @Autowired
    public ErrorCtrl(ErrorAttributes errorAttributes, ServerProperties serverProperties, List<ErrorViewResolver> errorViewResolvers) {
        super(errorAttributes, serverProperties.getError(), errorViewResolvers);
    }

    @Override
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        log.error("error异常 {}", getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL)));
        return new ResponseEntity<>(BeanUtil.beanToMap(R.empty()), HttpStatus.OK);
    }

    @Override
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        log.error("errorHtml异常 {}", getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL)));
        return new ModelAndView("empty");
    }

}