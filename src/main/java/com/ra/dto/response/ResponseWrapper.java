package com.ra.dto.response;

import com.ra.constants.EHttpStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ResponseWrapper<T> {
	EHttpStatus eHttpStatus;
	int statusCode;
	T data;
}
