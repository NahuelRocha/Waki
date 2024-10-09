import InputField from '../atoms/InputField';
import PasswordInput from '../molecules/PasswordInput';
import Button from '../atoms/Button';
import GoogleLoginButton from '../molecules/GoogleLoginButton';

export default function RegisterForm() {
    return (
        <form className="flex w-full max-w-md flex-col justify-center bg-white p-8 sm:rounded-lg sm:shadow-lg">
            <h2 className="mb-2 text-xl font-bold text-blue-600">Bienvenido a waki,</h2>
            <p className="mb-6 text-gray-500">Crea tu cuenta completando los datos</p>

            <div className="flex flex-col gap-3">
                <InputField label="Nombre de usuario" type="text" />
                <InputField label="Ingresa tu email o teléfono" type="text" />
                <PasswordInput label="Contraseña" />
                <PasswordInput label="Repetir contraseña" />
            </div>

            <Button className="h-[35px] my-6 bg-buttonPurple text-white">Registrarse</Button>

            <div className="mb-4 flex items-center justify-center relative">
                <span className="text-black-400 relative z-10 bg-white px-2">o inicia sesión con</span>
                <div className="absolute inset-0 flex items-center">
                    <div className="w-full border-t border-black"></div>
                </div>
            </div>

            <GoogleLoginButton />
        </form>
    );
}