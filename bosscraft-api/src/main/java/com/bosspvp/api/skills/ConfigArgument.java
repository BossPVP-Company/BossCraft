package com.bosspvp.api.skills;

import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.violation.ConfigViolation;
import com.bosspvp.api.tuples.PairRecord;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public interface ConfigArgument {
    @NotNull List<ConfigViolation> test(@NotNull Config config);

    class ConfigArgumentsBuilder {
        private List<ConfigArgument> arguments = new ArrayList<>();

        public void require(@NotNull String name, @NotNull String message) {
            require(List.of(name), message);
        }

        public <T> void require(
                @NotNull String name,
                @NotNull String message,
                @NotNull Function<PairRecord<Config,String>,T> getter,
                @NotNull Predicate<T> predicate) {

            require(List.of(name), message, getter, predicate);
        }

        public <T> void require(@NotNull List<String> names, @NotNull String message) {
            require(names, message, entry -> (T)entry.first().get(entry.second()), (it->true));
        }

        public <T> void require(
                @NotNull List<String> names,
                @NotNull String message,
                @NotNull Function<PairRecord<Config,String>,T> getter,
                @NotNull Predicate<T> predicate) {

            arguments.add(new RequiredArgument<T>(names, message, getter, predicate));
        }

        public void inherit(@NotNull Function<Config, Compilable<?>> getter) {
            arguments.add(new InheritedArguments(getter));
        }

        public void inherit(@NotNull String subsection,
                            @NotNull Function<Config, Compilable<?>> getter) {
            arguments.add(new InheritedArguments(getter, subsection));
        }

        @NotNull
        public Arguments build(){
           return new Arguments(arguments);
        }
    }

    class RequiredArgument<T> implements ConfigArgument {
        private List<String> names;
        private String message;
        private Function<PairRecord<Config,String>,T> getter;
        private Predicate<T> predicate;

        public RequiredArgument(@NotNull List<String> names,
                                @NotNull String message,
                                @NotNull Function<PairRecord<Config,String>,T> getter,
                                @NotNull Predicate<T> predicate){
            this.names = names;
            this.message = message;
            this.getter = getter;
            this.predicate = predicate;
        }

        @Override
        public @NotNull List<ConfigViolation> test(@NotNull Config config) {
            for (String name : names) {
                var value = getter.apply(new PairRecord<>(config,name));
                if (config.hasPath(name) && predicate.test(value)) {
                    return new ArrayList<>();
                }
            }
            return List.of(new ConfigViolation(names.get(0), message));
        }
    }
    class InheritedArguments implements ConfigArgument{
        private Function<Config,Compilable<?>> getter;
        private String subsection;
        public InheritedArguments(@NotNull Function<Config,Compilable<?>> getter){
            this(getter,null);
        }
        public InheritedArguments(@NotNull Function<Config,Compilable<?>> getter,
                                  @Nullable String subsection){
            this.getter = getter;
            this.subsection = subsection;
        }
        @Override
        public @NotNull List<ConfigViolation> test(@NotNull Config config) {

            var section = Optional.ofNullable(config.getSubsection(subsection)).orElse(config);
            var compilable = getter.apply(section);


            if(compilable == null) return new ArrayList<>();
            return compilable.getArguments().test(section);
        }
    }

    class Arguments{
        private final List<ConfigArgument> list;
        public Arguments(@NotNull List<ConfigArgument> list){
            this.list = list;
        }
        public List<ConfigViolation> test(@NotNull Config config){
            return list.stream().flatMap(it->it.test(config).stream()).collect(Collectors.toList());
        }
    }
}
