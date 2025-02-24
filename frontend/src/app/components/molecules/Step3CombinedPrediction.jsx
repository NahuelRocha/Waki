import { useModal } from '../../context/ModalContext';
import { IoFootballOutline } from 'react-icons/io5';
import { TbPlayFootball } from 'react-icons/tb';

export default function Step3CombinedPrediction() {
  const { handleNextStep } = useModal();

  return (
    <section className="flex flex-col divide-y">
      <div className="flex flex-col px-5 pb-5">
        <h2 className="text-center text-semibold-22 font-semibold text-label">
          ¿Con qué vas a combinar?
        </h2>
        <p className="text-center text-medium-18 font-medium text-grayWaki">
          Selecciona una opción
        </p>
      </div>
      <div className="flex flex-col gap-4 px-10 py-7">
        {/* Botón Goles - actualmente deshabilitado */}
        <button
          className="flex items-center gap-2 rounded-large border border-transparent bg-white p-5 text-medium-18 font-medium text-label shadow-[0_0_9.2px_0_rgba(0,0,0,0.25)] transition-all duration-300 hover:border-blueWaki disabled:border-transparent disabled:bg-gray-200"
          disabled
        >
          <TbPlayFootball size={36} className="text-blueWaki" />
          Goles (Próximamente)
        </button>

        {/* Botón Otro partido - Avanza al paso 4 */}
        <button
          className="flex items-center gap-2 rounded-large border border-transparent bg-white p-5 text-medium-18 font-medium text-label shadow-[0_0_9.2px_0_rgba(0,0,0,0.25)] transition-all duration-300 hover:border-blueWaki"
          onClick={() => handleNextStep(4)}
        >
          <IoFootballOutline size={36} className="text-blueWaki" />
          Otro partido
        </button>
      </div>
    </section>
  );
}
