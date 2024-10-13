import type { AnimationItem, RendererType } from 'lottie-web';
import lottie from 'lottie-web';

export interface LottieParams {
  containerId: string
  path: string
  loop?: boolean
  renderer?: RendererType
  autoplay?: boolean
}

const getElement = (id: string): HTMLElement => {
  const element = document.querySelector(id);
  if (!element) {
    throw new Error(`Element with id "${id}" not found`);
  }
  return element as HTMLElement;
};

export const useLottie = ({
  containerId,
  path,
  loop = true,
  renderer = 'svg',
  autoplay = true,
}: LottieParams): AnimationItem => {
  return lottie.loadAnimation({
    container: getElement(containerId),
    path,
    loop,
    renderer,
    autoplay,
  });
};

export const useListLottie = (list: LottieParams[]): AnimationItem[] => {
  return list.map(useLottie);
};
