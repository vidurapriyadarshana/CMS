package edu.epic.cms.api;

public class CommonResponse {

    private int code;
    private String status;
    private Object data;

    public CommonResponse() {
    }

    public CommonResponse(int code, String status, Object data) {
        this.code = code;
        this.status = status;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static CommonResponse success(Object data) {
        return new CommonResponse(200, "SUCCESS", data);
    }
    public static CommonResponse created(Object data) {
        return new CommonResponse(201, "CREATED", data);
    }
    public static CommonResponse error(int code, String message) {
        return new CommonResponse(code, "ERROR", message);
    }
    public static CommonResponse error(int code, Object data) {
        return new CommonResponse(code, "ERROR", data);
    }
    public static CommonResponse notFound(String message) {
        return new CommonResponse(404, "NOT_FOUND", message);
    }
    public static CommonResponse badRequest(String message) {
        return new CommonResponse(400, "BAD_REQUEST", message);
    }
}
