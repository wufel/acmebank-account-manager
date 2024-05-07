package com.wufel.acmebank.repository;

import com.wufel.acmebank.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {

}
