package com.lc.springioinit.springinitdemo.model;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * log
 *
 * @auther lichi
 * @create 2017-10-29 20:10
 */
@Entity(name = "baselog")
public class BaseLog implements Serializable {

    // 编号
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // 客户端请求IP
    @Column(name = "client_ip")
    private String clientIp;

    // 客户端请求路径
    private String uri;

    // 终端请求方式：普通请求/Ajax请求
    private String type;

    // 请求方法：post/get...
    private String method;

    // 请求参数：JSON
    @Column(name = "param_data")
    private String prarmData;

    // 请求接口唯一Session标识
    @Column(name = "session_id")
    private String sessionId;

    // 请求时间
    @Column(name = "time")
    private Timestamp time;

    // 接口返回时间
    @Column(name = "return_time")
    private Timestamp returnTime;

    // 接口返回数据：JSON
    @Column(name = "return_data")
    private String returnData;

    // Status Code: 200/500...
    @Column(name = "status_code")
    private String httpStatusCode;

    // 请求耗时：seconds
    @Column(name = "time_consuming")
    private int timeConsuming;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPrarmData() {
        return prarmData;
    }

    public void setPrarmData(String prarmData) {
        this.prarmData = prarmData;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public Timestamp getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Timestamp returnTime) {
        this.returnTime = returnTime;
    }

    public String getReturnData() {
        return returnData;
    }

    public void setReturnData(String returnData) {
        this.returnData = returnData;
    }

    public String getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(String httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public int getTimeConsuming() {
        return timeConsuming;
    }

    public void setTimeConsuming(int timeConsuming) {
        this.timeConsuming = timeConsuming;
    }
}
