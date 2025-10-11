package mg.cnaps.gestion.ccl.project.util;

import mg.cnaps.gestion.ccl.project.entity.Infrastructure;

import java.util.List;

public class PageUtil<T> {
    public List<T> paginateList(int page, int pageSize, List<T> allFiltered) {
        int total = allFiltered.size();
        int start = page * pageSize;

        if (start >= total) {
            start = 0;
        }

        int end = Math.min(start + pageSize, total);
        return allFiltered.subList(start, end);
    }

}
