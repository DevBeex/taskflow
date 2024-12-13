package com.trello.trello.config;

import com.trello.trello.dto.response.ApiResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class ApiResponseHandler<T> implements ResponseBodyAdvice<T> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public T beforeBodyWrite(T body,
                             MethodParameter returnType,
                             MediaType contentType,
                             Class<? extends HttpMessageConverter<?>> converterType,
                             ServerHttpRequest request,
                             ServerHttpResponse response) {
        if (body instanceof ApiResponse) {
            return body;
        }

        return (T) new ApiResponse<>(HttpStatus.OK.value(), "Success", body);
    }
}
