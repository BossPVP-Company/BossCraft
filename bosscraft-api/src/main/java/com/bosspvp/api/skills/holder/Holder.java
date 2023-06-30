package com.bosspvp.api.skills.holder;

import com.bosspvp.api.skills.conditions.ConditionList;
import com.bosspvp.api.skills.effects.EffectList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public interface Holder {

    String getId();
    ConditionList getConditionList();
    EffectList getEffectList();


    record Template(EffectList effectList, ConditionList conditionList){

        public Holder toHolder(String id){
            return new Holder() {
                @Override
                public String getId() {
                    return id;
                }

                @Override
                public ConditionList getConditionList() {
                    return conditionList;
                }

                @Override
                public EffectList getEffectList() {
                    return effectList;
                }

                @Override
                public boolean equals(Object obj) {
                    if (obj instanceof Holder){
                        return ((Holder) obj).getId().equals(this.getId());
                    }
                    return false;
                }

                @Override
                public int hashCode() {
                    return this.getId().hashCode();
                }
            };
        }
    }
}
