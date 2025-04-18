package project.collectors_hub.repository

import org.springframework.data.jpa.repository.JpaRepository
import project.collectors_hub.entity.Item

interface ItemRepository : JpaRepository<Item, Long>