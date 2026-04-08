import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { login, register } from "../lib/api";
import { saveSession } from "../lib/auth";

const initialRegister = {
  firstName: "",
  lastName: "",
  email: "",
  password: "",
  birthDate: "",
  country: "Colombia",
  city: "",
  adultConfirmed: true,
  acceptedTerms: true,
  acceptedPrivacyPolicy: true,
  creatorRequest: false,
  creatorDisplayName: "",
  creatorHeadline: ""
};

export default function LoginPage() {
  const navigate = useNavigate();
  const [mode, setMode] = useState("login");
  const [loginForm, setLoginForm] = useState({ email: "", password: "" });
  const [registerForm, setRegisterForm] = useState(initialRegister);
  const [status, setStatus] = useState({ error: "", success: "", loading: false });

  async function handleLogin(event) {
    event.preventDefault();
    setStatus({ error: "", success: "", loading: true });
    try {
      const session = await login(loginForm);
      saveSession(session);
      setStatus({ error: "", success: "Sesion iniciada.", loading: false });
      navigate("/dashboard/streams");
    } catch (error) {
      setStatus({
        error: error.response?.data?.message ?? "No fue posible iniciar sesion.",
        success: "",
        loading: false
      });
    }
  }

  async function handleRegister(event) {
    event.preventDefault();
    setStatus({ error: "", success: "", loading: true });
    try {
      const session = await register(registerForm);
      saveSession(session);
      setStatus({ error: "", success: "Cuenta creada correctamente.", loading: false });
      navigate("/dashboard/streams");
    } catch (error) {
      setStatus({
        error: error.response?.data?.message ?? "No fue posible crear la cuenta.",
        success: "",
        loading: false
      });
    }
  }

  return (
    <div className="auth-page">
      <section className="auth-panel">
        <div className="auth-copy">
          <p className="eyebrow">Acceso a la plataforma</p>
          <h1>Empieza por identidad, seguridad y continuidad.</h1>
          <p>
            Esta SPA ya se conecta a JWT, perfil, streams, servicios, chat y pagos simulados del backend.
          </p>
        </div>
        <div className="auth-tabs">
          <button className={mode === "login" ? "tab active" : "tab"} onClick={() => setMode("login")}>
            Iniciar sesion
          </button>
          <button className={mode === "register" ? "tab active" : "tab"} onClick={() => setMode("register")}>
            Registrarse
          </button>
        </div>

        {mode === "login" ? (
          <form className="form-grid" onSubmit={handleLogin}>
            <label>
              Correo
              <input
                type="email"
                value={loginForm.email}
                onChange={(event) => setLoginForm({ ...loginForm, email: event.target.value })}
                required
              />
            </label>
            <label>
              Contrasena
              <input
                type="password"
                value={loginForm.password}
                onChange={(event) => setLoginForm({ ...loginForm, password: event.target.value })}
                required
              />
            </label>
            <button className="primary-button" disabled={status.loading}>
              {status.loading ? "Ingresando..." : "Entrar"}
            </button>
          </form>
        ) : (
          <form className="form-grid wide" onSubmit={handleRegister}>
            <label>
              Nombre
              <input
                value={registerForm.firstName}
                onChange={(event) => setRegisterForm({ ...registerForm, firstName: event.target.value })}
                required
              />
            </label>
            <label>
              Apellido
              <input
                value={registerForm.lastName}
                onChange={(event) => setRegisterForm({ ...registerForm, lastName: event.target.value })}
                required
              />
            </label>
            <label>
              Correo
              <input
                type="email"
                value={registerForm.email}
                onChange={(event) => setRegisterForm({ ...registerForm, email: event.target.value })}
                required
              />
            </label>
            <label>
              Contrasena
              <input
                type="password"
                value={registerForm.password}
                onChange={(event) => setRegisterForm({ ...registerForm, password: event.target.value })}
                required
              />
            </label>
            <label>
              Fecha de nacimiento
              <input
                type="date"
                value={registerForm.birthDate}
                onChange={(event) => setRegisterForm({ ...registerForm, birthDate: event.target.value })}
                required
              />
            </label>
            <label>
              Pais
              <input
                value={registerForm.country}
                onChange={(event) => setRegisterForm({ ...registerForm, country: event.target.value })}
                required
              />
            </label>
            <label>
              Ciudad
              <input
                value={registerForm.city}
                onChange={(event) => setRegisterForm({ ...registerForm, city: event.target.value })}
              />
            </label>
            <label className="check-row">
              <input
                type="checkbox"
                checked={registerForm.creatorRequest}
                onChange={(event) => setRegisterForm({ ...registerForm, creatorRequest: event.target.checked })}
              />
              Quiero registrarme como creador
            </label>
            {registerForm.creatorRequest ? (
              <>
                <label>
                  Nombre publico
                  <input
                    value={registerForm.creatorDisplayName}
                    onChange={(event) =>
                      setRegisterForm({ ...registerForm, creatorDisplayName: event.target.value })
                    }
                    required
                  />
                </label>
                <label>
                  Titular
                  <input
                    value={registerForm.creatorHeadline}
                    onChange={(event) =>
                      setRegisterForm({ ...registerForm, creatorHeadline: event.target.value })
                    }
                  />
                </label>
              </>
            ) : null}
            <button className="primary-button" disabled={status.loading}>
              {status.loading ? "Creando cuenta..." : "Crear cuenta"}
            </button>
          </form>
        )}

        {status.error ? <p className="form-error">{status.error}</p> : null}
        {status.success ? <p className="form-success">{status.success}</p> : null}
      </section>
    </div>
  );
}
