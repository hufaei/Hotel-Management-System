package com.sz.security.pojo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
@Component
@ConfigurationProperties(prefix = "router")
public class WhitelistProperties {

    private List<String> whitelist;

}
