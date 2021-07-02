package io.github.katkatee.todoapp.controller;

import java.util.Set;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfiguration implements WebMvcConfigurer {
  private Set<HandlerInterceptor> interceptors;

  MvcConfiguration(Set<HandlerInterceptor> interceptors) {
    this.interceptors = interceptors;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    interceptors.stream().forEach(registry::addInterceptor);
  }
}
