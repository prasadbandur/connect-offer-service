package com.goeuro.offerservice.providerconfig.wrapper;

import com.goeuro.offerservice.providerconfig.model.AdditionalServicesConfig;
import com.goeuro.offerservice.providerconfig.model.BaggageItemConfig;
import com.goeuro.offerservice.providerconfig.model.ProviderConfig;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class AdditionalServicesWrapper {

  private final ProviderConfig providerConfig;

  public boolean isEnabled() {
    return getAdditionalServicesConfig().map(AdditionalServicesConfig::isEnabled).orElse(false);
  }

  public List<BaggageItemConfig> getBaggageItems() {
    return getAdditionalServicesConfig()
        .map(AdditionalServicesConfig::getBaggageItems)
        .orElse(Lists.newArrayList());
  }

  private Optional<AdditionalServicesConfig> getAdditionalServicesConfig() {
    return Optional.ofNullable(providerConfig).map(ProviderConfig::getAdditionalServices);
  }
}
