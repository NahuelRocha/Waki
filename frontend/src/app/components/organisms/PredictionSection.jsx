import AnchorButton from '../atoms/AnchorButton';
import { PredictionCard } from '../molecules/PredictionCard';

export default function PredictionSection({ title, predictions }) {
  return (
    <div className="rounded-t-large bg-white p-5">
      <div className="mb-5 flex items-center justify-between">
        <h3 className="text-medium-18 font-medium text-blueWaki">{title}</h3>
        <AnchorButton>Hacer predicción</AnchorButton>
      </div>
      <ul className="mb-2 flex items-center justify-between rounded-large px-4 py-2 shadow-[0_0_14.6px_0_rgba(0,0,0,0.2)]">
        <li className="text-grayLightWaki text-regular-12">Predicción</li>
        <li className="text-grayLightWaki text-regular-12">Partido</li>
        <li className="text-grayLightWaki text-regular-12">Puntos</li>
      </ul>
      <div className="flex flex-col gap-2">
        {predictions.map((prediction, index) => (
          <PredictionCard key={index} {...prediction} />
        ))}
      </div>
    </div>
  );
}
