import { useEffect, useState } from "react";
import { fetchProfile, updateProfile } from "../lib/api";

export default function ProfilePage() {
  const [form, setForm] = useState({
    firstName: "",
    lastName: "",
    country: "",
    city: "",
    bio: ""
  });
  const [profile, setProfile] = useState(null);
  const [message, setMessage] = useState("");

  useEffect(() => {
    fetchProfile()
      .then((data) => {
        setProfile(data);
        setForm({
          firstName: data.firstName ?? "",
          lastName: data.lastName ?? "",
          country: data.country ?? "",
          city: data.city ?? "",
          bio: data.bio ?? ""
        });
      })
      .catch(() => setMessage("No fue posible cargar el perfil."));
  }, []);

  async function handleSubmit(event) {
    event.preventDefault();
    try {
      const updated = await updateProfile(form);
      setProfile(updated);
      setMessage("Perfil actualizado.");
    } catch (error) {
      setMessage(error.response?.data?.message ?? "No fue posible actualizar el perfil.");
    }
  }

  return (
    <section className="split-layout">
      <div className="content-panel">
        <p className="eyebrow">Perfil</p>
        <h3>{profile ? `${profile.firstName} ${profile.lastName}` : "Cargando perfil..."}</h3>
        <div className="profile-badges">
          {profile?.roles?.map((role) => (
            <span key={role} className="status-pill">
              {role}
            </span>
          ))}
        </div>
        <p>{profile?.email}</p>
        <p>{profile?.creatorDisplayName ? `Creador: ${profile.creatorDisplayName}` : "Cuenta de cliente."}</p>
      </div>

      <form className="content-panel form-grid" onSubmit={handleSubmit}>
        <label>
          Nombre
          <input value={form.firstName} onChange={(event) => setForm({ ...form, firstName: event.target.value })} />
        </label>
        <label>
          Apellido
          <input value={form.lastName} onChange={(event) => setForm({ ...form, lastName: event.target.value })} />
        </label>
        <label>
          Pais
          <input value={form.country} onChange={(event) => setForm({ ...form, country: event.target.value })} />
        </label>
        <label>
          Ciudad
          <input value={form.city} onChange={(event) => setForm({ ...form, city: event.target.value })} />
        </label>
        <label>
          Bio
          <textarea rows="6" value={form.bio} onChange={(event) => setForm({ ...form, bio: event.target.value })} />
        </label>
        <button className="primary-button">Guardar cambios</button>
        {message ? <p className="form-success">{message}</p> : null}
      </form>
    </section>
  );
}
