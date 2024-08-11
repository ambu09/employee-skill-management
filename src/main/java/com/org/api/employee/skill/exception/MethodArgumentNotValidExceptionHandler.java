/*
 * Copyright (C) 2024, Oraganization - All Rights Reserved.
 * CONFIDENTIAL INFORMATION - DO NOT DISTRIBUTE
 */

package com.org.api.employee.skill.exception;

import com.org.api.employee.skill.exception.model.ErrorDetails;
import com.org.api.employee.skill.exception.model.ErrorDto;
import com.org.api.employee.skill.model.SkillLevel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class MethodArgumentNotValidExceptionHandler {

  String type = "https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/";
  String invalidErrorMessage = "The request is invalid.";

  /**
   * Handle UnAuthorizedException.
   *
   * @param ex UnAuthorizedException
   * @return error response
   */
  @ExceptionHandler(UnAuthorizedException.class)
  @ResponseBody
  public ResponseEntity<ErrorDetails> handleUnAuthorizedCustumException(UnAuthorizedException ex) {
    log.debug(">>> UnAuthorizedException : {}", ex.getMessage());
    int status = HttpStatus.FORBIDDEN.value();
    ErrorDetails errorDetails = new ErrorDetails(type + status, "Forbidden", status,
        "The request could not be completed because of invalid permissions.");
    return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
  }

  /**
   * Handle HttpMediaTypeNotSupportedException.
   *
   * @param ex HttpMediaTypeNotSupportedException.
   * @return error response.
   */
  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  public ResponseEntity<ErrorDetails> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
    log.debug(">>> HttpMediaTypeNotSupportedException : {}", ex.getMessage());
    int status = HttpStatus.UNSUPPORTED_MEDIA_TYPE.value();
    ErrorDetails errorDetails = new ErrorDetails(type + status, "Unsupported Media Type", status, ex.getMessage());
    return new ResponseEntity<>(errorDetails, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
  }

  /**
   * This method is used to handle MethodArgumentValidationException.
   *
   * @param ex MethodArgumentNotValidException.
   * @return ErrorDto the ErrorDto.
   */
  @ResponseBody
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorDto> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    int statusCode = status.value();
    List<ErrorDto.ErrorDetailsDto> errorDetailsDto = new ArrayList<>();
    List<FieldError> errors = ex.getBindingResult().getFieldErrors();
    for (FieldError error : errors) {
      ErrorDto.ErrorDetailsDto errorDetail = new ErrorDto.ErrorDetailsDto(error.getField(), error.getDefaultMessage());
      errorDetailsDto.add(errorDetail);
    }

    ErrorDto errorDetails = new ErrorDto(type + statusCode, status.getReasonPhrase(), statusCode, invalidErrorMessage,
        errorDetailsDto);
    return new ResponseEntity<>(errorDetails, status);
  }

  /**
   * This method is used to capture MissingPathVariableException.
   *
   * @param ex WebServiceHeaderException.
   * @return Chosen exception class.
   *
   */
  @ExceptionHandler(MissingPathVariableException.class)
  public ResponseEntity<ErrorDto> handleMissingPathVariableException(MissingPathVariableException ex) {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    int statusCode = status.value();
    List<ErrorDto.ErrorDetailsDto> errorDetailsDto = new ArrayList<>();
    String pathVariable = ex.getVariableName();
    ErrorDto.ErrorDetailsDto errorDetail = new ErrorDto.ErrorDetailsDto(pathVariable, ex.getBody().getDetail());
    errorDetailsDto.add(errorDetail);
    ErrorDto errorDetails = new ErrorDto(type + statusCode, status.getReasonPhrase(), statusCode, invalidErrorMessage,
        errorDetailsDto);
    return new ResponseEntity<>(errorDetails, status);
  }

  /**
   * This method is used to validate Request Payload.
   *
   * @param e the HttpMessageNotReadableException.
   * @return ResponseEntity.
   * @throws IOException the Exception.
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorDto> handleException(HttpMessageNotReadableException e) throws IOException {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    ErrorDto errorDetails = null;
    List<ErrorDto.ErrorDetailsDto> errorDetailsDto = new ArrayList<>();
    String headerName = "Request Payload";
    ErrorDto.ErrorDetailsDto errorDetail = null;
    if (e.getMessage().contains("JSON parse error")) { // NOSONAR
      errorDetail = new ErrorDto.ErrorDetailsDto("skill.level", "Valid values are " + SkillLevel.getLevels());
      errorDetailsDto.add(errorDetail);
      errorDetails = new ErrorDto(type + "400", status.getReasonPhrase(), 400, "Please provide valid skill level.",
          errorDetailsDto);
    } else {
      errorDetail = new ErrorDto.ErrorDetailsDto(headerName, "Required request body is missing");
      errorDetailsDto.add(errorDetail);
      errorDetails = new ErrorDto(type + "400", status.getReasonPhrase(), 400, "Please provide Request payload.",
          errorDetailsDto);

    }
    return new ResponseEntity<>(errorDetails, status);
  }

  /**
   * This method is used to handle Missing RequestParameter Exception.
   *
   * @param ex      exception
   * @param request request
   * @return Chosen exception class
   */
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
      WebRequest request) {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    int statusCode = status.value();
    List<ErrorDto.ErrorDetailsDto> errorDetailsDto = new ArrayList<>();
    String paramName = ex.getParameterName();
    ErrorDto.ErrorDetailsDto errorDetail = new ErrorDto.ErrorDetailsDto(paramName, ex.getBody().getDetail());
    errorDetailsDto.add(errorDetail);
    ErrorDto errorDetails = new ErrorDto(type + statusCode, status.getReasonPhrase(), statusCode, invalidErrorMessage,
        errorDetailsDto);
    return new ResponseEntity<>(errorDetails, status);

  }

  /**
   * Handle DataAccessResourceFailureException.
   *
   * @param ex      runtime exception
   * @param request http request
   * @return error response
   */
  @ExceptionHandler(DataAccessResourceFailureException.class)
  public ResponseEntity<ErrorDetails> dataAccessResourceFailureExceptionHandler(DataAccessResourceFailureException ex,
      WebRequest request) {
    log.debug(">>> DataAccessResourceFailureException class in Global exception handler : {}", ex.getMessage());
    int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
    ErrorDetails errorDetails = new ErrorDetails(type + status, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
        status, "DataAccessResourceFailureException while quering to mongo DB");
    return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
  }

}