package calc;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.tuple.Pair;
import ru.sberbank.inkass.dto.CashCarDto;
import ru.sberbank.inkass.dto.EdgeDto;
import ru.sberbank.inkass.dto.PointDto;
import util.SideDefinition;

@Data
@Builder
public class BestPointsCalculation {

    private final CashCarDto cashCarDto;
    private List<EdgeDto> fullEdgeDtos;
    private List<EdgeDto> edgeDtos;
    private final SideDefinition sideDefinition;
    private PointDto bankPoint;


    public synchronized List<EdgeDto> getBestWey() {

        PointDto startPoint = cashCarDto.getCurrentPoint();
        Set<PointDto> pointDtos = new HashSet<>();
        EdgeDto bestEdgeDto;

        //удаляем заведомо лишние связи
        edgeDtos = fullEdgeDtos.stream()
                .filter(edge -> (edge.getTo().getSum() < cashCarDto.getCashLimit() - cashCarDto.getCurrentSum() &&
                        getTimeWithTrafficAndService(edge) < cashCarDto.getWorkingDayLength() - cashCarDto.getTimeSpent()))
                .filter(edge -> !CashCarDto.reservedPoints.containsKey(edge.getTo())) //
                .collect(Collectors.toList());

        //находим маршрут к точке с наибольшим весом
        bestEdgeDto = edgeDtos.stream().filter(edge -> edge.getFrom().equals(startPoint))
                .max((e0, e1) -> new WeightComparator().compare(e0, e1)).get();

        //предварительный список точек в которые поедем
        pointDtos.add(startPoint);
        pointDtos.add(bestEdgeDto.getTo());
        getWeightAndEdgeList(bestEdgeDto).getRight().forEach(edge -> {
            pointDtos.add(edge.getTo());
        });
        //???  pointDtos.add(bankPoint);

        //список связей между всеми нужными нам точками
        List<EdgeDto> allNeededEdge = edgeDtos.stream()
                .filter(edge -> pointDtos.contains(edge.getTo()) && pointDtos.contains(edge.getFrom())).collect(Collectors.toList());

        return getEdgesFromPoints(sideDefinition.getSortedPoints(pointDtos, allNeededEdge, bankPoint, bestEdgeDto.getTo()));

    }

    /**
     * Получает сортированный список точек
     * Возвращает сортированный список связей между полученными точками
     */
    private List<EdgeDto> getEdgesFromPoints(List<PointDto> pointDtos) {
        List<EdgeDto> bestEdgeDtos = new ArrayList<>();
        double timeInWeyAndServ = 0;
        for (int i = 0; i <= pointDtos.size() - 3; i++) {
            PointDto curPoint = pointDtos.get(i);
            PointDto nextPoint = pointDtos.get(i + 1);
            EdgeDto edgeDto = getEdge(curPoint, nextPoint);
            EdgeDto edgeIToBank = getEdge(curPoint, pointDtos.get(pointDtos.size() - 1));
            EdgeDto edgeIPlus1ToBank = getEdge(nextPoint, pointDtos.get(pointDtos.size() - 1));

            timeInWeyAndServ += getTimeWithTrafficAndService(edgeDto);
            double timeToBank = getTimeWhithTraffic(edgeIPlus1ToBank);

            if (timeInWeyAndServ >= cashCarDto.getWorkingDayLength() - timeToBank - cashCarDto.getTimeSpent()) { //не укладываемся по времени
                bestEdgeDtos.add(edgeIToBank);
            } else {
                bestEdgeDtos.add(edgeDto);
                CashCarDto.addReservedPoint(cashCarDto, edgeDto.getTo());
            }
        }

        return bestEdgeDtos;
    }

    /**
     * Поиск путей объезда
     */
    public Pair<EdgeDto, EdgeDto> getOptimalWay(EdgeDto edgeDto){
        List<PointDto> nearFromPoint = new ArrayList<>();
        List<PointDto> nearToPoint = new ArrayList<>();
        fullEdgeDtos.stream().filter(edge -> edge.getFrom().equals(edgeDto.getFrom())
                && getTimeWhithTraffic(edge) < getTimeWhithTraffic(edgeDto))
                .forEach(edge -> nearFromPoint.add(edge.getTo()));
        fullEdgeDtos.stream().filter(edge -> edge.getTo().equals(edgeDto.getTo())
                && getTimeWhithTraffic(edge) < getTimeWhithTraffic(edgeDto))
                .forEach(edge -> nearToPoint.add(edge.getTo()));

       PointDto detourPoint = nearFromPoint.stream().filter(point -> nearToPoint.contains(point))
               .filter(point -> getDetourWay(edgeDto, point) < getTimeWhithTraffic(edgeDto))
               .min((p1, p2) -> new WayTimeComparator().compare(getDetourWay(edgeDto, p1), getDetourWay(edgeDto, p2))).get();

       return Pair.of(getEdge(edgeDto.getFrom(), detourPoint), getEdge(detourPoint, edgeDto.getTo()));
    }

    private double getDetourWay(EdgeDto edgeDto, PointDto pointDto){
        return getTimeWhithTraffic(getEdge(edgeDto.getFrom(), pointDto)) +
                getTimeWhithTraffic(getEdge(pointDto, edgeDto.getTo()));
    }

    /**
     * Возвращает пару: вес точки(связи) и список наиболее привлекательных соседних точек(связей)
     */
    private Pair<Double, List<EdgeDto>> getWeightAndEdgeList(EdgeDto edgeDto) {

        double sumCount = cashCarDto.getCurrentSum() + edgeDto.getFrom().getSum();
        double timeCount = cashCarDto.getTimeSpent() + getTimeWithTrafficAndService(edgeDto);

        double weight = getSelfWeight(edgeDto);

        List<EdgeDto> selfEdges = edgeDtos.stream().filter(e -> e.getFrom().equals(edgeDto.getTo()))
                .sorted((e0, e1) -> new SelfWeightComparator().compare(e0, e1))
                .filter(edge ->
                        sumCount + edge.getTo().getSum() < cashCarDto.getCashLimit() &&
                                timeCount + getTimeWithTrafficAndService(edgeDto) < cashCarDto.getWorkingDayLength()
                ).collect(Collectors.toList());

        for (EdgeDto edge : selfEdges) {
            weight += getSelfWeight(edge);
        }

        return Pair.of(weight, selfEdges);
    }

    /**
     * Возвращает точки(связи) без учета соседей
     */
    private double getSelfWeight(EdgeDto edgeDto) {
        double backWayTime = edgeDto.equals(bankPoint) ? getTimeWhithTraffic(edgeDto) :
                getTimeWhithTraffic(getEdge(edgeDto.getTo(), bankPoint));

        return (edgeDto.getTo().getSum() / (cashCarDto.getCashLimit() - cashCarDto.getCurrentSum())) /
                (edgeDto.getWayInfo().getTimeInWay() + backWayTime + edgeDto.getTo().getTimeInPoint());
    }

    /**
     * Возвращает связь по двум точкам
     */
    private EdgeDto getEdge(PointDto from, PointDto to) {
        return edgeDtos.stream()
                .filter(edge -> edge.getFrom().equals(from) && edge.getTo().equals(to))
                .findFirst().get();
    }

    /**
     * Время с учетом пробок и обслуживания
     */
    private double getTimeWithTrafficAndService(EdgeDto edgeDto) {
        return getTimeWhithTraffic(edgeDto) + edgeDto.getTo().getTimeInPoint();
    }

    /**
     * Время с учетом пробок
     */
    private double getTimeWhithTraffic(EdgeDto edgeDto) {
        return edgeDto.getWayInfo().getTimeInWay() * edgeDto.getWayInfo().getTrafficCoef();
    }

    private class SelfWeightComparator implements Comparator<EdgeDto> {
        @Override
        public int compare(EdgeDto e0, EdgeDto e1) {
            if (getSelfWeight(e0) > getSelfWeight(e1)) return 1;
            else if (getSelfWeight(e0) < getSelfWeight(e1)) return -1;
            else return 0;
        }
    }

    private class WeightComparator implements Comparator<EdgeDto> {
        @Override
        public int compare(EdgeDto e0, EdgeDto e1) {
            if (getWeightAndEdgeList(e0).getLeft() > getWeightAndEdgeList(e1).getLeft()) return 1;
            else if (getWeightAndEdgeList(e0).getLeft() < getWeightAndEdgeList(e1).getLeft()) return -1;
            else return 0;
        }
    }

    private class WayTimeComparator implements Comparator<Double> {
        @Override
        public int compare(Double e0, Double e1) {
            if (e0 > e1) return 1;
            else if (e0 < e1) return -1;
            else return 0;
        }
    }

}
