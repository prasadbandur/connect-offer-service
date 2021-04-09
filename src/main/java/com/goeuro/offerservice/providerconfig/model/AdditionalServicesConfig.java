package com.goeuro.offerservice.providerconfig.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalServicesConfig {

  private boolean enabled;
  private List<BaggageItemConfig> baggageItems;
}
