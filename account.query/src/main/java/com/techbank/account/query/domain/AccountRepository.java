package com.techbank.account.query.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.techbank.cqrs.core.domain.BaseEntity;

@Repository
public interface AccountRepository extends CrudRepository<BankAccount, String> {
    Optional<BankAccount> findByAccountHolder(String accountHolder);
    List<BaseEntity> findByBalanceGreaterThan(double balance);
    List<BaseEntity> findByBalanceLessThan(double balance);
}
