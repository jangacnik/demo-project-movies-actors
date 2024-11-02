package com.demo.actor.interceptor;

import com.demo.actor.services.RequestCounterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Log4j2
@Component
@RequiredArgsConstructor
public class RequestCounterInterceptor implements HandlerInterceptor {
  private final RequestCounterService requestCounterService;

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex) throws Exception {
    HandlerMethod handlerMethod = (HandlerMethod) handler;
    String className = handlerMethod.getBeanType().getSimpleName();
    String methodName = Objects.requireNonNull(handlerMethod.getResolvedFromHandlerMethod()).getMethod().getName();
    requestCounterService.incrementCounter(className, methodName);
    HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
  }
}
