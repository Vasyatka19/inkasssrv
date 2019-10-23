package util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import ru.sberbank.inkass.dto.EdgeDto;
import ru.sberbank.inkass.dto.PointDto;

public class SideDefinition {
    /**
     * @param a от старта до самой дальней точки
     * @param b от старта до звездной точки
     * @param c от звездной точки до самой дальней
     * @param d от вычисляемой точки до дальней
     * @param e от старта до вычисляемой точки
     * @param f от звездной точки до вычисляемой
     * @return
     */

    private int getSortedPoints(double a, double b, double c, double d, double e, double f) {


        double p1 = (a + b + c) / 2;
        double p2 = (a + d + e) / 2;
        double h1 = 2 * Math.sqrt(p1 * (p1 - a) * (p1 - b) * (p1 - c)) / a;
        double h2 = 2 * Math.sqrt(p2 * (p2 - a) * (p2 - d) * (p2 - e)) / a;
        double s = Math.abs(Math.sqrt(Math.pow(b, 2) - Math.pow(h1, 2)) - Math.sqrt(Math.pow(e, 2) - Math.pow(h2, 2)));
        double g = Math.sqrt(Math.pow(h2, 2) + Math.pow(s, 2));
        double cosinus = (Math.pow(g, 2) + Math.pow(h1, 2) - Math.pow(f, 2)) / (2 * g * h1);
        double len = Math.sqrt(Math.pow(d, 2) - Math.pow(h1, 2));
        if (cosinus > 0) {
            return len > a ? 4 : 1;
        } else return len > a ? 3 : 2;

    }

    private double getDistance(EdgeDto baseToFarPoint, EdgeDto baseToBestPoint, EdgeDto bestToFarPoint, EdgeDto countPointToFarPoint, EdgeDto baseToCountPoint, EdgeDto countPointToBestPoint) {
        int side = getSortedPoints(
                baseToFarPoint.getWayInfo().getTimeInWay(),
                baseToBestPoint.getWayInfo().getTimeInWay(),
                bestToFarPoint.getWayInfo().getTimeInWay(),
                countPointToFarPoint.getWayInfo().getTimeInWay(),
                baseToCountPoint.getWayInfo().getTimeInWay(),
                countPointToBestPoint.getWayInfo().getTimeInWay());
        switch (side) {
            case 1: return baseToCountPoint.getWayInfo().getTimeInWay();
            case 2:
            case 3: return baseToFarPoint.getWayInfo().getTimeInWay() + countPointToFarPoint.getWayInfo().getTimeInWay();
            case 4: return baseToFarPoint.getWayInfo().getTimeInWay() * 4 + baseToCountPoint.getWayInfo().getTimeInWay();
            default: return 0;
        }
    }

    public List<PointDto> getSortedPoints(Set<PointDto> pointDtos, List<EdgeDto> allNeededEdge, PointDto startPopint, PointDto bestPoint) {

        List<PointDto> sortedPointDtoList = new ArrayList<>();
        List<Pair<PointDto, Double>> sortedPointPairs = null;

        EdgeDto startToFar = allNeededEdge.stream().filter(edge -> edge.getFrom().equals(startPopint))
                .max((e0, e1) -> new WayTimeComparator()
                        .compare(e0.getWayInfo().getTimeInWay(), e1.getWayInfo().getTimeInWay())).get();
        EdgeDto startToBest = getEdge(allNeededEdge, startPopint, bestPoint);
        EdgeDto bestToFar = getEdge(allNeededEdge, bestPoint, startToFar.getTo());

        pointDtos.stream().forEach(point -> {
            double distance;
            if (point.equals(startToFar.getTo())) {
                distance = startToFar.getWayInfo().getTimeInWay();
            } else if (point.equals(startToFar.getFrom())) {
                distance = 0;
            } else if (point.equals(bestPoint)) {
                distance = startToBest.getWayInfo().getTimeInWay();
            }else {
                EdgeDto thisPointToFar = getEdge(allNeededEdge, point, startToFar.getTo());
                EdgeDto startToThisPoint = getEdge(allNeededEdge, startToBest.getFrom(), point);
                EdgeDto thisPointToBest = getEdge(allNeededEdge, point, bestPoint);
                distance = getDistance(startToFar, startToBest, bestToFar, thisPointToFar, startToThisPoint, thisPointToBest);
            }

            sortedPointPairs.add(Pair.of(point, distance));
        });


        sortedPointPairs.stream().sorted((e0, e1) ->
                new WayTimeComparator().compare(e0.getRight(), e1.getRight()))
                .forEach(p -> sortedPointDtoList.add(p.getLeft()));

        return sortedPointDtoList;
    }

    private EdgeDto getEdge(List<EdgeDto> edgeDtos, PointDto from, PointDto to){
        return edgeDtos.stream()
                .filter(edge -> edge.getFrom().equals(from) && edge.getTo().equals(to))
                .findFirst().get();
    }


    public static void main(String[] args) {

        System.out.println(Math.random());
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

