package io.vertx.tracing.opentracing;

import io.opentracing.Span;

public abstract class TypedOpenTracingSpanDecorator<T> implements OpenTracingSpanDecorator {

  private final Class<T> type;

  protected TypedOpenTracingSpanDecorator(Class<T> type) {
    this.type = type;
  }

  @Override
  public final boolean supports(Object obj) {
    return type.isAssignableFrom(obj.getClass());
  }

  @Override
  @SuppressWarnings("unchecked")
  public final void decorate(Span span, Object obj) {
    decorateSpanFrom(span, (T)obj);
  }

  protected abstract void decorateSpanFrom(Span span, T obj);
}
