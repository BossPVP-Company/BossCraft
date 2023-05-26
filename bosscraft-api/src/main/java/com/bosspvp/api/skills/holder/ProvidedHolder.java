package com.bosspvp.api.skills.holder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ProvidedHolder {
    @NotNull Holder getHolder();
    @Nullable Object getProvider();


    @AllArgsConstructor
    class SimpleProvidedHolder implements ProvidedHolder{
        @Getter
        @NotNull
        private Holder holder;
        @Getter @Nullable
        private Object provider;

        @Override
        public int hashCode() {
            return holder.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof SimpleProvidedHolder providedHolder)){
                return false;
            }
            return providedHolder.holder.equals(holder);
        }
    }
    //@TODO
    @AllArgsConstructor
    class ProvidedHolderConfig implements ProvidedHolder{
        @Getter
        @NotNull
        private Holder holder;
        @Getter @Nullable
        private Object provider;

        @Override
        public int hashCode() {
            return holder.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof SimpleProvidedHolder providedHolder)){
                return false;
            }
            return providedHolder.holder.equals(holder);
        }
    }
}
