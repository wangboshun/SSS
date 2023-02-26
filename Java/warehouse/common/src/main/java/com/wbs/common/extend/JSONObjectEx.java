package com.wbs.common.extend;

import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONObject;

public class JSONObjectEx extends JSONObject {
    public JSONObjectEx(Object source) {
        super(source, JSONConfig.create().setDateFormat("yyyy-MM-dd HH:mm:ss"));
    }
}
