package com.bosspvp.api.skills.filters;

import com.bosspvp.api.skills.triggers.TriggerData;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;

public class FilterList {
    @Getter
    private List<FilterBlock<?,?>> list;

    public FilterList(List<FilterBlock<?,?>> filters) {
        this.list = filters.stream().sorted(Comparator.comparingInt(a -> a.getFilter().getRunOrder().getWeight())).toList();
    }
    public boolean isMet(TriggerData data) {
        return list.stream().allMatch(a -> a.isMet(data));
    }
}
