package com.bmilab.backend.global.annotation;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.http.MediaType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestBody(
        content = @Content(
                encoding = @Encoding(
                        name = "request",
                        contentType = MediaType.APPLICATION_JSON_VALUE
                )
        )
)
public @interface FormDataRequestBody {
}