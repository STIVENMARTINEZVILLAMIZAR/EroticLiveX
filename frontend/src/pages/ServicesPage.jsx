import { useEffect, useState } from "react";
import { createBooking, fetchServices } from "../lib/api";

export default function ServicesPage() {
  const [filters, setFilters] = useState({ country: "", category: "", mode: "" });
  const [services, setServices] = useState([]);
  const [selectedService, setSelectedService] = useState(null);
  const [bookingForm, setBookingForm] = useState({ scheduledAt: "", notes: "" });
  const [message, setMessage] = useState("");

  useEffect(() => {
    fetchServices(filters)
      .then(setServices)
      .catch(() => setMessage("No fue posible cargar servicios."));
  }, [filters]);

  async function submitBooking(event) {
    event.preventDefault();
    if (!selectedService) {
      return;
    }

    try {
      const payload = {
        scheduledAt: new Date(bookingForm.scheduledAt).toISOString(),
        notes: bookingForm.notes
      };
      await createBooking(selectedService.id, payload);
      setMessage("Reserva creada. Idealmente el siguiente paso es abrir una conversacion.");
      setBookingForm({ scheduledAt: "", notes: "" });
    } catch (error) {
      setMessage(error.response?.data?.message ?? "No fue posible reservar el servicio.");
    }
  }

  return (
    <section className="split-layout">
      <div className="panel-stack">
        <div className="content-panel">
          <p className="eyebrow">Marketplace de servicios</p>
          <h3>Explora proveedores, agenda y manten la conversacion dentro del producto.</h3>
          <div className="filters-grid">
            <input
              placeholder="Pais"
              value={filters.country}
              onChange={(event) => setFilters({ ...filters, country: event.target.value })}
            />
            <input
              placeholder="Slug categoria"
              value={filters.category}
              onChange={(event) => setFilters({ ...filters, category: event.target.value })}
            />
            <select value={filters.mode} onChange={(event) => setFilters({ ...filters, mode: event.target.value })}>
              <option value="">Todos</option>
              <option value="DIGITAL">DIGITAL</option>
              <option value="PRESENTIAL">PRESENTIAL</option>
              <option value="HYBRID">HYBRID</option>
            </select>
          </div>
        </div>
        <div className="card-grid">
          {services.map((service) => (
            <article key={service.id} className="data-card">
              <div className="card-topline">
                <span className="status-pill">{service.mode}</span>
                <span>{service.creatorCountry}</span>
              </div>
              <h4>{service.title}</h4>
              <p>{service.description || "Sin descripcion."}</p>
              <div className="meta-row">
                <span>Proveedor: {service.creatorDisplayName}</span>
                <span>Duracion: {service.durationMinutes} min</span>
              </div>
              <div className="meta-row">
                <span>Precio: ${service.price}</span>
                <span>{service.category || "Sin categoria"}</span>
              </div>
              <button className="secondary-button" onClick={() => setSelectedService(service)}>
                Reservar este servicio
              </button>
            </article>
          ))}
        </div>
      </div>
      <aside className="content-panel sticky-panel">
        <p className="eyebrow">Agenda rapida</p>
        <h3>{selectedService ? selectedService.title : "Selecciona un servicio"}</h3>
        <p>
          {selectedService
            ? `Reservaras con ${selectedService.creatorDisplayName}.`
            : "El flujo ideal es servicio -> reserva -> chat -> pago -> seguimiento."}
        </p>
        <form className="form-grid" onSubmit={submitBooking}>
          <label>
            Fecha y hora
            <input
              type="datetime-local"
              value={bookingForm.scheduledAt}
              onChange={(event) => setBookingForm({ ...bookingForm, scheduledAt: event.target.value })}
              required
            />
          </label>
          <label>
            Notas
            <textarea
              rows="5"
              value={bookingForm.notes}
              onChange={(event) => setBookingForm({ ...bookingForm, notes: event.target.value })}
            />
          </label>
          <button className="primary-button" disabled={!selectedService}>
            Confirmar reserva
          </button>
        </form>
        {message ? <p className="form-success">{message}</p> : null}
      </aside>
    </section>
  );
}
