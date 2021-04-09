package com.goeuro.offerservice.providerconfig.wrapper;

import com.goeuro.offerservice.providerconfig.model.CountryWhitelistConfig;
import com.goeuro.offerservice.providerconfig.model.ProviderConfig;
import com.goeuro.offerservice.providerconfig.model.SearchConfig;
import com.google.common.annotations.VisibleForTesting;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

@RequiredArgsConstructor
public class CountriesWhitelistConfigWrapper {

  private final ProviderConfig providerConfig;
}
