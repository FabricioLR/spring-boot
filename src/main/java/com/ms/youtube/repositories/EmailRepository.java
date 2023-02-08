package com.ms.youtube.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ms.youtube.models.EmailModel;

public interface EmailRepository extends JpaRepository<EmailModel, Long>{}
