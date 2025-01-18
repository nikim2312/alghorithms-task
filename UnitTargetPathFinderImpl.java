package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.UnitTargetPathFinder;

import java.util.*;

public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {

    private static final int WIDTH = 27;  // Ширина поля
    private static final int HEIGHT = 21; // Высота поля
    private static final int[] DX = {0, 0, 1, -1}; // Сдвиги по X (вправо, влево, вниз, вверх)
    private static final int[] DY = {1, -1, 0, 0}; // Сдвиги по Y (вверх, вниз, вправо, влево)

    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {
        // Карта, чтобы пометить занятые клетки
        boolean[][] blocked = new boolean[WIDTH][HEIGHT];
        for (Unit unit : existingUnitList) {
            blocked[unit.getxCoordinate()][unit.getyCoordinate()] = true;
        }

        // BFS
        Queue<Edge> queue = new LinkedList<>();
        boolean[][] visited = new boolean[WIDTH][HEIGHT];
        Edge[][] predecessors = new Edge[WIDTH][HEIGHT]; // Для восстановления пути

        // Начинаем с клетки атакующего юнита
        queue.add(new Edge(attackUnit.getxCoordinate(), attackUnit.getyCoordinate()));
        visited[attackUnit.getxCoordinate()][attackUnit.getyCoordinate()] = true;

        while (!queue.isEmpty()) {
            Edge current = queue.poll();
            int x = current.getX();
            int y = current.getY();

            // Если нашли целевой юнит
            if (x == targetUnit.getxCoordinate() && y == targetUnit.getyCoordinate()) {
                return reconstructPath(predecessors, new Edge(x, y));
            }

            // Проверяем все 4 соседние клетки
            for (int i = 0; i < 4; i++) {
                int nx = x + DX[i];
                int ny = y + DY[i];

                // Проверяем, находится ли соседняя клетка в пределах поля и не занята ли она
                if (isValid(nx, ny) && !visited[nx][ny] && !blocked[nx][ny]) {
                    visited[nx][ny] = true;
                    queue.add(new Edge(nx, ny));
                    predecessors[nx][ny] = current; // Сохраняем предшественника
                }
            }
        }

        // Если путь не найден
        return new ArrayList<>();
    }

    private boolean isValid(int x, int y) {
        return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT;
    }

    private List<Edge> reconstructPath(Edge[][] predecessors, Edge target) {
        List<Edge> path = new ArrayList<>();
        for (Edge at = target; at != null; at = predecessors[at.getX()][at.getY()]) {
            path.add(at);
        }
        Collections.reverse(path); // Переворачиваем путь, так как мы шли от цели
        return path;
    }
}