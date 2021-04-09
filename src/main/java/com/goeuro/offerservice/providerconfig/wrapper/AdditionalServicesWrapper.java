package com.goeuro.offerservice.providerconfig.wrapper;

import com.goeuro.offerservice.providerconfig.model.AdditionalServicesConfig;
import com.goeuro.offerservice.providerconfig.model.ProviderConfig;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public class AdditionalServicesWrapper {

  private final ProviderConfig providerConfig;

  public boolean isEnabled() {
    return getAdditionalServicesConfig().map(AdditionalServicesConfig::isEnabled).orElse(false);
  }

  private Optional<AdditionalServicesConfig> getAdditionalServicesConfig() {
    return Optional.ofNullable(providerConfig).map(ProviderConfig::getAdditionalServices);
  }
}
