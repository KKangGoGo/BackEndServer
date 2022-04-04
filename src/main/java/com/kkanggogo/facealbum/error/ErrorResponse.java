package com.kkanggogo.facealbum.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ErrorResponse {

    private LocalDateTime time = LocalDateTime.now();
    private String message; // 예외 메시지
    private String code; // 예외를 세분화하기 위한 코드
    private int status; // Http 상태 값. 400, 401, ...

    // @Valid 검증을 통과하지 못한 필드가 담김
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("errors")
    private List<CustomFieldError> customFieldErrors;

    public ErrorResponse(){}

    public static ErrorResponse create(){
        return new ErrorResponse();
    }

    public ErrorResponse code(String code){
        this.code = code;
        return this;
    }

    public ErrorResponse status(int status){
        this.status = status;
        return this;
    }

    public ErrorResponse message(String message){
        this.message = message;
        return this;
    }

    // BindingResult.getFieldErrors() 메소드를 통해 전달받은 filedErrors
    public ErrorResponse errors(Errors errors){
        setCustomFieldErrors(errors.getFieldErrors());
        return this;
    }

    public List<CustomFieldError> setCustomFieldErrors(List<FieldError> fieldErrors) {
        customFieldErrors = new ArrayList<>();

        fieldErrors.forEach(error -> {
            customFieldErrors.add(new CustomFieldError(
                    error.getCodes()[0],
                    error.getRejectedValue(),
                    error.getDefaultMessage()
            ));
        });
        return customFieldErrors;
    }

    @Getter
    public static class CustomFieldError {
        private String field;
        private Object value;
        private String reason;

        public CustomFieldError(String field, Object value, String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }
    }


}
