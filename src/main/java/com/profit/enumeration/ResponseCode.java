package com.profit.enumeration;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ResponseCode {

	SUCCESS(1000, "SUCCESS", ""), FAILED(1001, "FAILED", ""),
	BAD_REQUEST(1002, "BAD_REQUEST", "Check your request object and tryagain"),
	ERROR_STORING_DATA(1003, "ERROR_STORING_DATA", "Failed to store data, please contact for support"),
	USER_NOT_FOUND(1004, "USER_NOT_FOUND", ""), MOBILE_NUMBER_ALREADY_EXISTS(1005, "MOBILE_NUMBER_ALREADY_EXISTS", ""),
	DEACTIVATED_USER(1006, "DEACTIVATED_USER", "User deactivated"),
	INACTIVE_USER(1007, "INACTIVE_USER", "User is inactive"),

	MOBILE_NUM_NOT_FOUND(1008, "MOBILE_NUM_NOT_FOUND", "Mobile number not registered at admin portal"),

	OBJECT_CAN_NOT_BE_NULL(1009, "OBJECT_CAN_NOT_BE_NULL", "Check Object and try again"),

	INVALID_CREDENTIALS(1010, "INVALID_CREDENTIALS", "Check your credentials"),

	INVALID_QUERY(1011, "INVALID_QUERY", ""),
	OLD_NEW_PWD_SAME(1012, "OLD_NEW_PWD_SAME", "Old and New password are same"),
	OTP_SENT_TO_YOUR_EMAIL(1013, "OTP_SEND_TO_YOUR_EMAIL", "Please check your email for OTP"),
	INVALID_OTP(1014, "INVALID_OTP", "Please enter a valid OTP"),
	OTP_EXPIRED(1015, "OTP_EXPIRED", "OTP expired try again"),
	PASSWORD_UPDATED_SUCESSFULLY(1016, "PASSWORD_UPDATED_SUCESSFULLY", "Password updated successful"),
	DUPLICATE_ENTRY(1017, "DUPLICATE_ENTRY", "Record already exists"),
	INVALID_PERMISSIONS(1018, "INVALID_PERMISSIONS", "You do not have permission");

	private final int code;

	private final String message;

	private final String userMessage;

	private static final Map<Integer, ResponseCode> responseCodeMap = Arrays.asList(ResponseCode.values()).stream()
			.collect(Collectors.toMap(ResponseCode::getCode, Function.identity()));

	ResponseCode(int code, String message, String userMessage) {
		this.code = code;
		this.message = message;
		this.userMessage = userMessage;

	}

	public int getCode() {
		return this.code;
	}

	public String getMessage() {
		return this.message;
	}

	public String getUserMessage() {
		return this.userMessage;
	}

	public static ResponseCode valueOf(int value) {
		if (responseCodeMap.containsKey(value)) {
			return responseCodeMap.get(value);
		}
		throw new IllegalArgumentException("No matching constant for [" + value + "]");
	}

	@Override
	public String toString() {
		return this.code + " " + this.message;
	}

}
