import { useState } from "react";
import { Link } from "react-router-dom";

export default function LandingPage() {
  const [checks, setChecks] = useState({
    adult: false,
    terms: false,
    privacy: false
  });

  const ready = checks.adult && checks.terms && checks.privacy;

  function toggle(key) {
    setChecks((current) => ({ ...current, [key]: !current[key] }));
  }

  return (
    <div className="landing-page">
      <div className="landing-collage" />
      <div className="landing-overlay">
        <section className="hero-card">
          <p className="eyebrow">Marketplace + streaming + mensajeria</p>
          <h1>Una plataforma para descubrir, conversar, reservar y pagar sin salir del ecosistema.</h1>
          <p className="hero-copy">
            La idea de producto es simple: cada interaccion debe llevar a la siguiente. Contenido,
            contacto, reserva, pago y seguimiento en el mismo lugar.
          </p>
          <div className="hero-actions">
            <Link className="primary-button" to={ready ? "/login" : "#"}>
              Entrar a la plataforma
            </Link>
            <a className="ghost-link" href="#consent">
              Revisar requisitos
            </a>
          </div>
        </section>

        <section className="modal-card" id="consent">
          <p className="eyebrow">Acceso requerido</p>
          <h2>Antes de continuar necesitamos validacion obligatoria.</h2>
          <label className="check-row">
            <input type="checkbox" checked={checks.adult} onChange={() => toggle("adult")} />
            Confirmo que soy mayor de 18 anos.
          </label>
          <label className="check-row">
            <input type="checkbox" checked={checks.terms} onChange={() => toggle("terms")} />
            Acepto terminos de uso de la plataforma.
          </label>
          <label className="check-row">
            <input type="checkbox" checked={checks.privacy} onChange={() => toggle("privacy")} />
            Acepto politicas de privacidad y tratamiento de datos.
          </label>
          <div className="consent-footer">
            <span className={ready ? "status-pill ready" : "status-pill"}>
              {ready ? "Acceso habilitado" : "Debes aceptar las tres condiciones"}
            </span>
            <Link className={ready ? "primary-button" : "primary-button disabled"} to={ready ? "/login" : "#"}>
              Continuar
            </Link>
          </div>
        </section>
      </div>
    </div>
  );
}
