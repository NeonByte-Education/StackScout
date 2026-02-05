export default function Home() {
  return (
    <div className="flex flex-col items-center justify-center min-h-screen px-4 py-20 text-center">
      <div className="relative group">
        <div className="absolute -inset-1 bg-linear-to-r from-cyber-blue to-cyber-orange rounded-lg blur opacity-25 group-hover:opacity-100 transition duration-1000 group-hover:duration-200"></div>
        <div className="relative glass-panel cyber-border px-8 py-10 rounded-lg">
          <h1 className="text-5xl md:text-7xl font-bold bg-clip-text text-transparent bg-linear-to-b from-white to-slate-500 mb-4">
            STACK<span className="text-cyber-blue">SCOUT</span>
          </h1>
          <p className="text-xl md:text-2xl text-cyber-blue/80 font-mono tracking-widest mb-8">
            [ INITIALIZING ANALYTICS CORE ]
          </p>
          <div className="flex flex-col md:flex-row gap-4 justify-center">
            <button className="px-8 py-3 bg-cyber-blue text-obsidian font-bold rounded-sm border border-cyber-blue/50 hover:bg-cyber-blue/80 hover:scale-105 transition-all duration-300 shadow-[0_0_15px_rgba(0,242,255,0.4)]">
              EXPLORE LIBRARIES
            </button>
            <button className="px-8 py-3 bg-transparent text-cyber-blue font-bold rounded-sm border border-cyber-blue hover:bg-cyber-blue/10 transition-all duration-300">
              LEARN MORE
            </button>
          </div>
        </div>
      </div>

      <div className="mt-20 grid grid-cols-1 md:grid-cols-3 gap-8 max-w-6xl w-full">
        <div className="glass-panel p-6 rounded-sm cyber-border border-white/5">
          <div className="text-cyber-orange mb-4 text-2xl">01 // MONITOR</div>
          <h3 className="text-xl font-bold mb-2">Health Tracking</h3>
          <p className="text-slate-400 text-sm">
            Real-time monitoring of package health and maintenance status across
            multiple ecosystems.
          </p>
        </div>
        <div className="glass-panel p-6 rounded-sm cyber-border border-white/5">
          <div className="text-cyber-blue mb-4 text-2xl">02 // SECURE</div>
          <h3 className="text-xl font-bold mb-2">License Compliance</h3>
          <p className="text-slate-400 text-sm">
            Automated legal risk assessment to ensure your project stays
            compliant with open source licenses.
          </p>
        </div>
        <div className="glass-panel p-6 rounded-sm cyber-border border-white/5">
          <div className="text-cyber-green mb-4 text-2xl">03 // ANALYZE</div>
          <h3 className="text-xl font-bold mb-2">Predictive Logic</h3>
          <p className="text-slate-400 text-sm">
            Advanced patterns detection to help you choose the most reliable
            dependencies for your stack.
          </p>
        </div>
      </div>
    </div>
  );
}
