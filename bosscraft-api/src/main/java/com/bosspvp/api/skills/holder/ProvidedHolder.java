package com.bosspvp.api.skills.holder;

import com.bosspvp.api.BossAPI;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.placeholders.context.PlaceholderContext;
import com.bosspvp.api.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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
    //@TODO make it delegated
    @AllArgsConstructor
    class ProvidedHolderConfig{
        @Getter @NotNull
        private Config config;
        @Getter @NotNull
        private ProvidedHolder holder;

        public double getDoubleFromExpression(String path, PlaceholderContext context) {
            return BossAPI.getInstance().evaluate(
                    config.getString(path),
                    context.withInjectableContext(config).copy(item = holder.getProvider())
            );
        }

        @Nullable
        public String getFormattedStringOrNull(String path, PlaceholderContext context){
            String string = config.getStringOrNull(path);
            if(string==null) return null;
            return StringUtils.formatWithPlaceholders(
                    string,
                    context.withInjectableContext(config).copy(item = holder.getProvider())
            );
        }

        @Nullable
        public List<String> getFormattedStringsOrNull(String path, PlaceholderContext context) {
            List<String> strings = config.getStringListOrNull(path);
            return StringUtils.formatWithPlaceholders(
                    strings,
                    context.withInjectableContext(config).copy(item = holder.getProvider())
            );
        }
    }
}
