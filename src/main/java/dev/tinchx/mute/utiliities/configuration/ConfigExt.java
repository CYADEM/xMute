package dev.tinchx.mute.utiliities.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ConfigExt {

    YML(".yml"),
    JSON(".json"),
    SQL(".sql");

    private String extension;
}
