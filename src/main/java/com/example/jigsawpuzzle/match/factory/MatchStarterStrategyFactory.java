package com.example.jigsawpuzzle.match.factory;

import com.example.jigsawpuzzle.domain.MatchMode;
import com.example.jigsawpuzzle.match.strategy.MatchStarterStrategy;
import com.example.jigsawpuzzle.match.strategy.OneVsOneMatchStarterStrategy;
import com.example.jigsawpuzzle.match.strategy.TeamMatchStarterStrategy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class MatchStarterStrategyFactory {

    private final Map<MatchMode, MatchStarterStrategy> strategies;

    public MatchStarterStrategyFactory(List<MatchStarterStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(
                        strategy -> getSupportedMode(strategy),
                        Function.identity()
                ));
    }

    private MatchMode getSupportedMode(MatchStarterStrategy strategy) {
        if (strategy instanceof OneVsOneMatchStarterStrategy) return MatchMode.ONEVSONE;
        if (strategy instanceof TeamMatchStarterStrategy) return MatchMode.TEAM;
        throw new UnsupportedOperationException("Unknown strategy type: " + strategy.getClass());
    }

    public MatchStarterStrategy getStrategy(MatchMode mode) {
        return Optional.ofNullable(strategies.get(mode))
                .orElseThrow(() -> new UnsupportedOperationException("Unsupported match mode: " + mode));
    }
}

