package com.lc.springioinit.springinitdemo.jpa;

import com.lc.springioinit.springinitdemo.model.BaseLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoggerJPA extends JpaRepository<BaseLog, Long> {
}
