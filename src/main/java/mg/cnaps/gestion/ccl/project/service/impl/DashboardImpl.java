package mg.cnaps.gestion.ccl.project.service.impl;

import mg.cnaps.gestion.ccl.project.service.DashboardService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class DashboardImpl implements DashboardService {

    public Integer[] getTotalDashboard(Date date1, Date date2, Integer year) {
        return new Integer[0];
    }
}
