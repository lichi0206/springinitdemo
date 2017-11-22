package com.lc.springioinit.springinitdemo.jpa;

import com.lc.springioinit.springinitdemo.model.GoodEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface GoodJPA extends JpaRepository<GoodEntity, Long>, QueryDslPredicateExecutor<GoodEntity> {
}
