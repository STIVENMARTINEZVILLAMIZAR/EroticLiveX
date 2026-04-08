import { NavLink, Outlet, useNavigate } from "react-router-dom";
import { clearSession, getSession } from "../lib/auth";

const links = [
  { to: "/dashboard/streams", label: "Streaming" },
  { to: "/dashboard/services", label: "Marketplace" },
  { to: "/dashboard/store", label: "Tienda" },
  { to: "/dashboard/chat", label: "Chat" },
  { to: "/dashboard/profile", label: "Perfil" },
  { to: "/dashboard/settings", label: "Configuracion" }
];

export default function DashboardLayout() {
  const navigate = useNavigate();
  const session = getSession();

  function logout() {
    clearSession();
    navigate("/login");
  }

  return (
    <div className="dashboard-shell">
      <aside className="sidebar">
        <div>
          <p className="eyebrow">Lujuria Platform</p>
          <h1 className="sidebar-title">Todo ocurre dentro del sistema</h1>
        </div>
        <nav className="sidebar-nav">
          {links.map((link) => (
            <NavLink
              key={link.to}
              to={link.to}
              className={({ isActive }) => (isActive ? "nav-link active" : "nav-link")}
            >
              {link.label}
            </NavLink>
          ))}
        </nav>
        <div className="sidebar-card">
          <p className="mini-label">Sesion activa</p>
          <strong>{session?.email ?? "Usuario"}</strong>
          <button className="secondary-button" onClick={logout}>
            Cerrar sesion
          </button>
        </div>
      </aside>
      <main className="dashboard-main">
        <header className="dashboard-header">
          <div>
            <p className="eyebrow">Marketplace + streaming + chat</p>
            <h2>Base lista para web y futura app movil</h2>
          </div>
          <div className="status-pill">JWT + REST + WebSocket</div>
        </header>
        <Outlet />
      </main>
    </div>
  );
}
