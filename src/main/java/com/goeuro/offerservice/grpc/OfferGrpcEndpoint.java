package com.goeuro.offerservice.grpc;

import com.goeuro.coverage.offer.store.protobuf.BookingOffer;
import com.goeuro.offerservice.offerstore.OfferStore;
import com.goeuro.offerservice.service.TicketConfigService;
import com.goeuro.offerservice.utils.ReactiveUtil;
import com.goeuro.search2.model.proto.Offer;
import com.goeuro.search2.pi.proto.*;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class OfferGrpcEndpoint extends PiboxOfferServiceGrpc.PiboxOfferServiceImplBase {

  private final TicketConfigService ticketConfigService;
  private final OfferStore offerStore;

  @Override
  public void getOffer(
      PiboxGetOfferRequest request, StreamObserver<PiboxGetOfferResponse> observer) {
    var offerStoreId = request.getOfferStoreId();
    var provider = request.getProviderId();
    log.info("Started to call [getOffer] of offer id [{}] & provider [{}]", offerStoreId, provider);
    var query = buildOfferDetailsQuery(provider, offerStoreId);
    offerStore
        .getBookingOffer(query)
        .map(bookingOffer -> toPiboxGetOfferResponse(bookingOffer, request))
        .doOnSuccess(response -> logGetOfferSuccess(response, request))
        .doOnError(throwable -> logGetOfferError(request, throwable))
        .onErrorResume(
            throwable ->
                ReactiveUtil.createMonoError(
                    throwable,
                    String.format(
                        "Error while getOffer for offerStoreId: %s and provider: %s",
                        query.getOfferStoreId(), query.getProviderId())))
        .switchIfEmpty(
            ReactiveUtil.createNotFoundMonoError(
                String.format(
                    "GetOffer for offerStoreId [%s] and provider [%s] was not found",
                    request.getOfferStoreId(), request.getProviderId())))
        .subscribe(new GrpcCustomSubscriber<>(observer));
  }

  @Override
  public void updateOffer(
      PiboxUpdateOfferRequest request, StreamObserver<PiboxUpdateOfferResponse> observer) {
    var offerStoreId = request.getOfferStoreId();
    var provider = request.getProviderId();
    log.info(
        "Started to call [UpdateOfferGrpcEndpoint] for offer id {} and provider {}",
        offerStoreId,
        provider);
    var ticketConfigRequest = buildTicketConfigurationRequest(request, offerStoreId);
    ticketConfigService
        .refreshOfferWithTicketConfig(request)
        .doOnSuccess(response -> logUpdateOfferSuccess(ticketConfigRequest, response))
        .onErrorResume(
            throwable ->
                ReactiveUtil.createMonoError(
                    throwable,
                    String.format(
                        "Error while updating offer with ticketConfig for offerStoreId %s and provider %s",
                        request.getOfferStoreId(), request.getProviderId())))
        .doOnError(throwable -> logUpdateOfferError(ticketConfigRequest, throwable))
        .switchIfEmpty(
            Mono.fromCallable(
                () ->
                    PiboxUpdateOfferResponse.newBuilder()
                        .setStatus(PiboxUpdateOfferResponse.PiboxUpdateOfferStatus.NOT_IMPLEMENTED)
                        .build()))
        .subscribe(new GrpcCustomSubscriber<>(observer));
  }

  private OfferDetailsQuery buildOfferDetailsQuery(String provider, String offerStoreId) {
    return OfferDetailsQuery.newBuilder()
        .setOfferStoreId(offerStoreId)
        .setProviderId(provider)
        .build();
  }

  private TicketConfigurationRequest buildTicketConfigurationRequest(
      PiboxUpdateOfferRequest request, String offerStoreId) {
    return TicketConfigurationRequest.newBuilder()
        .setOfferStoreId(offerStoreId)
        .setProviderId(request.getProviderId())
        .build();
  }

  private PiboxGetOfferResponse toPiboxGetOfferResponse(
      BookingOffer bookingOffer, PiboxGetOfferRequest request) {
    /*
     call protobufOfferMapper.toProtobufOffer(bookingOffer, request)
     once BookingOffer is updated to be able to map to Offer
    */
    return PiboxGetOfferResponse.newBuilder()
        .setOffer(Offer.getDefaultInstance())
        .setStatus(PiboxGetOfferResponse.PiboxGetOfferStatus.SUCCESS)
        .build();
  }

  private void logGetOfferError(PiboxGetOfferRequest request, Throwable throwable) {
    log.error(
        "Could not handle getOffer request of offer id [{}] and provider [{}]: {}",
        request.getOfferStoreId(),
        request.getProviderId(),
        throwable);
  }

  private void logUpdateOfferError(TicketConfigurationRequest request, Throwable throwable) {
    log.error(
        "Could not handle updateOffer request of offer id [{}] and provider [{}]: {}",
        request.getOfferStoreId(),
        request.getProviderId(),
        throwable);
  }

  private void logUpdateOfferSuccess(
      TicketConfigurationRequest request, PiboxUpdateOfferResponse response) {
    log.info(
        "Successfully retrieved updateOffer for offer id = [{}] and provider = [{}]. Response: {}",
        request.getOfferStoreId(),
        request.getProviderId(),
        response);
  }

  private void logGetOfferSuccess(PiboxGetOfferResponse response, PiboxGetOfferRequest request) {
    log.info(
        "Successfully retrieved getOffer for offer id = [{}] and provider = [{}]. Response: {}",
        request.getOfferStoreId(),
        request.getProviderId(),
        response);
  }
}
