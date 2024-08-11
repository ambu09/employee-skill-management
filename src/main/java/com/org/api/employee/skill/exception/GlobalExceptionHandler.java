/*
 * Copyright (C) 2024, Oraganization - All Rights Reserved.
 * CONFIDENTIAL INFORMATION - DO NOT DISTRIBUTE
 */

package com.org.api.employee.skill.exception;

import com.org.api.employee.skill.exception.model.ErrorDetails;
import com.org.api.employee.skill.exception.model.ErrorDto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  String type = "https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/";
  String invalidRequest = "The request is invalid.";

  /**
   * Handle authenticationException.
   *
   * @param ex      authenticationException
   * @param request http request
   * @return error response
   */
  @ExceptionHandler(UnAuthorizedException.class)
  public ResponseEntity<ErrorDetails> handleUnAuthorizedException(UnAuthorizedException ex, WebRequest request) {
    int status = HttpStatus.FORBIDDEN.value();
    ErrorDetails errorDetails = new ErrorDetails(type + status, "Forbidden", status,
        "The request could not be completed because of invalid permissions.");
    return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
  }

  /**
   * Fired on constraint validation (id supplied).
   *
   * @param ex      exception
   * @param request request
   * @return Chosen exception class
   */
  @ExceptionHandler(InvalidInputException.class)
  public ResponseEntity<ErrorDto> inValidInputException(InvalidInputException ex, WebRequest request) {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    int statusCode = status.value();
    List<ErrorDto.ErrorDetailsDto> errorDetailsDtosList = new ArrayList<>();
    ErrorDto.ErrorDetailsDto errorDetailsDto = new ErrorDto.ErrorDetailsDto(ex.getFieldName(), ex.getMessage());
    errorDetailsDtosList.add(errorDetailsDto);
    ErrorDto errorDetails = new ErrorDto(type + statusCode, status.getReasonPhrase(), statusCode, invalidRequest,
        errorDetailsDtosList);
    return new ResponseEntity<>(errorDetails, status);
  }

  /**
   * This method is used to handle MissingRequestHeaderException.
   *
   * @param ex exception.
   * @return ErrorDto the ErrorDto.
   */
  @ExceptionHandler(MissingRequestHeaderException.class)
  public ResponseEntity<ErrorDto> handleException(MissingRequestHeaderException ex) {
    log.debug(">>> MissingRequestHeaderException : {}", ex.getMessage());
    HttpStatus status = HttpStatus.BAD_REQUEST;
    int statusCode = status.value();
    List<ErrorDto.ErrorDetailsDto> errorDetailsDto = new ArrayList<>();
    String headerName = ex.getHeaderName();
    ErrorDto.ErrorDetailsDto errorDetail = new ErrorDto.ErrorDetailsDto(headerName, ex.getBody().getDetail());
    errorDetailsDto.add(errorDetail);

    ErrorDto errorDetails = new ErrorDto(type + statusCode, status.getReasonPhrase(), statusCode, invalidRequest,
        errorDetailsDto);
    return new ResponseEntity<>(errorDetails, status);

  }

  /**
   * Fired if no resource is found (id supplied).
   *
   * @param ex      exception
   * @param request request
   * @return selected error response
   */
  @ExceptionHandler(EmployeeNotFoundException.class)
  public ResponseEntity<ErrorDto> resourceNotFoundException(EmployeeNotFoundException ex, WebRequest request) {
    HttpStatus status = HttpStatus.NOT_FOUND;
    int statusCode = status.value();
    ErrorDto errorDetails = new ErrorDto(type + statusCode, status.getReasonPhrase(), statusCode,
        "No data found for the given id", null);
    return new ResponseEntity<>(errorDetails, status);
  }

  /**
   * Fired on constraint validation (id supplied).
   *
   * @param ex      exception
   * @param request request
   * @return Chosen exception class
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorDto> handleConstraintViolationException(ConstraintViolationException ex,
      WebRequest request) {
    log.debug(">>> ConstraintViolationException : {}", ex.getMessage());

    HttpStatus status = HttpStatus.BAD_REQUEST;
    int statusCode = status.value();
    List<ErrorDto.ErrorDetailsDto> errorDetailsDtoList = new ArrayList<>();

    Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
    for (ConstraintViolation<?> violation : violations) {
      PathImpl pathImpl = (PathImpl) violation.getPropertyPath();
      ErrorDto.ErrorDetailsDto errorDetailsDto = new ErrorDto.ErrorDetailsDto(pathImpl.getLeafNode().toString(),
          violation.getMessage());
      errorDetailsDtoList.add(errorDetailsDto);
    }

    ErrorDto errorDetails = new ErrorDto(type + statusCode, status.getReasonPhrase(), statusCode, invalidRequest,
        errorDetailsDtoList);
    return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handle unexpected error.
   *
   * @param ex      runtime exception
   * @param request http request
   * @return error response
   */
  @ExceptionHandler(Exception.class)
  @ResponseBody
  public ResponseEntity<ErrorDetails> globalExceptionHandler(Exception ex, WebRequest request) {
    log.debug(">>> Exception : {}", ex.getMessage());

    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    int statusCode = status.value();
    ErrorDetails errorDetails = new ErrorDetails(type + statusCode, status.getReasonPhrase(), statusCode,
        ex.getMessage());
    return new ResponseEntity<>(errorDetails, status);
  }

}
