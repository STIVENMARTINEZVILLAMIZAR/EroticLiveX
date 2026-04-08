import { useEffect, useState } from "react";
import { createOrder, fetchOrders, fetchProducts } from "../lib/api";

export default function StorePage() {
  const [products, setProducts] = useState([]);
  const [orders, setOrders] = useState([]);
  const [message, setMessage] = useState("");

  useEffect(() => {
    fetchProducts({})
      .then(setProducts)
      .catch(() => setMessage("No fue posible cargar productos."));

    fetchOrders().then(setOrders).catch(() => null);
  }, []);

  async function buy(productId) {
    try {
      const order = await createOrder({ items: [{ productId, quantity: 1 }] });
      setOrders((current) => [order, ...current]);
      setMessage(`Pago simulado aprobado. Referencia ${order.paymentReference}.`);
    } catch (error) {
      setMessage(error.response?.data?.message ?? "No fue posible completar la compra.");
    }
  }

  return (
    <section className="panel-stack">
      <div className="content-panel">
        <p className="eyebrow">Tienda</p>
        <h3>Compra rapida para monetizacion inmediata del ecosistema.</h3>
        {message ? <p className="form-success">{message}</p> : null}
      </div>

      <div className="card-grid">
        {products.map((product) => (
          <article key={product.id} className="data-card">
            <div className="card-topline">
              <span className="status-pill">{product.type}</span>
              <span>Inventario: {product.inventory}</span>
            </div>
            <h4>{product.name}</h4>
            <p>{product.description || "Sin descripcion."}</p>
            <div className="meta-row">
              <span>{product.creatorDisplayName}</span>
              <span>${product.price}</span>
            </div>
            <button className="secondary-button" onClick={() => buy(product.id)}>
              Comprar
            </button>
          </article>
        ))}
      </div>

      <div className="content-panel">
        <p className="eyebrow">Mis ordenes</p>
        <div className="list-stack">
          {orders.length === 0 ? <p>No tienes ordenes todavia.</p> : null}
          {orders.map((order) => (
            <div key={order.id} className="list-row">
              <strong>Orden #{order.id}</strong>
              <span>{order.status}</span>
              <span>${order.totalAmount}</span>
              <span>{order.paymentReference}</span>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}
