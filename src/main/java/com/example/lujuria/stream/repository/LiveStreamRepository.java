package com.example.lujuria.stream.repository;

import com.example.lujuria.stream.entity.LiveStream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LiveStreamRepository extends JpaRepository<LiveStream, Long>, JpaSpecificationExecutor<LiveStream> {
}
