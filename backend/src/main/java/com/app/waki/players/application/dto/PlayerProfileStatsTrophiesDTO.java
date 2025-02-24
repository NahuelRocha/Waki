package com.app.waki.players.application.dto;

import com.app.waki.players.domain.player.Birth;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PlayerProfileStatsTrophiesDTO {
    private Long profileId;
    private String name;
    private String firstname;
    private String lastname;
    private int age;
    private Birth birth;
    private String nationality;
    private boolean injured;
    private String photo;

    private String logrosDesde;
    private String logrosHasta;
    private String estadisticasDesde;
    private String estadisticasHasta;
    private String position;

    // Totales de estadísticas
    private int totalGoals;
    private int totalAppearances;
    private int totalMinutes;
    private int totalAssists;
    private int totalYellowCards;
    private int totalRedCards;

    // Lista de trofeos
    private List<?> trophies;
}
