package com.unis.crk.enums;

public enum OperationTypeEnum {
    INSERT(1,"新增"),
    DELETE(2,"删除"),
    UPDATE(3,"更新");
    private int status;
    private String message;

    private OperationTypeEnum(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
