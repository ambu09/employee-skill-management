/*
 * Copyright (C) 2024, Oraganization - All Rights Reserved.
 * CONFIDENTIAL INFORMATION - DO NOT DISTRIBUTE
 */

package com.org.api.employee.skill.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RequestHandlerInterceptor implements HandlerInterceptor {

  private static final Logger logger = LoggerFactory.getLogger(RequestHandlerInterceptor.class);

  private static final String CORRELATION_ID_NAME = "correlation-id";

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    String apigeeRequestId = request.getHeader("x-request-id");
    logger.debug("Apigee proxy gateway request id: {}", apigeeRequestId);

    final String correlationId = getCorrelationId();
    logger.debug("microservice generated corelation id: {}", correlationId);

    response.addHeader(CORRELATION_ID_NAME, correlationId);

    MDC.put(CORRELATION_ID_NAME, correlationId);
    return true;
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
      throws Exception {
    MDC.remove(CORRELATION_ID_NAME);
  }

  private String getCorrelationId() {
    return UUID.randomUUID().toString();
  }

}
