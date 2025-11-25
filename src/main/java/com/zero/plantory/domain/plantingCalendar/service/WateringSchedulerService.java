package com.zero.plantory.domain.plantingCalendar.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WateringSchedulerService {
    private final PlantingCalenderServiceImpl plantingCalenderService;

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void tick() {
        System.out.println("WateringSchedulerService.tick");
        int batch = 500;
        for (int i = 0; i < 20; i++) {
            int n = plantingCalenderService.processOnce(batch);
            if (n < batch) break;
        }
    }
}
