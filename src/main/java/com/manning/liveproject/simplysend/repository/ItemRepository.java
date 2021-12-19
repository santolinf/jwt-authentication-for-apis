package com.manning.liveproject.simplysend.repository;

import com.manning.liveproject.simplysend.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Page<Item> findItemsByTypeIsLike(String type, Pageable pageable);
}
