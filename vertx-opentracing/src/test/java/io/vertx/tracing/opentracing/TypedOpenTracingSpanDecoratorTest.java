package io.vertx.tracing.opentracing;

import io.opentracing.Span;
import org.junit.Assert;
import org.junit.Test;

public class TypedOpenTracingSpanDecoratorTest {

  @Test
  public void shouldSupportOnlyDefinedType() {

    TypedOpenTracingSpanDecorator<Number> decorator = new TypedOpenTracingSpanDecorator<Number>(Number.class) {
      @Override
      protected void decorateSpanFrom(Span span, Number number) {
        span.setTag("NUMBER", number.toString());
      }
    };

    Assert.assertTrue(decorator.supports(Integer.valueOf(17)));
    Assert.assertTrue(decorator.supports(Double.valueOf(17.19)));
    Assert.assertFalse(decorator.supports("string"));
  }
}
