package com.zero.plantory.domain.plantingCalendar.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WateringSchedulerService {
    private final PlantingCalenderService plantingCalenderService;

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void registerWatering() {
        int batch = 1000;
        for (int i = 0; i < 20; i++) {
            int n = plantingCalenderService.registerWatering(batch);
            if (n < batch) break;
        }
    }

    @Scheduled(cron = "0 0 9 * * *", zone = "Asia/Seoul")
    public void sendSMS() {
        int batch = 1000;
        for (int i = 0; i < 20; i++) {
            int n = plantingCalenderService.sendSMS(batch);
            if (n < batch) break;
        }
    }
}
