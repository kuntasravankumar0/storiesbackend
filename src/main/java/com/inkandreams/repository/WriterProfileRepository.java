package com.inkandreams.repository;

import com.inkandreams.entity.WriterProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WriterProfileRepository extends JpaRepository<WriterProfile, Long> {
}
