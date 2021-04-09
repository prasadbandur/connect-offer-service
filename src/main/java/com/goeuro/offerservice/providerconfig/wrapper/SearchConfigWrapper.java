package com.goeuro.offerservice.providerconfig.wrapper;

import com.goeuro.offerservice.providerconfig.model.CarrierConfig;
import com.goeuro.offerservice.providerconfig.model.ProviderConfig;
import com.goeuro.offerservice.providerconfig.model.SearchConfig;
import lombok.Getter;

import java.util.Optional;

import static com.goeuro.offerservice.providerconfig.wrapper.CarriersConfigWrapper.getCarrierConfig;

public class SearchConfigWrapper {

  private final ProviderConfig providerConfig;

  @Getter private final AuthConfigWrapper auth;

  SearchConfigWrapper(ProviderConfig providerConfig) {
    this.providerConfig = providerConfig;

    auth = new AuthConfigWrapper(providerConfig);
  }

  public boolean isPassengerData() {
    return getSearchConfig().map(SearchConfig::isPassengerData).orElse(false);
  }

  public boolean isMobileTicket() {
    return getSearchConfig().map(SearchConfig::getMobileTicket).orElse(false);
  }

  public boolean isMobileTicket(String carrierCode) {
    return getCarrierConfig(providerConfig, carrierCode)
        .map(CarrierConfig::getSearch)
        .map(SearchConfig::getMobileTicket)
        .orElse(isMobileTicket());
  }

  private Optional<SearchConfig> getSearchConfig() {
    return Optional.ofNullable(providerConfig).map(ProviderConfig::getSearch);
  }

  // TODO
  //  public boolean isMobileTicket(Journey journey) {
  //    return isMobileTicket(extractCarrierCodes(journey));
  //  }

}
