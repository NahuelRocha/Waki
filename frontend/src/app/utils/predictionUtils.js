import { useEffect, useState } from 'react';
import { useDate } from '../context/DateContext';
import { getPredictionByDate } from '../services/predictionService';
import { formatDate } from './dateUtils';

export function PredictionsByDate(userId) {
  const { selectedDate } = useDate();
  const [datePredictions, setDatePredictions] = useState([]);
  console.log('date PredictionDetailsId ', datePredictions);
  console.log('selectedDate ', formatDate(selectedDate));

  useEffect(() => {
    setDatePredictions([]);
    const fetchPredictionsByDate = async () => {
      if (selectedDate) {
        try {
          const data = await getPredictionByDate(
            userId,
            formatDate(selectedDate)
          );
          setDatePredictions(data);
        } catch (error) {
          console.error('Error al obtener predicciones por fecha:', error);
        }
      }
    };
    if (selectedDate !== null) {
      fetchPredictionsByDate();
    }
  }, [selectedDate]);

  return datePredictions;
}

// Función para calcular los puntos de una predicción según la opción seleccionada
export const calculatePoints = (selectedOption, matchPredictions) => {
  return selectedOption === 'LOCAL'
    ? matchPredictions.localWin
    : selectedOption === 'DRAW'
      ? matchPredictions.draw
      : matchPredictions.visitorWin;
};
