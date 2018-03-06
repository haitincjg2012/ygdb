package com.apec.framework.core.handler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.apec.framework.common.exception.DispatchException;
import com.apec.framework.common.exception.ExceptionInfo;

/**
 * @author xxx
 */
@ControllerAdvice
public class DispatchExceptionHandler
{
    @ExceptionHandler(value = DispatchException.class)
    @ResponseBody
    public ExceptionInfo jsonErrorHandler(HttpServletRequest req, DispatchException e) throws Exception
    {
        ExceptionInfo error = new ExceptionInfo();
        error.setErrorMessgae(e.getMessage());
        error.setErrorCode(e.getErrorCode());
        error.setUrl(req.getRequestURL().toString());
        return error;
    }
}