package com.example.demo.mock;

import com.example.demo.common.service.port.ClockHolder;
import lombok.RequiredArgsConstructor;

import java.time.Clock;

@RequiredArgsConstructor
public class TestClockHolder implements ClockHolder {

    private final long millis;

    @Override
    public long millis() {
        return millis;
    }
}
