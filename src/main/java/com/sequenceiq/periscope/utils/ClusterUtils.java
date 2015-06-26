package com.sequenceiq.periscope.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.yarn.server.resourcemanager.webapp.dao.CapacitySchedulerInfo;
import org.apache.hadoop.yarn.server.resourcemanager.webapp.dao.CapacitySchedulerQueueInfo;
import org.apache.hadoop.yarn.server.resourcemanager.webapp.dao.CapacitySchedulerQueueInfoList;
import org.apache.hadoop.yarn.server.resourcemanager.webapp.dao.ClusterMetricsInfo;
import org.apache.hadoop.yarn.server.resourcemanager.webapp.dao.SchedulerInfo;

import com.sequenceiq.ambari.client.AmbariClient;

public final class ClusterUtils {

    public static final DecimalFormat TIME_FORMAT = new DecimalFormat("##.##");
    public static final int MAX_CAPACITY = 100;
    public static final int MIN_IN_MS = 1000 * 60;

    private ClusterUtils() {
        throw new IllegalStateException();
    }

    public static double computeFreeClusterResourceRate(ClusterMetricsInfo metrics) {
        return (double) metrics.getAvailableMB() / (double) metrics.getTotalMB();
    }

    public static List<CapacitySchedulerQueueInfo> getAllQueueInfo(SchedulerInfo schedulerInfo) {
        List<CapacitySchedulerQueueInfo> queueInfoList = new ArrayList<>();
        if (schedulerInfo instanceof CapacitySchedulerInfo) {
            addQueueInfo(queueInfoList, ((CapacitySchedulerInfo) schedulerInfo).getQueues());
        }
        return queueInfoList;
    }

    private static void addQueueInfo(List<CapacitySchedulerQueueInfo> queueInfoList, CapacitySchedulerQueueInfoList queues) {
        if (queues != null && queues.getQueueInfoList() != null) {
            for (CapacitySchedulerQueueInfo info : queues.getQueueInfoList()) {
                queueInfoList.add(info);
                addQueueInfo(queueInfoList, info.getQueues());
            }
        }
    }

    public static int getTotalNodes(AmbariClient ambariClient) {
        return ambariClient.getClusterHosts().size();
    }
}
