package com.icceey.jweb.repository;

import com.icceey.jweb.beans.Todo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TodoRepository extends CrudRepository<Todo, Long> {

    List<Todo> findAllByOwnerId(Long ownerId);

    List<Todo> findAllByOwnerIdAndDoneTrue(Long ownerId);

    List<Todo> findAllByOwnerIdAndDoneFalse(Long ownerId);


}
