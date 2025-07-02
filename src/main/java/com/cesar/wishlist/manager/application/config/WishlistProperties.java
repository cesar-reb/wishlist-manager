package com.cesar.wishlist.manager.application.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "wishlist")
@Getter
@Setter
public class WishlistProperties {

    private int maxProducts;

}
