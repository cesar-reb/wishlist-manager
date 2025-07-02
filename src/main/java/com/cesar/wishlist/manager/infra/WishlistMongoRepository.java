package com.cesar.wishlist.manager.infra;

import com.cesar.wishlist.manager.domain.entity.Wishlist;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WishlistMongoRepository extends MongoRepository<Wishlist, String> {
}
