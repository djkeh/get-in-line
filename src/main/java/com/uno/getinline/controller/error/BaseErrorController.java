package com.uno.getinline.controller.error;

import com.uno.getinline.constant.ErrorCode;
import com.uno.getinline.dto.ApiErrorResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class BaseErrorController implements ErrorController {

    @RequestMapping(path = "/error", produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView errorHtml(HttpServletResponse response) {
        HttpStatus httpStatus = HttpStatus.valueOf(response.getStatus());
        ErrorCode errorCode = httpStatus.is4xxClientError() ? ErrorCode.BAD_REQUEST : ErrorCode.INTERNAL_ERROR;

        if (httpStatus == HttpStatus.OK) {
            httpStatus = HttpStatus.FORBIDDEN;
            errorCode = ErrorCode.BAD_REQUEST;
        }

        return new ModelAndView(
                "error",
                Map.of(
                        "statusCode", httpStatus.value(),
                        "errorCode", errorCode,
                        "message", errorCode.getMessage(httpStatus.getReasonPhrase())
                ),
                httpStatus
        );
    }

    @RequestMapping("/error")
    public ResponseEntity<ApiErrorResponse> error(HttpServletResponse response) {
        HttpStatus httpStatus = HttpStatus.valueOf(response.getStatus());
        ErrorCode errorCode = httpStatus.is4xxClientError() ? ErrorCode.BAD_REQUEST : ErrorCode.INTERNAL_ERROR;

        if (httpStatus == HttpStatus.OK) {
            httpStatus = HttpStatus.FORBIDDEN;
            errorCode = ErrorCode.BAD_REQUEST;
        }

        return ResponseEntity
                .status(httpStatus)
                .body(ApiErrorResponse.of(false, errorCode));
    }

}