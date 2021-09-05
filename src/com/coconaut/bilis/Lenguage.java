package com.coconaut.bilis;

import org.json.simple.JSONObject;

public class Lenguage {
    private JSONObject file;

    public Lenguage(String path) {
        this.file = JSONUtil.load(path);
    }

    public String get(String str) {
        return file.get(str).toString();
    }
}
