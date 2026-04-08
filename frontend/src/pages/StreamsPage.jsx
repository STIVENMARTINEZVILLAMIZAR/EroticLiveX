import { useEffect, useState } from "react";
import { fetchStreams } from "../lib/api";

export default function StreamsPage() {
  const [filters, setFilters] = useState({ country: "", category: "", tag: "", status: "LIVE" });
  const [streams, setStreams] = useState([]);
  const [error, setError] = useState("");

  useEffect(() => {
    let cancelled = false;
    fetchStreams(filters)
      .then((data) => {
        if (!cancelled) {
          setStreams(data);
          setError("");
        }
      })
      .catch(() => {
        if (!cancelled) {
          setError("No fue posible cargar transmisiones.");
        }
      });

    return () => {
      cancelled = true;
    };
  }, [filters]);

  return (
    <section className="panel-stack">
      <div className="content-panel">
        <div className="panel-heading">
          <div>
            <p className="eyebrow">Streaming simulado</p>
            <h3>Descubrimiento con filtros pensados para retencion.</h3>
          </div>
        </div>
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
          <input
            placeholder="Slug tag"
            value={filters.tag}
            onChange={(event) => setFilters({ ...filters, tag: event.target.value })}
          />
          <select
            value={filters.status}
            onChange={(event) => setFilters({ ...filters, status: event.target.value })}
          >
            <option value="LIVE">LIVE</option>
            <option value="SCHEDULED">SCHEDULED</option>
            <option value="ENDED">ENDED</option>
          </select>
        </div>
        {error ? <p className="form-error">{error}</p> : null}
      </div>
      <div className="card-grid">
        {streams.length === 0 ? (
          <div className="content-panel">
            <p>No hay transmisiones con esos filtros. Crea una desde backend o cambia los criterios.</p>
          </div>
        ) : null}
        {streams.map((stream) => (
          <article key={stream.id} className="data-card">
            <div className="card-topline">
              <span className="status-pill">{stream.status}</span>
              <span>{stream.creatorCountry}</span>
            </div>
            <h4>{stream.title}</h4>
            <p>{stream.description || "Sin descripcion."}</p>
            <div className="meta-row">
              <span>Creador: {stream.creatorDisplayName}</span>
              <span>Categoria: {stream.category || "N/A"}</span>
            </div>
            <div className="meta-row">
              <span>Espectadores: {stream.viewerCount}</span>
              <span>Precio: ${stream.accessPrice}</span>
            </div>
            <a className="ghost-link" href={stream.playbackUrl} target="_blank" rel="noreferrer">
              Abrir playback
            </a>
          </article>
        ))}
      </div>
    </section>
  );
}
