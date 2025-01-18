package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.*;

public class GeneratePresetImpl implements GeneratePreset {

    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {
        Army army = new Army();
        ArrayList<Unit> unitsToAddList = new ArrayList<>();
        int remainingPoints = maxPoints;

        // Сортировка юнитов по эффективности: (атака + здоровье) / стоимость
        unitList.sort((unit1, unit2) -> {
            double efficiency1 = (unit1.getBaseAttack() + unit1.getHealth()) / (double) unit1.getCost();
            double efficiency2 = (unit2.getBaseAttack() + unit2.getHealth()) / (double) unit2.getCost();
            return Double.compare(efficiency2, efficiency1); // Сортировка по убыванию
        });

        // Проходим по списку юнитов и добавляем их в армию
        for (Unit unit : unitList) {
            // Вычисляем максимальное количество юнитов, которые можем добавить с учетом оставшихся очков
            int unitsToAdd = Math.min(11, remainingPoints / unit.getCost());

            // Добавляем юнитов в список
            for (int i = 0; i < unitsToAdd; i++) {
                unitsToAddList.add(unit); // Добавляем юнит в список
                remainingPoints -= unit.getCost(); // Уменьшаем оставшиеся очки
            }

            // Если очки исчерпаны, прекращаем добавление юнитов
            if (remainingPoints <= 0) {
                break;
            }
        }

        // Устанавливаем список юнитов в армию и обновляем очки
        army.setUnits(unitsToAddList);
        army.setPoints(maxPoints - remainingPoints); // Обновляем количество использованных очков

        return army; // Возвращаем армию с добавленными юнитами
    }
}