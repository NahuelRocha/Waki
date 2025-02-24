package com.app.waki.prediction.domain;

import com.app.waki.prediction.application.dto.PredictionMatchDto;
import com.app.waki.prediction.domain.valueObject.*;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@EqualsAndHashCode
@ToString
@Getter
public class Prediction {

    @Id
    private PredictionId predictionId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "details_id")
    private PredictionDetails predictionDetails;
    private String matchId;
    @Enumerated(EnumType.STRING)
    private ExpectedResult expectedResult;
    @Enumerated(EnumType.STRING)
    private MatchResult matchResult;
    @AttributeOverrides({
            @AttributeOverride(name = "team", column = @Column(name = "home_team"))
    })
    private Team homeTeam;
    @AttributeOverrides({
            @AttributeOverride(name = "team", column = @Column(name = "away_team"))
    })
    private Team awayTeam;
    private String homeShield;
    private String awayShield;
    private HomeGoals homeGoals;
    private AwayGoals awayGoals;
    private LocalDate matchDay;
    private Double odds;
    private String competition;
    private String competitionShield;
    private Boolean combined;
    @Enumerated(EnumType.STRING)
    private PredictionStatus status;
    @Version
    private Long version;

    public Prediction(){}

    private Prediction(PredictionDetails predictionDetails, String matchId, ExpectedResult expectedResult,
                       Team homeTeam, Team awayTeam, String homeShield, String awayShield, LocalDate matchDay,
                       Double odds, String competition, String competitionShield, Boolean combined) {
        this.predictionId = new PredictionId();
        this.predictionDetails = predictionDetails;
        this.matchId = matchId;
        this.expectedResult = expectedResult;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeShield = homeShield;
        this.awayShield = awayShield;
        this.matchResult = MatchResult.PENDING;
        this.matchDay = matchDay;
        this.odds = odds;
        this.competition = competition;
        this.competitionShield = competitionShield;
        this.combined = combined;
        this.status = PredictionStatus.PENDING;
    }

    public static Prediction createPrediction(PredictionDetails predictionDetails, PredictionRequest predictionRequest, Boolean combined){

        var expectedResult = ExpectedResult.fromString(predictionRequest.expectedResult());
        var homeTeam = new Team(predictionRequest.homeTeam());
        var awayTeam = new Team(predictionRequest.awayTeam());

        return new Prediction(
                predictionDetails,
                predictionRequest.matchId(),
                expectedResult,
                homeTeam,
                awayTeam,
                predictionRequest.homeShield(),
                predictionRequest.awayShield(),
                predictionRequest.matchDay(),
                predictionRequest.pay(),
                predictionRequest.competition(),
                predictionRequest.competitionShield(),
                combined
        );
    }

    void setPredictionDetails(PredictionDetails predictionDetails) {
        this.predictionDetails = predictionDetails;
    }

    public void updateMatchResult(MatchResult result, Integer homeGoals, Integer awayGoals) {
        this.matchResult = result;
        this.status = (this.expectedResult.toString().equals(result.toString())) ? PredictionStatus.CORRECT : PredictionStatus.FAILED;
        this.homeGoals = new HomeGoals(homeGoals);
        this.awayGoals = new AwayGoals(awayGoals);
    }

    public boolean isPredictionCorrect(){

        return this.status.equals(PredictionStatus.CORRECT);
    }

    public boolean getCombined(){
        return this.combined;
    }

    public void setPredictionStatus(PredictionStatus status){
        this.status = status;
    }

    public void setMatchResult(MatchResult matchResult){
        this.matchResult = matchResult;
    }

    public String getFinalResult() {
        return switch (this.matchResult) {
            case LOCAL -> homeTeam.team();
            case AWAY -> awayTeam.team();
            default -> matchResult.name();
        };
    }

    public String getExpectedResult() {
        return switch (this.expectedResult) {
            case LOCAL -> homeTeam.team();
            case AWAY -> awayTeam.team();
            default -> ExpectedResult.DRAW.name();
        };
    }

    public PredictionMatchDto toMatchDto() {
        return new PredictionMatchDto(
                this.competition,
                this.combined,
                calculatePoints(),
                this.getExpectedResult(),
                this.homeTeam.team(),
                this.awayTeam.team(),
                this.homeShield,
                this.awayShield,
                this.competitionShield
        );
    }

    private int calculatePoints() {
        final int IND_PREDICTION_MULTI = 10;
        return (int)(this.odds * IND_PREDICTION_MULTI);
    }
}
