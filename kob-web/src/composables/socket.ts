import { tokenStorage } from '~/utils/token-storage';

const formatSocketUrl = () => {
  const urlPrefix = import.meta.env.VITE_BASE_SOCKET_URL;
  return `${urlPrefix}/websocket/${tokenStorage.get()}/`;
};

export function useSocket(onmessage: (msg: any) => void) {
  const pkStore = usePkStore();
  const socket = new WebSocket(formatSocketUrl());

  socket.onopen = () => pkStore.updateGameState({ socket });
  socket.onmessage = onmessage;
  socket.onclose = () => {};

  return socket;
}
