package com.security.admin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@MappedSuperclass  // This makes it a superclass without its own table in the database
@Getter
@Setter
public class BaseEntity {


    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @PrePersist
    public void prePersist() {
        createdDate = LocalDateTime.now();  // Set the created date before persisting
    }

    @PreUpdate
    public void preUpdate() {
        updatedDate = LocalDateTime.now();  // Set the updated date before updating
    }
}
