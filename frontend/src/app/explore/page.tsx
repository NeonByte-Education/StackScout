"use client";

import { useState, useEffect } from "react";
import { libraryApi, Library } from "@/lib/api";

export default function ExplorePage() {
  const [libraries, setLibraries] = useState<Library[]>([]);
  const [query, setQuery] = useState("");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    fetchLibraries();
  }, []);

  const fetchLibraries = async () => {
    try {
      setLoading(true);
      const { data } = await libraryApi.getAll();
      setLibraries(data.libraries);
      setError(null);
    } catch (err) {
      setError("FAILED TO RETRIEVE SYNC DATA FROM CORE.");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!query) {
      fetchLibraries();
      return;
    }
    try {
      setLoading(true);
      const { data } = await libraryApi.search(query);
      setLibraries(data.libraries);
      setError(null);
    } catch (err) {
      setError("SEARCH ANOMALY DETECTED.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container mx-auto px-4 py-12">
      <div className="mb-12">
        <h2 className="text-3xl font-bold mb-2 flex items-center gap-2">
          <span className="w-2 h-8 bg-cyber-orange inline-block"></span>
          EXPLORE_REPOSITORIES
        </h2>
        <form onSubmit={handleSearch} className="flex gap-4 max-w-2xl">
          <input
            type="text"
            placeholder="[ SEARCH_STRING ]"
            className="flex-1 bg-obsidian border border-cyber-blue/30 px-6 py-3 rounded-sm focus:outline-none focus:border-cyber-blue transition-colors text-cyber-blue font-mono"
            value={query}
            onChange={(e) => setQuery(e.target.value)}
          />
          <button
            type="submit"
            className="px-8 py-3 bg-cyber-blue/20 text-cyber-blue border border-cyber-blue hover:bg-cyber-blue hover:text-obsidian transition-all"
          >
            SCAN
          </button>
        </form>
      </div>

      {loading ? (
        <div className="flex justify-center py-20">
          <div className="animate-spin text-cyber-blue text-4xl font-mono">
            [ UPDATING ]
          </div>
        </div>
      ) : error ? (
        <div className="glass-panel p-10 cyber-border border-red-500/30 text-red-500 text-center">
          <div className="text-2xl font-bold mb-2 pt-2">CRITICAL_ERROR</div>
          <p>{error}</p>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {libraries.map((lib) => (
            <div
              key={lib.id}
              className="glass-panel p-6 rounded-sm cyber-border border-white/5 group hover:border-cyber-blue/40 transition-all duration-500"
            >
              <div className="flex justify-between items-start mb-4">
                <span className="text-xs font-mono text-cyber-blue/60">
                  {lib.source} // v{lib.version}
                </span>
                <div
                  className={`w-3 h-3 rounded-full animate-pulse shadow-[0_0_8px] ${lib.healthScore > 80 ? "bg-cyber-green shadow-cyber-green" : "bg-cyber-orange shadow-cyber-orange"}`}
                ></div>
              </div>
              <h3 className="text-xl font-bold text-white mb-2 group-hover:text-cyber-blue transition-colors">
                {lib.name}
              </h3>
              <p className="text-slate-400 text-sm mb-6 line-clamp-2">
                {lib.description ||
                  "No tactical briefing available for this dependency."}
              </p>
              <div className="flex justify-between items-center pt-4 border-t border-white/5">
                <span className="text-sm font-mono text-slate-500">
                  HEALTH_INDEX: {lib.healthScore}%
                </span>
                <span className="text-xs px-2 py-1 bg-white/5 text-slate-300 font-mono tracking-tighter uppercase">
                  {lib.license || "PROPRIETARY"}
                </span>
              </div>
            </div>
          ))}
          {libraries.length === 0 && (
            <div className="col-span-full py-20 text-center text-slate-500 font-mono">
              [ NO MATCHES FOUND IN ACTIVE DATABASE ]
            </div>
          )}
        </div>
      )}
    </div>
  );
}
