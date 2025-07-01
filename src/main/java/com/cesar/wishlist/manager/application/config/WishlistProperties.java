package com.cesar.wishlist.manager.application.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "wishlist")
@Getter
public class WishlistProperties {

    private final int maxProducts = 20;

}
