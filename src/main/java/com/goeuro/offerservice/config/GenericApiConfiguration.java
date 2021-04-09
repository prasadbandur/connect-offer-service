package com.goeuro.offerservice.config;

import com.goeuro.offerservice.adapter.CachingGenericApiClientProvider;
import com.goeuro.offerservice.adapter.GenericApiClientFactory;
import com.goeuro.offerservice.adapter.GenericApiClientProvider;
import com.goeuro.offerservice.providerconfig.service.ProviderConfigService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GenericApiConfiguration {

  @Bean
  public GenericApiClientProvider genericApiClientProvider(
      ProviderConfigService providerConfigService) {
    return new CachingGenericApiClientProvider(new GenericApiClientFactory(providerConfigService));
  }
}
