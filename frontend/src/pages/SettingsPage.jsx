import { useEffect, useState } from "react";
import { fetchProfile, updateProfile } from "../lib/api";

export default function SettingsPage() {
  const [form, setForm] = useState({
    emailNotifications: true,
    platformNotifications: true,
    marketingNotifications: false,
    profileVisibility: "PUBLIC",
    locale: "es"
  });
  const [message, setMessage] = useState("");

  useEffect(() => {
    fetchProfile()
      .then((data) =>
        setForm({
          emailNotifications: data.emailNotifications,
          platformNotifications: data.platformNotifications,
          marketingNotifications: data.marketingNotifications,
          profileVisibility: data.profileVisibility || "PUBLIC",
          locale: data.locale || "es"
        })
      )
      .catch(() => setMessage("No fue posible cargar configuracion."));
  }, []);

  async function handleSubmit(event) {
    event.preventDefault();
    try {
      await updateProfile(form);
      setMessage("Configuracion guardada.");
    } catch (error) {
      setMessage(error.response?.data?.message ?? "No fue posible guardar configuracion.");
    }
  }

  return (
    <section className="content-panel">
      <p className="eyebrow">Configuracion</p>
      <h3>Preferencias que sostienen la comunicacion continua.</h3>
      <form className="form-grid" onSubmit={handleSubmit}>
        <label className="check-row">
          <input
            type="checkbox"
            checked={form.emailNotifications}
            onChange={(event) => setForm({ ...form, emailNotifications: event.target.checked })}
          />
          Notificaciones por email
        </label>
        <label className="check-row">
          <input
            type="checkbox"
            checked={form.platformNotifications}
            onChange={(event) => setForm({ ...form, platformNotifications: event.target.checked })}
          />
          Notificaciones dentro de la plataforma
        </label>
        <label className="check-row">
          <input
            type="checkbox"
            checked={form.marketingNotifications}
            onChange={(event) => setForm({ ...form, marketingNotifications: event.target.checked })}
          />
          Comunicaciones comerciales
        </label>
        <label>
          Visibilidad del perfil
          <select
            value={form.profileVisibility}
            onChange={(event) => setForm({ ...form, profileVisibility: event.target.value })}
          >
            <option value="PUBLIC">PUBLIC</option>
            <option value="PRIVATE">PRIVATE</option>
          </select>
        </label>
        <label>
          Idioma
          <select value={form.locale} onChange={(event) => setForm({ ...form, locale: event.target.value })}>
            <option value="es">es</option>
            <option value="en">en</option>
          </select>
        </label>
        <button className="primary-button">Guardar preferencias</button>
      </form>
      {message ? <p className="form-success">{message}</p> : null}
    </section>
  );
}
