package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;
import com.battle.heroes.army.Unit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SimulateBattleImpl implements SimulateBattle {
    private PrintBattleLog printBattleLog; // Позволяет логировать. Использовать после каждой атаки юнита

    @Override
    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {
        List<Unit> playerUnits = new ArrayList<>(playerArmy.getUnits());
        List<Unit> computerUnits = new ArrayList<>(computerArmy.getUnits());
        boolean battleOngoing = true;

        // Пока обе армии имеют живых юнитов
        while (battleOngoing) {
            // Сортировка юнитов по убыванию атаки
            sortUnitsByAttack(playerUnits);
            sortUnitsByAttack(computerUnits);

            // Утверждаем, что бой продолжается
            battleOngoing = false;

            // Процесс атаки
            Iterator<Unit> playerIterator = playerUnits.iterator();
            Iterator<Unit> computerIterator = computerUnits.iterator();

            // Обрабатываем ходы игроков
            while (playerIterator.hasNext()) {
                Unit playerUnit = playerIterator.next();
                if (!playerUnit.isAlive()) {
                    continue;
                }

                // Получаем цель для атаки игрока
                Unit target = playerUnit.getProgram().attack();
                if (target != null && target.isAlive()) {
                    // Атакуем цель
                    printBattleLog(playerUnit, target);
                    target.setAlive(false);  // Цель умирает
                    battleOngoing = true;
                }
            }

            // Обрабатываем ходы компьютера
            while (computerIterator.hasNext()) {
                Unit computerUnit = computerIterator.next();
                if (!computerUnit.isAlive()) {
                    continue;
                }

                // Получаем цель для атаки компьютера
                Unit target = computerUnit.getProgram().attack();
                if (target != null && target.isAlive()) {
                    // Атакуем цель
                    printBattleLog(computerUnit, target);
                    target.setAlive(false);  // Цель умирает
                    battleOngoing = true;
                }
            }

            // Очищаем мертвых юнитов из очередей
            playerUnits.removeIf(unit -> !unit.isAlive());
            computerUnits.removeIf(unit -> !unit.isAlive());
        }
    }

    // Метод сортировки юнитов по убыванию атаки
    private void sortUnitsByAttack(List<Unit> units) {
        units.sort((unit1, unit2) -> Integer.compare(unit2.getBaseAttack(), unit1.getBaseAttack()));
    }

    // Метод для вывода лога битвы
    private void printBattleLog(Unit unit, Unit target) {
        // Реализация вывода лога
        System.out.println(unit.getName() + " атакует " + target.getName());
    }
}