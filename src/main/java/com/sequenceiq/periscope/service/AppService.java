package com.sequenceiq.periscope.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.api.records.ApplicationReport;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sequenceiq.periscope.domain.Cluster;
import com.sequenceiq.periscope.domain.PeriscopeUser;
import com.sequenceiq.periscope.model.Priority;
import com.sequenceiq.periscope.model.SchedulerApplication;

@Service
public class AppService {

    @Autowired
    private ClusterService clusterService;

    public List<ApplicationReport> getApplicationReports(PeriscopeUser user, long clusterId)
            throws IOException, YarnException, ClusterNotFoundException {
        Cluster cluster = clusterService.get(user, clusterId);
        YarnClient yarnClient = cluster.getYarnClient();
        return yarnClient.getApplications();
    }

    public void setPriorityToHighRandomly(PeriscopeUser user, long clusterId) throws ClusterNotFoundException {
        Cluster cluster = clusterService.get(user, clusterId);
        Map<ApplicationId, SchedulerApplication> applications = cluster.getApplications(Priority.NORMAL);
        int i = 0;
        for (ApplicationId applicationId : applications.keySet()) {
            if (i++ % 2 == 0) {
                cluster.setApplicationPriority(applicationId, Priority.HIGH);
            }
        }
    }

}
