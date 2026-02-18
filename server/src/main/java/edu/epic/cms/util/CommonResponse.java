package edu.epic.cms.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommonResponse {

    private int code;
    private String status;
    private Object data;

    public static CommonResponse success(Object data) {
        return new CommonResponse(200, "SUCCESS", data);
    }
    public static CommonResponse created(Object data) {
        return new CommonResponse(201, "CREATED", data);
    }
    public static CommonResponse error(int code, String message) {
        return new CommonResponse(code, "ERROR", message);
    }
    public static CommonResponse notFound(String message) {
        return new CommonResponse(404, "NOT_FOUND", message);
    }
    public static CommonResponse badRequest(String message) {
        return new CommonResponse(400, "BAD_REQUEST", message);
    }
}
