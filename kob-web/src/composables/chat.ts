import { tokenStorage } from '~/utils/token-storage';

const formatSocketUrl = () => {
  const urlPrefix = import.meta.env.VITE_BASE_CHAT_SOCKET_URL;
  return `${urlPrefix}/chat/${tokenStorage.get()}/`;
};

export function useChatSocket(onmessage: (msg: any) => void) {
  const socket = new WebSocket(formatSocketUrl());

  socket.onopen = () => {};
  socket.onmessage = onmessage;
  socket.onclose = () => {};

  return socket;
}
