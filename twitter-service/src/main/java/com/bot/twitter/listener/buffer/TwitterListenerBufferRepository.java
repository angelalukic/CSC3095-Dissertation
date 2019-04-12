package com.bot.twitter.listener.buffer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TwitterListenerBufferRepository extends JpaRepository<TwitterListenerBuffer, Long> {
}
