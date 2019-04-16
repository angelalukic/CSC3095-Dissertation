package com.bot.discord.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscordRoleRepository extends JpaRepository<DiscordRole, Long> {

}
