package com.qsadxs.project.component;


import cn.hutool.json.JSONUtil;
import com.qsadxs.project.pojo.ResultMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class MyExceptionHandler {

    @ExceptionHandler(value =Exception.class)
    @ResponseBody
    public String exceptionHandler(Exception e){
        log.info("全局异常");
        return JSONUtil.toJsonStr(ResultMap.fail("全局异常："+e.getMessage()));
    }
}



