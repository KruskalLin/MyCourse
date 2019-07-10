const getPerformanceTiming = () => {
  const performance = window.performance
  const t = performance.timing
  const times = {}

  // 【重要】页面加载完成的时间
  times.onload = t.loadEventEnd - t.navigationStart

  // 【重要】解析DOM树结构的时间，包括内嵌资源
  times.domResolved = t.domComplete - t.domLoading

  // 【重要】dom准备开始解析，从最开始到准备开始解析DOM的时间
  times.domReadyResolve = t.domLoading - t.navigationStart

  // 【重要】白屏时间，读取页面第一个字节的时间
  times.firstPaint = t.responseStart - t.navigationStart

  // 【重要】内容加载完成的时间
  times.request = t.responseEnd - t.requestStart

  // 【重要】time to interactive
  times.tti = t.domInteractive - t.requestStart

  return times
}

export default getPerformanceTiming
