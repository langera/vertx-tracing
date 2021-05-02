package io.vertx.tracing.opentracing;

import io.opentracing.Span;

public interface OpenTracingSpanDecorator {

  default boolean supports(Object obj) {
    return true;
  }

  void decorate(Span span, Object obj);
}

