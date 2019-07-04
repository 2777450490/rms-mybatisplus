package com.unis.crk.enums;

import lombok.Getter;

/**
 * 系统状态枚举
 */
@Getter
public enum SystemStatusEnum {
	ENABLED(1, "启用"),
	DISABLED(2, "禁用"),
	SYSTEM_EXCEPTION(3,"系统异常");

	private int status;

	private String message;

	private SystemStatusEnum(int status, String message) {
		this.status = status;
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}
}
