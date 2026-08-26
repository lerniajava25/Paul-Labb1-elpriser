package org.example;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ElectricityPrice(
        @JsonProperty("SEK_per_kWh") double sekPerKwh,
        @JsonProperty("EUR_per_kWh") double eurPerKwh,
        @JsonProperty("EXR") double exr,
        @JsonProperty("time_start") String timeStart,
        @JsonProperty("time_end") String timeEnd
) {
}