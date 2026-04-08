import { Client } from "@stomp/stompjs";
import { useEffect, useRef, useState } from "react";
import SockJS from "sockjs-client";
import {
  createConversation,
  fetchConversations,
  fetchMessages,
  sendMessage
} from "../lib/api";
import { getToken } from "../lib/auth";

export default function ChatPage() {
  const [conversations, setConversations] = useState([]);
  const [selectedConversationId, setSelectedConversationId] = useState(null);
  const [messages, setMessages] = useState([]);
  const [messageText, setMessageText] = useState("");
  const [status, setStatus] = useState("");
  const [newConversation, setNewConversation] = useState({ subject: "", participantIds: "" });
  const selectedConversationRef = useRef(null);

  useEffect(() => {
    selectedConversationRef.current = selectedConversationId;
  }, [selectedConversationId]);

  useEffect(() => {
    fetchConversations().then(setConversations).catch(() => setStatus("No fue posible cargar conversaciones."));
  }, []);

  useEffect(() => {
    const token = getToken();
    if (!token) {
      return undefined;
    }

    const client = new Client({
      webSocketFactory: () => new SockJS("/ws"),
      connectHeaders: {
        Authorization: `Bearer ${token}`
      },
      reconnectDelay: 4000,
      onConnect: () => {
        client.subscribe("/user/queue/messages", (frame) => {
          const payload = JSON.parse(frame.body);
          if (payload.conversationId === selectedConversationRef.current) {
            setMessages((current) =>
              current.some((message) => message.id === payload.id) ? current : [...current, payload]
            );
          }
          fetchConversations().then(setConversations).catch(() => null);
        });
        setStatus("WebSocket conectado.");
      },
      onStompError: () => setStatus("No fue posible conectar el chat en tiempo real.")
    });

    client.activate();

    return () => {
      client.deactivate();
    };
  }, []);

  useEffect(() => {
    if (!selectedConversationId) {
      return;
    }

    fetchMessages(selectedConversationId)
      .then(setMessages)
      .catch(() => setStatus("No fue posible cargar mensajes."));
  }, [selectedConversationId]);

  async function handleCreateConversation(event) {
    event.preventDefault();
    try {
      const participantIds = newConversation.participantIds
        .split(",")
        .map((value) => Number(value.trim()))
        .filter(Boolean);

      const conversation = await createConversation({
        type: "DIRECT",
        subject: newConversation.subject,
        participantIds
      });
      setConversations((current) => [conversation, ...current]);
      setSelectedConversationId(conversation.id);
      setNewConversation({ subject: "", participantIds: "" });
      setStatus("Conversacion creada.");
    } catch (error) {
      setStatus(error.response?.data?.message ?? "No fue posible crear la conversacion.");
    }
  }

  async function handleSendMessage(event) {
    event.preventDefault();
    if (!selectedConversationId || !messageText.trim()) {
      return;
    }

    try {
      await sendMessage(selectedConversationId, { content: messageText });
      setMessageText("");
    } catch (error) {
      setStatus(error.response?.data?.message ?? "No fue posible enviar el mensaje.");
    }
  }

  return (
    <section className="split-layout chat-layout">
      <aside className="content-panel chat-sidebar">
        <p className="eyebrow">Conversaciones</p>
        <form className="form-grid compact" onSubmit={handleCreateConversation}>
          <label>
            Asunto
            <input
              value={newConversation.subject}
              onChange={(event) => setNewConversation({ ...newConversation, subject: event.target.value })}
            />
          </label>
          <label>
            IDs participantes
            <input
              placeholder="2, 5, 9"
              value={newConversation.participantIds}
              onChange={(event) =>
                setNewConversation({ ...newConversation, participantIds: event.target.value })
              }
              required
            />
          </label>
          <button className="secondary-button">Crear conversacion</button>
        </form>
        <div className="list-stack">
          {conversations.map((conversation) => (
            <button
              key={conversation.id}
              className={selectedConversationId === conversation.id ? "list-row active-row" : "list-row"}
              onClick={() => setSelectedConversationId(conversation.id)}
            >
              <strong>#{conversation.id}</strong>
              <span>{conversation.subject || conversation.type}</span>
              <span>{conversation.participantIds.join(", ")}</span>
            </button>
          ))}
        </div>
      </aside>

      <div className="content-panel chat-panel">
        <div className="panel-heading">
          <div>
            <p className="eyebrow">Mensajeria persistente</p>
            <h3>{selectedConversationId ? `Conversacion ${selectedConversationId}` : "Selecciona una conversacion"}</h3>
          </div>
          <span className="status-pill">{status || "Lista"}</span>
        </div>

        <div className="messages-box">
          {messages.length === 0 ? <p>No hay mensajes para mostrar.</p> : null}
          {messages.map((message) => (
            <article key={`${message.id}-${message.createdAt}`} className="message-bubble">
              <strong>{message.senderName}</strong>
              <p>{message.content}</p>
              <span>{new Date(message.createdAt).toLocaleString()}</span>
            </article>
          ))}
        </div>

        <form className="message-form" onSubmit={handleSendMessage}>
          <input
            placeholder="Escribe tu mensaje..."
            value={messageText}
            onChange={(event) => setMessageText(event.target.value)}
          />
          <button className="primary-button" disabled={!selectedConversationId}>
            Enviar
          </button>
        </form>
      </div>
    </section>
  );
}
