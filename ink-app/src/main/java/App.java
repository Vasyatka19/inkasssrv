import java.util.List;

import calc.BestPointsCalculation;
import ru.sberbank.inkass.dto.*;
import ru.sberbank.inkass.property.StartPropertyDto;
import ru.sberbank.service.fill.FillGraphService;
import ru.sberbank.service.fill.FillGraphServiceImpl;
import ru.sberbank.service.saver.BestWayContainer;
import ru.sberbank.service.saver.WaySaverService;
import ru.sberbank.service.saver.WaySaverServiceImpl;
import util.SideDefinition;


public class App {

    static StartPropertyDto startPropertyDto;

    public static void main(String[] args) {

        startPropertyDto = StartPropertyDto.builder()
                .graphSize(500)
                .workingDayCount(1)
                .workingDayLength(8800)
                .maxSumInPoint(10000)
                .maxTimeInPoint(200)
                .maxMoneyInAnt(400)
                .antCount(5)
                .maxTimeInWay(2000)
                .build();


        GraphDto fill = getFill(startPropertyDto);
        //получение точки старта
        PointDto bankPoint = fill.getEdgeDtos().stream()
                .filter(edgeDto -> edgeDto.getFrom().isBase()).findFirst().get().getFrom();


        for (int i = 0; i < startPropertyDto.getAntCount(); i++) {
            Runnable runnable = () -> process(fill, bankPoint);
            Thread thread = new Thread(runnable);
            thread.start();
        }

    }

    private static GraphDto getFill(StartPropertyDto startPropertyDto) {
        FillGraphService fillGraphService = new FillGraphServiceImpl(startPropertyDto);
        return fillGraphService.fill(startPropertyDto.getGraphSize());
    }

    private static void process(GraphDto fill, PointDto bankPoint) {
        //создание машинки
        CashCarDto cashCarDto = CashCarDto.builder()
                .currentSum(0)
                .currentPoint(bankPoint)
                .cashLimit(20000)
                .timeSpent(0)
                .workingDayLength(8800).build();

        //получень маршрута
        synchronized (CashCarDto.reservedPoints) {
            List<EdgeDto> bestEdge = BestPointsCalculation.builder()
                    .cashCarDto(cashCarDto)
                    .fullEdgeDtos(fill.getEdgeDtos())
                    .sideDefinition(new SideDefinition())
                    .bankPoint(bankPoint)
                    .build().getBestWey();
        }

        while (cashCarDto.getWorkingDayLength() > 0){
            //передача маршрута на сервер, вообще, наверно, нужно передавать конкретную точку, а не весь маршрут
            WaySaverService waySaverService = new WaySaverServiceImpl(new BestWayContainer());
            BestWayCandidateDto bestWayCandidateDto = null;
            waySaverService.saveBestWay(bestWayCandidateDto);

            //после получение ответа от сервера получаю измененный граф
            fill = getFill(startPropertyDto);

            //пересчитываю связи между точками
        }


    }

}
