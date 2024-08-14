package com.feilu.api.common.task;

import com.feilu.api.common.service.RecommendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author dzh
 */
@Slf4j
@Component
public class ScheduledTasks {

    private RecommendService recommendService;

    @Autowired
    public void setRecommendService(RecommendService recommendService) {
        this.recommendService = recommendService;
    }

    @Scheduled(cron = "${scheduled.cron.updateDataModelTaskCron}")
    public void updateDataModelTask() {
        try {
            log.info("UpdateDataModelJob 定时更新dataModel定时任务开始=============:");
            recommendService.buildDataMoDel(null);
            log.info("UpdateDataModelJob 定时更新dataModel定时任务结束=============:");
        } catch (Exception e) {
            log.error("UpdateDataModelJob 定时更新dataModel时报错：{}", e.getMessage(), e);
        }
    }

}
