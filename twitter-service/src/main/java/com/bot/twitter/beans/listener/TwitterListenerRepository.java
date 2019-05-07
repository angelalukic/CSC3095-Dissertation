package com.bot.twitter.beans.listener;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TwitterListenerRepository extends JpaRepository<TwitterListener, Long> {
}
