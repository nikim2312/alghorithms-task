package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {
    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        List<Unit> suitableUnits = new ArrayList<>();

        // Если атакуют юниты компьютера, то ищем юнитов игрока, не закрытых справа
        if (isLeftArmyTarget) {
            for (int row = 0; row < unitsByRow.size(); row++) {
                List<Unit> rowUnits = unitsByRow.get(row);
                boolean isBlocked = false;

                // Перебираем юнитов в ряду с конца (справа налево)
                for (int i = rowUnits.size() - 1; i >= 0; i--) {
                    Unit unit = rowUnits.get(i);
                    if (unit.isAlive()) {
                        // Если этот юнит жив, то он потенциальная цель для атаки
                        if (!isBlocked) {
                            suitableUnits.add(unit);
                        }
                        // Закрывает ли этот юнит следующие юниты?
                        isBlocked = true;
                    }
                }
            }
        }
        // Если атакуют юниты игрока, то ищем юнитов компьютера, не закрытых слева
        else {
            for (int row = 0; row < unitsByRow.size(); row++) {
                List<Unit> rowUnits = unitsByRow.get(row);
                boolean isBlocked = false;

                // Перебираем юнитов в ряду с начала (слева направо)
                for (int i = 0; i < rowUnits.size(); i++) {
                    Unit unit = rowUnits.get(i);
                    if (unit.isAlive()) {
                        // Если этот юнит жив, то он потенциальная цель для атаки
                        if (!isBlocked) {
                            suitableUnits.add(unit);
                        }
                        // Закрывает ли этот юнит следующие юниты?
                        isBlocked = true;
                    }
                }
            }
        }

        return suitableUnits;
    }
}