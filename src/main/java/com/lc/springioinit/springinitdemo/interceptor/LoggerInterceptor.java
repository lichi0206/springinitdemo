package com.lc.springioinit.springinitdemo.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lc.springioinit.springinitdemo.jpa.LoggerJPA;
import com.lc.springioinit.springinitdemo.model.BaseLog;
import com.lc.springioinit.springinitdemo.utils.LoggerUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;

/**
 * Logger interceptor
 *
 * @auther lichi
 * @create 2017-10-29 20:24
 */
public class LoggerInterceptor implements HandlerInterceptor {

    public static final String LOGGER_SEND_TIME = "loggerSendTime";
    public static final String LOGGER_ENTITY = "loggerEntity";

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        BaseLog logger = new BaseLog();

        String sessionId = httpServletRequest.getRequestedSessionId();

        String uri = httpServletRequest.getRequestURI();

        String paramData = JSON.toJSONString(httpServletRequest.getParameterMap(),
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteMapNullValue);
        long currentTime = System.currentTimeMillis();

        logger.setSessionId(LoggerUtils.getClientIp(httpServletRequest));

        logger.setMethod(httpServletRequest.getMethod());

        logger.setType(LoggerUtils.getRequestType(httpServletRequest));

        logger.setPrarmData(paramData);

        logger.setUri(uri);

        logger.setSessionId(sessionId);

        logger.setTime(new Timestamp(currentTime));

        httpServletRequest.setAttribute(LOGGER_SEND_TIME, System.currentTimeMillis());

        httpServletRequest.setAttribute(LOGGER_ENTITY, logger);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        // 获取请求错误码
        int status = httpServletResponse.getStatus();
        // 当前时间
        long currentTime = System.currentTimeMillis();
        // 请求开始时间
        long time = Long.valueOf(httpServletRequest.getAttribute(LOGGER_SEND_TIME).toString());
        // 获取本次请求日志实体
        BaseLog loggerEntity = (BaseLog) httpServletRequest.getAttribute(LOGGER_ENTITY);

        // 设置日志实体内容
        loggerEntity.setTimeConsuming(Integer.valueOf((currentTime - time) + ""));
        loggerEntity.setReturnTime(new Timestamp(currentTime));
        loggerEntity.setHttpStatusCode(status + "");
        loggerEntity.setReturnData(JSON.toJSONString(httpServletRequest.getAttribute(LoggerUtils.LOGGER_RETURN),
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteMapNullValue));

        // 写入DB
        LoggerJPA loggerJPA = getDao(LoggerJPA.class, httpServletRequest);
        loggerJPA.save(loggerEntity);
    }

    /**
     * 根据传入的类型获取Spring管理对应的Dao
     *
     * @param clazz
     * @param request
     * @param <T>
     * @return
     */
    private <T> T getDao(Class<T> clazz, HttpServletRequest request) {
        BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
        return factory.getBean(clazz);
    }
}
