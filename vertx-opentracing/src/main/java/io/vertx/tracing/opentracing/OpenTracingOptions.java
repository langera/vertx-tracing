/*
 * Copyright (c) 2011-2020 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0
 * which is available at https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
 */
package io.vertx.tracing.opentracing;

import io.opentracing.Tracer;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.core.tracing.TracingOptions;

import java.util.Arrays;

@DataObject
public class OpenTracingOptions extends TracingOptions {

  private final OpenTracingSpanDecorator[] decorators;
  private final Tracer tracer;

  public OpenTracingOptions() {
    tracer = null;
    decorators = new OpenTracingSpanDecorator[0];
  }

  public OpenTracingOptions(OpenTracingOptions other) {
    this(other.tracer, other.decorators);
  }

  public OpenTracingOptions(JsonObject json) {
    super(json);
    tracer = null;
    decorators = new OpenTracingSpanDecorator[0];
  }

  public OpenTracingOptions(Tracer tracer, OpenTracingSpanDecorator... decorators) {
    this.tracer = tracer;
    this.decorators = decorators;
  }

  @Override
  public OpenTracingOptions copy() {
    return new OpenTracingOptions(this);
  }

  // Visible for testing
  Tracer getTracer() {
    return tracer;
  }
  OpenTracingSpanDecorator[] getDecorators() {
    return Arrays.copyOf(decorators, decorators.length);
  }

  io.vertx.core.spi.tracing.VertxTracer<?, ?> buildTracer() {
    if (tracer != null) {
      return new OpenTracingTracer(false, tracer, decorators);
    } else {
      return new OpenTracingTracer(true, OpenTracingTracer.createDefaultTracer(), decorators);
    }
  }
}
