package io.revolut.moneytransfer.domain;

/**
 * Base class for all endpoint responses by <br>
 * setting response status code into {@link ApiResponseBean#statusCode} field
 * <br>
 * and the appropriate apiResponseBody into
 * {@link ApiResponseBean#apiResponseBody} field
 */
public class ApiResponseBean {

	/**
	 * response status code to return to client
	 */
	private int statusCode;

	/**
	 * json formatted string to be sent as response body
	 */
	private String apiResponseBody;

	/**
	 * any extra errorReason to be passed to client, eg. errorReason why entity did
	 * not save in backend.
	 */
	private String errorReason;

	public ApiResponseBean(int statusCode, String apiResponseBody) {
		this.statusCode = statusCode;
		this.apiResponseBody = apiResponseBody;
	}

	public ApiResponseBean(int statusCode, String apiResponseBody, String errorReason) {
		this.statusCode = statusCode;
		this.apiResponseBody = apiResponseBody;
		this.errorReason = errorReason;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public ApiResponseBean setStatusCode(int statusCode) {
		this.statusCode = statusCode;
		return this;
	}

	public String getApiResponseBody() {
		return apiResponseBody;
	}

	public ApiResponseBean setApiResponseBody(String apiResponseBody) {
		this.apiResponseBody = apiResponseBody;
		return this;
	}

	public String getErrorReason() {
		return errorReason;
	}

	public ApiResponseBean setErrorReason(String errorReason) {
		this.errorReason = errorReason;
		return this;
	}

	public static ApiResponseBean getErrorBean(int statusCode, String errorReason) {
		return new ApiResponseBean(statusCode, "", errorReason);
	}

	public static ApiResponseBean getSuccessBean(int statusCode) {
		return new ApiResponseBean(statusCode, "", "");
	}

	public static ApiResponseBean getSuccessBean(int statusCode, String responseBody) {
		return new ApiResponseBean(statusCode, responseBody, "");
	}
}
