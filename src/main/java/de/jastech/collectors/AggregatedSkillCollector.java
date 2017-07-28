package de.jastech.collectors;

import de.jastech.model.AggregatedSkillMapEntry;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * @author Hendrik Stein
 */
public class AggregatedSkillCollector implements Collector<AggregatedSkillMapEntry, AggregatedSkillMapEntry, AggregatedSkillMapEntry> {
    @Override
    public Supplier<AggregatedSkillMapEntry> supplier() {
        return AggregatedSkillMapEntry::new;
    }

    @Override
    public BiConsumer<AggregatedSkillMapEntry, AggregatedSkillMapEntry> accumulator() {
        return (skill1, skill2) -> skill1.aggregate(skill2);
    }

    @Override
    public BinaryOperator<AggregatedSkillMapEntry> combiner() {
        return (skill1, skill2) -> skill1.aggregate(skill2);
    }

    @Override
    public Function<AggregatedSkillMapEntry, AggregatedSkillMapEntry> finisher() {
        return skill -> skill;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.of(Characteristics.UNORDERED);
    }
}
