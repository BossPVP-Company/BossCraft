package com.bosspvp.api.skills.holder.provided;

import com.bosspvp.api.skills.holder.Holder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleProvidedHolder implements ProvidedHolder {
    @Getter
    @NotNull
    private Holder holder;
    @Getter
    @Nullable
    private Object provider = null;

    public SimpleProvidedHolder(@NotNull Holder holder) {
        this.holder = holder;
    }

    @Override
    public int hashCode() {
        return holder.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SimpleProvidedHolder providedHolder)) {
            return false;
        }
        return providedHolder.holder.equals(holder);
    }
}