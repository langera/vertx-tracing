package io.vertx.tracing.opentracing;

import io.opentracing.Span;
import io.opentracing.mock.MockSpan;
import io.opentracing.mock.MockTracer;
import io.vertx.codegen.annotations.Nullable;
import io.vertx.core.*;
import io.vertx.core.json.JsonObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static io.vertx.core.spi.tracing.SpanKind.RPC;
import static io.vertx.core.spi.tracing.TagExtractor.empty;
import static io.vertx.core.tracing.TracingPolicy.ALWAYS;
import static java.util.Collections.emptyList;

public class OpenTracingTracerTest {

  private MockTracer tracer;
  private OpenTracingTracer openTracingTracer;
  private Context context;

  @Before
  public void setup() {
    tracer = new MockTracer();
    context = dummyContext();

  }

  @Test
  public void shouldDecorateSpanWhenRequestReceived() {
    openTracingTracer = new OpenTracingTracer(true, tracer, (span, o) -> span.setTag("KEY", "VALUE"));

    MockSpan span = (MockSpan)
      openTracingTracer.receiveRequest(context, RPC, ALWAYS, null, "req", emptyList(), empty());

    Assert.assertEquals("VALUE", span.tags().get("KEY"));
  }

  @Test
  public void shouldDecorateSpanWhenRequestSent() {
    openTracingTracer = new OpenTracingTracer(true, tracer, (span, o) -> span.setTag("KEY", "VALUE"));

    MockSpan span = (MockSpan)
      openTracingTracer.sendRequest(context, RPC, ALWAYS, null, "req", null, empty());

    Assert.assertEquals("VALUE", span.tags().get("KEY"));
  }

  @Test
  public void shouldDecorateSpanWhenResponseReceived() {
    openTracingTracer = new OpenTracingTracer(true, tracer, (span, o) -> span.setTag("KEY", "VALUE"));
    MockSpan span = tracer.buildSpan("res").start();

    openTracingTracer.receiveResponse(context, null, span, null, empty());

    Assert.assertEquals("VALUE", span.tags().get("KEY"));
  }

  @Test
  public void shouldDecorateSpanWhenResponseSent() {
    openTracingTracer = new OpenTracingTracer(true, tracer, (span, o) -> span.setTag("KEY", "VALUE"));
    MockSpan span = tracer.buildSpan("res").start();

    openTracingTracer.sendResponse(context, null, span, null, empty());

    Assert.assertEquals("VALUE", span.tags().get("KEY"));
  }

  @Test
  public void shouldDecorateSpanWithMultipleDecorators() {
    openTracingTracer = new OpenTracingTracer(true, tracer,
          (span, o) -> span.setTag("KEY1", "VALUE1"),
          (span, o) -> span.setTag("KEY2", "VALUE2"));

    MockSpan span = (MockSpan)
      openTracingTracer.receiveRequest(context, RPC, ALWAYS, null, "req", emptyList(), empty());

    Assert.assertEquals("VALUE1", span.tags().get("KEY1"));
    Assert.assertEquals("VALUE2", span.tags().get("KEY2"));
  }

  @Test
  public void shouldNotDecorateSpanWhenDecoratorDoesNotSupportObject() {
    openTracingTracer = new OpenTracingTracer(true, tracer,
        new OpenTracingSpanDecorator() {
            @Override
            public void decorate(Span span, Object obj) {
              span.setTag("KEY", "VALUE");
            }

            @Override
            public boolean supports(Object obj) {
              return false;
            }
    });

    MockSpan span = (MockSpan)
        openTracingTracer.receiveRequest(context, RPC, ALWAYS, null, "req", emptyList(), empty());

    Assert.assertNull(span.tags().get("KEY"));
  }

  private Context dummyContext() {
    return new Context() {

      @Override
      public void runOnContext(Handler<Void> action) {

      }

      @Override
      public <T> void executeBlocking(Handler<Promise<T>> blockingCodeHandler, boolean ordered, Handler<AsyncResult<@Nullable T>> asyncResultHandler) {

      }

      @Override
      public <T> void executeBlocking(Handler<Promise<T>> blockingCodeHandler, Handler<AsyncResult<@Nullable T>> asyncResultHandler) {

      }

      @Override
      public <T> Future<@Nullable T> executeBlocking(Handler<Promise<T>> blockingCodeHandler, boolean ordered) {
        return null;
      }

      @Override
      public <T> Future<T> executeBlocking(Handler<Promise<T>> blockingCodeHandler) {
        return null;
      }

      @Override
      public String deploymentID() {
        return null;
      }

      @Override
      public @Nullable JsonObject config() {
        return null;
      }

      @Override
      public List<String> processArgs() {
        return null;
      }

      @Override
      public boolean isEventLoopContext() {
        return false;
      }

      @Override
      public boolean isWorkerContext() {
        return false;
      }

      @Override
      public <T> T get(String key) {
        return null;
      }

      @Override
      public void put(String key, Object value) {

      }

      @Override
      public boolean remove(String key) {
        return false;
      }

      @Override
      public <T> T getLocal(String key) {
        return null;
      }

      @Override
      public void putLocal(String key, Object value) {
      }

      @Override
      public boolean removeLocal(String key) {
        return false;
      }

      @Override
      public Vertx owner() {
        return null;
      }

      @Override
      public int getInstanceCount() {
        return 0;
      }

      @Override
      public Context exceptionHandler(@Nullable Handler<Throwable> handler) {
        return null;
      }

      @Override
      public @Nullable Handler<Throwable> exceptionHandler() {
        return null;
      }
    };
  }
}
