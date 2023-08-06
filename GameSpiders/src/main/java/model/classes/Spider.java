package model.classes;

import core.Coordinates;
import core.PlayField;
import core.DirectionsManager;
import core.TCell;
import javafx.scene.control.TableView;
import model.enums.Character;
import model.enums.MovingDirection;
import model.enums.RoutePassing;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static utils.UtilGeneral.moveCharacter;
import static core.Coordinates.calculateCoordinatesOfNextCell;
import static core.Coordinates.coordinatesArePermitted;

public class Spider {
    private final Coordinates[] routeCoordinates;
    private final Coordinates[] boundaryCoordinates;
    private final RoutePassing routePassing;
    private final Player player;
    private final PlayField playField;
    private final TableView<TCell[]> table;

    public Spider(Coordinates[] routeCoordinates, Coordinates[] boundaryCoordinates, RoutePassing routePassing, Player player, PlayField playField, TableView<TCell[]> table) {
        this.routeCoordinates = routeCoordinates;
        this.boundaryCoordinates = boundaryCoordinates;
        this.routePassing = routePassing;
        this.player = player;
        this.playField = playField;
        this.table = table;
    }

    public void launchSpider() {
        new Thread(this::spiderIsRunning).start();
    }

    private void spiderIsRunning() {
        Coordinates lastCoordinates = new Coordinates();
        spiderIsRunningAlongBoundary(lastCoordinates);
        spiderIsHuntingPlayer(lastCoordinates);
    }

    private boolean boundaryIsComplete() {
        boolean isComplete = true;

        for (Coordinates cellCoordinates: boundaryCoordinates) {
            if (playField.getCharacter(cellCoordinates.get(1), cellCoordinates.get(2)) == Character.Empty) {
                isComplete = false;
                break;
            }
        }

        return isComplete;
    }

    private void moveSpider(Coordinates oldCoordinates, Coordinates newCoordinates) {
        var characterObjects = new HashMap<String, Object>();
        characterObjects.put("Player", player);

        moveCharacter(Character.Spider, null, oldCoordinates, newCoordinates, playField, table, characterObjects);
    }

    private void spiderIsRunningAlongBoundary(Coordinates lastCoordinates) {
        Coordinates prevCoordinates = null;
        Coordinates currCoordinates = null;
        int currIndex = 0;
        int routeLength = routeCoordinates.length;
        boolean forwardDirection = true;

        while (boundaryIsComplete() && !player.isKilled() && !player.isWon()) {
            currCoordinates = routeCoordinates[currIndex];
            moveSpider(prevCoordinates, currCoordinates);

            if (forwardDirection) {
                if (currIndex < routeLength - 1) {
                    currIndex++;
                } else {
                    if (routePassing == RoutePassing.ToTheEndAndBack) {
                        currIndex--;
                        forwardDirection = false;
                    } else {
                        currIndex = 0;
                    }
                }
            } else {
                if (currIndex > 0) {
                    currIndex--;
                } else {
                    currIndex++;
                    forwardDirection = true;
                }
            }

            prevCoordinates = currCoordinates;
        }
        lastCoordinates.setCoordinate(currCoordinates != null ? currCoordinates.get(1) : routeCoordinates[0].get(1), 1);
        lastCoordinates.setCoordinate(currCoordinates != null ? currCoordinates.get(2) : routeCoordinates[0].get(2), 2);
    }

    private void spiderIsHuntingPlayer(Coordinates initialCoordinates) {
        Coordinates prevCoordinates = initialCoordinates;
        Coordinates currCoordinates;

        while (!player.isKilled() && !player.isWon()) {
            currCoordinates = calculateNewCoordinates(prevCoordinates);
            moveSpider(prevCoordinates, currCoordinates);

            prevCoordinates = currCoordinates;
        }
    }

    private Coordinates calculateNewCoordinates(Coordinates spiderCoordinates) {
        List<Coordinates> route = new ArrayList<>();
        buildRoute(route, spiderCoordinates, player.getCoordinates());
        return route.get(1);
    }

    private void buildRoute(List<Coordinates> route, Coordinates spiderCoordinates, Coordinates playerCoordinates) {
        var directionsManager = new DirectionsManager(spiderCoordinates, playerCoordinates);
        continueRoute(spiderCoordinates, playerCoordinates, spiderCoordinates, route, directionsManager, new ArrayList<>());
    }

    private void continueRoute(Coordinates startCoordinates, Coordinates finishCoordinates, Coordinates currentSpiderCoordinates,
                               List<Coordinates> route, DirectionsManager directionsManager, List<Coordinates> cyclicPathCells) {
        MovingDirection direction;
        boolean directionFound;
        Coordinates coordinates;

        if (startCoordinates.equals(finishCoordinates)) {
            return;
        }

        if (directionsManager.getActualDirection() != null) {
            if (directionsManager.getActualDirectionDiff() <= 0) {
                var lastRouteCoordinates = route.get(route.size()-1);
                var nextActualDirectionCoordinates = calculateCoordinatesOfNextCell(lastRouteCoordinates, directionsManager.getActualDirection());
                boolean nextActualDirectionCoordinatesArePermitted = coordinatesArePermitted(nextActualDirectionCoordinates, currentSpiderCoordinates, cyclicPathCells, playField);

                direction = directionsManager.getNextInPriorityDirection(route, finishCoordinates, nextActualDirectionCoordinatesArePermitted);
            } else if (directionsManager.actualDirectionIsContra()) {
                direction = directionsManager.getNextInPriorityDirection(route, finishCoordinates, null);
            } else {
                direction = directionsManager.getActualDirection();
            }

            directionFound = false;
            do {
                coordinates = calculateCoordinatesOfNextCell(startCoordinates, direction);
                if (coordinatesArePermitted(coordinates, currentSpiderCoordinates, cyclicPathCells, playField)) {
                    if (coordinates.equals(currentSpiderCoordinates)) {
                        addToCyclicPathCells(cyclicPathCells, route);
                        route.clear();
                    }

                    route.add(coordinates);

                    if (direction != directionsManager.getActualDirection()) {
                        directionsManager.setActualDirection(direction);
                    }

                    directionsManager.changeActualDirectionDiff();
                    directionsManager.clearFailedDirections();
                    directionFound = true;
                } else {
                    directionsManager.setTestedDirection(direction);
                    directionsManager.addFailedDirection(direction);
                    direction = directionsManager.getNextInPriorityDirection(route, finishCoordinates, null);
                }
            } while (!directionFound);

            continueRoute(coordinates, finishCoordinates, currentSpiderCoordinates, route, directionsManager, cyclicPathCells);
        } else {
            route.add(startCoordinates);

            Coordinates hCoordinates = calculateCoordinatesOfNextCell(startCoordinates, directionsManager.getHorizontalDirection());
            Coordinates vCoordinates = calculateCoordinatesOfNextCell(startCoordinates, directionsManager.getVerticalDirection());
            Coordinates vcCoordinates = calculateCoordinatesOfNextCell(startCoordinates, directionsManager.getVerticalContraDirection());
            Coordinates hcCoordinates = calculateCoordinatesOfNextCell(startCoordinates, directionsManager.getHorizontalContraDirection());
            Coordinates aCoordinates = new Coordinates();

            direction = directionsManager.chooseFirstDirection(hCoordinates, vCoordinates, vcCoordinates, hcCoordinates, aCoordinates, finishCoordinates, playField);

            route.add(aCoordinates);
            directionsManager.setActualDirection(direction);
            directionsManager.changeActualDirectionDiff();
            continueRoute(aCoordinates, finishCoordinates, currentSpiderCoordinates, route, directionsManager, cyclicPathCells);
        }
    }

    private void addToCyclicPathCells(List<Coordinates> cyclicPathCells, List<Coordinates> route) {
        for (Coordinates item : route) {
            if (!cyclicPathCells.contains(item)) {
                cyclicPathCells.add(item);
            }
        }
    }
}
