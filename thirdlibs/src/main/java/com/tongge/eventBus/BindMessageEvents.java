package com.tongge.eventBus;

/**
 * 绑定/解绑设备
 * Created by DZ on 2017/7/5.
 */
public class BindMessageEvents {

    private int code = 0;
    private String type;

    public BindMessageEvents(int code, String type){
        this.code = code;
        this.type = type;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
