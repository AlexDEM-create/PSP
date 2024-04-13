package com.flacko.merchant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, String> {
}
//TODO CRUD REPO + MerchantPOJO + MERCH - интерфейс , int --> long (Любые primary key --> long)
