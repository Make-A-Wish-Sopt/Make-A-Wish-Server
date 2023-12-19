package com.sopterm.makeawish.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "slackWishClient", url = "${slack.wish-url}")
public interface SlackWishClient {
    @PostMapping(value = "", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    void postWishMessage(@RequestBody String request);
}
