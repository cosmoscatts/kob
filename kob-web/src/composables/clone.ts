export const useClone = <T = any>(data: T) => JSON.parse(JSON.stringify(data));
