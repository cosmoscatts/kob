import { Token } from '~/utils';

const formatSocketUrl = () => {
  const urlPrefix = import.meta.env.VITE_BASE_SOCKET_URL;
  return `${urlPrefix}/websocket/${Token.get()}/`;
};

export function useSocket(onmessage: (msg: any) => void) {
  const pkStore = usePkStore();
  const socket = new WebSocket(formatSocketUrl());

  socket.onopen = () => pkStore.updateSocket(socket);
  socket.onmessage = onmessage;
  socket.onclose = () => {};

  return socket;
}
