package com.auto1.testdatastorage.repository;

import com.auto1.testdatastorage.domain.TypeOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TypeOwnersRepository
        extends JpaRepository<TypeOwner, Long>, JpaSpecificationExecutor<TypeOwner> {

    Optional<TypeOwner> findByDataType(String dataType);

}
