package edu.epic.cms.api;

public class CommonResponseDTO {

    private int code;
    private String status;
    private Object data;

    public CommonResponseDTO() {
    }

    public CommonResponseDTO(int code, String status, Object data) {
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

    public static CommonResponseDTO success(Object data) {
        return new CommonResponseDTO(200, "SUCCESS", data);
    }
    public static CommonResponseDTO created(Object data) {
        return new CommonResponseDTO(201, "CREATED", data);
    }
    public static CommonResponseDTO error(int code, String message) {
        return new CommonResponseDTO(code, "ERROR", message);
    }
    public static CommonResponseDTO error(int code, Object data) {
        return new CommonResponseDTO(code, "ERROR", data);
    }
    public static CommonResponseDTO notFound(String message) {
        return new CommonResponseDTO(404, "NOT_FOUND", message);
    }
    public static CommonResponseDTO badRequest(String message) {
        return new CommonResponseDTO(400, "BAD_REQUEST", message);
    }
}
