package com.itheima.study.a29;

import lombok.*;

/**
 * @author
 * @JsonInclude这个注解是老师视频加的 不知道干嘛用的
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Result {

    private int code;
    private String msg;
    private Object data;


    public static Result success(Object result) {
        return new Result(200, "成功", result);
    }

}
