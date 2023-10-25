package com.paper.sword.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Result {
    private Integer code;
    private Map<String, Object> data= new HashMap<>();

    public static Result success() {
        Result result = new Result();
        result.code = 200;
        return result;
    }
    
    public static Result success(int code) {
        Result result = new Result();
        result.code = code;
        return result;
    }


    public static Result error() {
        Result result = new Result();
        result.code = 500;
        return result;
    }

    public Result data(String str,Object message) {
        this.data.put(str,message);
        return this;
    }

    public Result data(String message) {
        this.data.put("info",message);
        return this;
    }
    
    
    public Result data(Map<String, Object> data) {
        this.data = data;
        return this;
    }
}
