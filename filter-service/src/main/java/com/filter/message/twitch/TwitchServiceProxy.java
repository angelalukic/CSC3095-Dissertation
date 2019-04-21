package com.filter.message.twitch;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name="twitch-service", url="localhost:8082")
public interface TwitchServiceProxy {

}
