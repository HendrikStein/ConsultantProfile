package de.jastech.collectors;

import de.jastech.model.AggregatedIndustryMapEntry;

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
public class AggregatedIndustryCollector implements Collector<AggregatedIndustryMapEntry, AggregatedIndustryMapEntry, AggregatedIndustryMapEntry> {
    @Override
    public Supplier<AggregatedIndustryMapEntry> supplier() {
        return AggregatedIndustryMapEntry::new;
    }

    @Override
    public BiConsumer<AggregatedIndustryMapEntry, AggregatedIndustryMapEntry> accumulator() {
        return (skill1, skill2) -> skill1.aggregate(skill2);
    }

    @Override
    public BinaryOperator<AggregatedIndustryMapEntry> combiner() {
        return (skill1, skill2) -> skill1.aggregate(skill2);
    }

    @Override
    public Function<AggregatedIndustryMapEntry, AggregatedIndustryMapEntry> finisher() {
        return skill -> skill;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.of(Characteristics.UNORDERED);
    }
}
