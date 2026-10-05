package com.ironman.paymentorder.application.kafka;

import com.ironman.paymentorder.application.exception.ExceptionCatalog;
import com.ironman.paymentorder.application.model.api.PaymentOrderInitiationTransaction;
import io.smallrye.reactive.messaging.kafka.api.OutgoingKafkaRecordMetadata;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;

@Slf4j
@ApplicationScoped
public class PaymentOrderInitiationProducer {

  private final Emitter<PaymentOrderInitiationTransaction> emitter;

  public PaymentOrderInitiationProducer(
      @Channel("payment-order-initiation-out") Emitter<PaymentOrderInitiationTransaction> emitter) {
    this.emitter = emitter;
  }

  public String publish(PaymentOrderInitiationTransaction transaction) {
    String correlationId = UUID.randomUUID().toString();
    CompletableFuture<Void> publishAck = new CompletableFuture<>();

    OutgoingKafkaRecordMetadata<String> recordMetadata =
        OutgoingKafkaRecordMetadata.<String>builder().withKey(correlationId).build();

    Message<PaymentOrderInitiationTransaction> message =
        Message.of(transaction)
            .addMetadata(recordMetadata)
            .withAck(
                () -> {
                  publishAck.complete(null);
                  return CompletableFuture.completedFuture(null);
                })
            .withNack(
                throwable -> {
                  publishAck.completeExceptionally(throwable);
                  return CompletableFuture.completedFuture(null);
                });

    emitter.send(message);

    try {
      publishAck.join();
    } catch (CompletionException e) {
      log.error("Failed to publish payment order initiation event: {}", e.getCause(), e);

      throw ExceptionCatalog.EVENT_SERVICE_ERROR.buildException();
    }

    return correlationId;
  }
}
