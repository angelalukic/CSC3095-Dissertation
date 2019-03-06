package com.bot.twitch.listener;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TwitchListenerRepository extends JpaRepository<TwitchListener, Long> {
}
