import lottie from 'lottie-web'
import type { RendererType } from 'lottie-web'

export function useLottie({
  container,
  path,
  loop = true,
  renderer = 'svg',
}: {
  container: Element
  path: string
  loop?: boolean
  renderer?: RendererType
}) {
  lottie.loadAnimation({
    container,
    path,
    loop,
    renderer,
  })
}
