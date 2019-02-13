package com.bot.twitter.listener;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TwitterListenerRepository extends JpaRepository<TwitterListener, Long> {

}
