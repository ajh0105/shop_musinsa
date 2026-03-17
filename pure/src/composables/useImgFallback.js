const FALLBACK_IMG = 'https://placehold.co/400x500/f5f5f5/999?text=No+Image'

export function useImgFallback() {
  function onImgError(e) {
    e.target.src = FALLBACK_IMG
    e.target.onerror = null
  }
  return { onImgError }
}
