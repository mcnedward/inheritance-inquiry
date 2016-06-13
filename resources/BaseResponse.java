/**
 * 
 */
package com.eatingcinci.app.response;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * @author Edward McNealy <edwardmcn64@gmail.com>
 *
 */
public abstract class BaseResponse {

	protected List<String> errors;
	protected HttpStatus status;
	// TODO find some way to exclude from 200 response
	protected MultiValueMap<String, String> headers;

	public BaseResponse() {
		errors = new ArrayList<String>();
		headers = new LinkedMultiValueMap<String, String>();
	}

	public BaseResponse(String message) {
		this();
		headers.add("Message", message);
	}

	public BaseResponse(String message, boolean isError) {
		this();
		if (isError)
			errors.add(message);
		else
			headers.add("Message", message);
	}

	public void addError(String error) {
		errors.add(error);
	}

	public void addHeader(String message) {
		headers.add("Message", message);
	}

	public boolean hasErrors() {
		return !errors.isEmpty();
	}

	public abstract boolean hasResults();

	public HttpStatus getStatus() {
		if (hasErrors())
			return HttpStatus.BAD_REQUEST;
		if (!hasResults())
			return HttpStatus.NO_CONTENT;
		return HttpStatus.OK;
	}

	public MultiValueMap<String, String> getHeaders() {
		return headers;
	}
}
