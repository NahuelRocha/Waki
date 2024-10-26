package com.app.waki.division.application;

import com.app.waki.common.exceptions.EntityNotFoundException;
import com.app.waki.division.domain.Division;
import com.app.waki.division.domain.DivisionRepository;
import com.app.waki.division.domain.UserRanking;
import com.app.waki.division.domain.valueObject.DivisionLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DivisionRecalculationService {

    private final DivisionRepository divisionRepository;


    //esto se debería ejecutar con un cron cada media noche
    @Transactional
    public void recalculateDivisionsAndRankings() {
        try {
            log.info("Starting division and ranking recalculation");

            // 1. Obtener todas las divisiones (sabemos que existen)
            List<Division> allDivisions = divisionRepository.findAll();

            if (allDivisions.isEmpty()){
                throw new EntityNotFoundException("No divisions found");
            }

            Map<DivisionLevel, Division> divisionMap = allDivisions.stream()
                    .collect(Collectors.toMap(Division::getDivision, d -> d));

            // 2. Obtener todos los UserRankings con points > 0 ordenados por puntos
            List<UserRanking> activeRankings = allDivisions.stream()
                    .flatMap(d -> d.getRankings().stream())
                    .filter(r -> r.getPoints() > 0)
                    .sorted(Comparator.comparing(UserRanking::getPoints).reversed())
                    .toList();

            int totalActive = activeRankings.size();
            if (totalActive == 0) {
                log.info("No active users found for recalculation");
                return;
            }

            // 3. Calcular límites para cada división (33% cada una)
            int goldLimit = (totalActive * 33) / 100;
            int silverLimit = (totalActive * 66) / 100;

            // 4. Limpiar divisiones competitivas
            divisionMap.get(DivisionLevel.GOLD).clearRankings();
            divisionMap.get(DivisionLevel.SILVER).clearRankings();
            divisionMap.get(DivisionLevel.BRONZE).clearRankings();

            // 5. Distribuir usuarios y asignar posiciones
            for (int i = 0; i < totalActive; i++) {
                UserRanking ranking = activeRankings.get(i);
                ranking.updatePosition(i + 1);

                if (i < goldLimit) {
                    divisionMap.get(DivisionLevel.GOLD).addUserRanking(ranking);
                } else if (i < silverLimit) {
                    divisionMap.get(DivisionLevel.SILVER).addUserRanking(ranking);
                } else {
                    divisionMap.get(DivisionLevel.BRONZE).addUserRanking(ranking);
                }
            }

            // 6. Guardar cambios
            divisionRepository.saveAll(allDivisions);

            log.info("Division recalculation completed. Distributed {} users across divisions", totalActive);

        } catch (Exception e) {
            log.error("Error during division recalculation", e);
            throw new RuntimeException("Failed to recalculate divisions", e);
        }
    }
}