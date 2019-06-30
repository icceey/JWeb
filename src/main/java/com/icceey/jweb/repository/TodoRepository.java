package com.icceey.jweb.repository;

import com.icceey.jweb.beans.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface TodoRepository extends CrudRepository<Todo, Long> {

    Page<Todo> findAllByOwnerId(Long ownerId, Pageable pageable);

    Page<Todo> findAllByOwnerIdAndDoneTrue(Long ownerId, Pageable pageable);

    Page<Todo> findAllByOwnerIdAndDoneFalse(Long ownerId, Pageable pageable);


}
