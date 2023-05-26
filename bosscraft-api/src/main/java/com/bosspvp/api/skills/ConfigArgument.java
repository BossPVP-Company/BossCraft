package com.bosspvp.api.skills;

import com.bosspvp.api.skills.violation.ConfigViolation;
import com.bosspvp.api.tuples.Pair;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public interface ConfigArgument {
    List<ConfigViolation> test(@NotNull ConfigurationSection config);



    class RequiredArgument<T> implements ConfigArgument {
        private List<String> names;
        private String message;
        private Function<Pair<ConfigurationSection,String>,T> getter;
        private Predicate<T> predicate;

        public RequiredArgument(@NotNull List<String> names,
                                @NotNull String message,
                                @NotNull Function<Pair<ConfigurationSection,String>,T> getter,
                                @NotNull Predicate<T> predicate){
            this.names = names;
            this.message = message;
            this.getter = getter;
            this.predicate = predicate;
        }

        @Override
        public List<ConfigViolation> test(@NotNull ConfigurationSection config) {
            for (String name : names) {
                var value = getter.apply(new Pair<>(config,name));
                if (config.contains(name) && predicate.test(value)) {
                    return new ArrayList<>();
                }
            }
            return List.of(new ConfigViolation(names.get(0), message));
        }
    }
    class InheritedArguments implements ConfigArgument{
        private Function<ConfigurationSection,Compilable<?>> getter;
        private String subsection;
        public InheritedArguments(@NotNull Function<ConfigurationSection,Compilable<?>> getter,
                                  @Nullable String subsection){
            this.getter = getter;
            this.subsection = subsection;
        }
        @Override
        public List<ConfigViolation> test(@NotNull ConfigurationSection config) {

            var section = Optional.ofNullable(config.getConfigurationSection(subsection)).orElse(config);
            var compilable = getter.apply(section);


            if(compilable == null) return new ArrayList<>();
            return compilable.getArguments().test(section);
        }
    }

    class Arguments{
        private List<ConfigArgument> list;
        public Arguments(@NotNull List<ConfigArgument> list){
            this.list = list;
        }
        public List<ConfigViolation> test(@NotNull ConfigurationSection config){
            return list.stream().flatMap(it->it.test(config).stream()).collect(Collectors.toList());
        }
    }
}
