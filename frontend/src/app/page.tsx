import Link from "next/link";

export default function Home() {
  return (
    <div className="relative min-h-screen overflow-hidden bg-black text-white selection:bg-emerald-500/30">
      {/* Background Decorative Elements */}
      <div className="pointer-events-none absolute inset-0 overflow-hidden">
        <div className="absolute -top-[25%] -left-[10%] h-[1000px] w-[1000px] rounded-full bg-emerald-500/10 blur-[120px]" />
        <div className="absolute -bottom-[20%] -right-[10%] h-[800px] w-[800px] rounded-full bg-blue-500/10 blur-[120px]" />
      </div>

      <nav className="relative z-10 flex items-center justify-between px-8 py-6 backdrop-blur-sm border-b border-white/5">
        <div className="flex items-center gap-2">
          <div className="h-8 w-8 rounded-lg bg-emerald-500 flex items-center justify-center shadow-[0_0_20px_rgba(16,185,129,0.4)]">
            <span className="font-bold text-black text-xl">S</span>
          </div>
          <span className="text-xl font-bold tracking-tight">StackScout</span>
        </div>
        <div className="hidden md:flex items-center gap-8 text-sm font-medium text-zinc-400">
          <Link href="#" className="hover:text-white transition-colors">
            Обзор
          </Link>
          <Link href="#" className="hover:text-white transition-colors">
            API
          </Link>
          <Link href="#" className="hover:text-white transition-colors">
            Roadmap
          </Link>
          <Link href="#" className="hover:text-white transition-colors">
            Документация
          </Link>
        </div>
        <div>
          <button className="rounded-full bg-white px-5 py-2 text-sm font-semibold text-black hover:bg-zinc-200 transition-all active:scale-95">
            Войти
          </button>
        </div>
      </nav>

      <main className="relative z-10 flex flex-col items-center justify-center px-6 pt-24 pb-32 text-center">
        <div className="inline-flex items-center gap-2 rounded-full border border-emerald-500/30 bg-emerald-500/5 px-3 py-1 text-xs font-medium text-emerald-400 mb-8 animate-fade-in">
          <span className="relative flex h-2 w-2">
            <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-emerald-400 opacity-75"></span>
            <span className="relative inline-flex rounded-full h-2 w-2 bg-emerald-500"></span>
          </span>
          v1.0.0-beta уже доступна
        </div>

        <h1 className="max-w-4xl text-5xl font-extrabold tracking-tight sm:text-7xl mb-8 leading-[1.1]">
          Интеллектуальный поиск <br />
          <span className="bg-gradient-to-r from-emerald-400 via-emerald-200 to-blue-400 bg-clip-text text-transparent">
            Open Source решений
          </span>
        </h1>

        <p className="max-w-2xl text-lg text-zinc-400 mb-12 leading-relaxed">
          Анализируйте &quot;здоровье&quot; библиотек, проверяйте лицензионную
          совместимость и управляйте своими зависимостями на основе данных от
          PyPI и Docker Hub.
        </p>

        <div className="flex flex-col sm:flex-row gap-4 items-center justify-center">
          <Link
            href="/dashboard"
            className="group relative flex h-12 w-full sm:w-auto items-center justify-center gap-2 overflow-hidden rounded-xl bg-emerald-500 px-8 text-sm font-bold text-black transition-all hover:shadow-[0_0_30px_rgba(16,185,129,0.4)] active:scale-95"
          >
            Начать поиск
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="18"
              height="18"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              strokeWidth="2.5"
              strokeLinecap="round"
              strokeLinejoin="round"
              className="transition-transform group-hover:translate-x-1"
            >
              <path d="M5 12h14" />
              <path d="m12 5 7 7-7 7" />
            </svg>
          </Link>
          <Link
            href="#docs"
            className="flex h-12 w-full sm:w-auto items-center justify-center rounded-xl border border-white/10 bg-white/5 px-8 text-sm font-bold backdrop-blur-md transition-all hover:bg-white/10 active:scale-95"
          >
            Документация
          </Link>
        </div>

        {/* Feature Grid Mockup */}
        <div className="mt-32 w-full max-w-6xl grid grid-cols-1 md:grid-cols-3 gap-6">
          <div className="rounded-2xl border border-white/5 bg-zinc-900/50 p-8 text-left backdrop-blur-sm hover:border-emerald-500/20 transition-colors">
            <div className="h-10 w-10 rounded-lg bg-emerald-500/10 flex items-center justify-center text-emerald-400 mb-4">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="24"
                height="24"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                strokeWidth="2"
                strokeLinecap="round"
                strokeLinejoin="round"
              >
                <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10" />
              </svg>
            </div>
            <h3 className="text-xl font-bold mb-2">Безопасность</h3>
            <p className="text-zinc-400 text-sm leading-relaxed">
              Автоматическое выявление уязвимостей и рисков в цепочке поставок
              ПО.
            </p>
          </div>
          <div className="rounded-2xl border border-white/5 bg-zinc-900/50 p-8 text-left backdrop-blur-sm hover:border-blue-500/20 transition-colors">
            <div className="h-10 w-10 rounded-lg bg-blue-500/10 flex items-center justify-center text-blue-400 mb-4">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="24"
                height="24"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                strokeWidth="2"
                strokeLinecap="round"
                strokeLinejoin="round"
              >
                <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14" />
                <polyline points="22 4 12 14.01 9 11.01" />
              </svg>
            </div>
            <h3 className="text-xl font-bold mb-2">Здоровье кода</h3>
            <p className="text-zinc-400 text-sm leading-relaxed">
              Интеллектуальный скоринг активности, поддержки и документации
              библиотек.
            </p>
          </div>
          <div className="rounded-2xl border border-white/5 bg-zinc-900/50 p-8 text-left backdrop-blur-sm hover:border-emerald-500/20 transition-colors">
            <div className="h-10 w-10 rounded-lg bg-emerald-500/10 flex items-center justify-center text-emerald-400 mb-4">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="24"
                height="24"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                strokeWidth="2"
                strokeLinecap="round"
                strokeLinejoin="round"
              >
                <rect width="18" height="11" x="3" y="11" rx="2" ry="2" />
                <path d="M7 11V7a5 5 0 0 1 10 0v4" />
              </svg>
            </div>
            <h3 className="text-xl font-bold mb-2">Лицензии</h3>
            <p className="text-zinc-400 text-sm leading-relaxed">
              Контроль соответствия юридическим требованиям вашего бизнеса.
            </p>
          </div>
        </div>
      </main>

      <footer className="relative z-10 border-t border-white/5 py-12 text-center text-zinc-500 text-sm">
        <p>© 2026 StackScout Team. Сделано для инновационных команд.</p>
      </footer>
    </div>
  );
}
