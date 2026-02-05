"use client";

import { useState, useEffect } from "react";
import { apiClient } from "@/lib/api";

export default function DashboardPage() {
  const [stats, setStats] = useState<any>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const { data } = await apiClient.get(
          "/libraries/stats",
        );
        setStats(data);
      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false);
      }
    };
    fetchStats();
  }, []);

  return (
    <div className="container mx-auto px-4 py-12">
      <h2 className="text-3xl font-bold mb-12 flex items-center gap-2">
        <span className="w-2 h-8 bg-cyber-blue inline-block"></span>
        SYSTEM_DIAGNOSTICS
      </h2>

      {loading ? (
        <div className="text-center font-mono py-20">
          [ CALCULATING_METRICS ]
        </div>
      ) : stats ? (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8">
          <StatCard
            label="TOTAL_LIBS"
            value={stats.totalLibraries}
            color="text-cyber-blue"
          />
          <StatCard
            label="AVG_HEALTH"
            value={`${Math.round(stats.averageHealthScore)}%`}
            color="text-cyber-green"
          />
          <StatCard
            label="PYPI_INDEX"
            value={stats.sources.pypi}
            color="text-cyber-orange"
          />
          <StatCard
            label="NPM_INDEX"
            value={stats.sources.npm}
            color="text-cyber-blue"
          />
        </div>
      ) : (
        <div className="glass-panel p-10 text-center font-mono text-slate-500">
          [ UNABLE TO SYNC WITH ANALYTICS ENGINE ]
        </div>
      )}

      <div className="mt-12 glass-panel p-8 cyber-border border-white/5 h-64 flex items-center justify-center text-slate-500 font-mono italic">
        [ DUAL_SCAN_OSCILLOSCOPE_VISUALIZATION_PLACEHOLDER ]
      </div>
    </div>
  );
}

function StatCard({
  label,
  value,
  color,
}: {
  label: string;
  value: any;
  color: string;
}) {
  return (
    <div className="glass-panel p-8 cyber-border border-white/5 hover:border-white/20 transition-all">
      <div className="text-xs font-mono text-slate-500 mb-2 tracking-widest">
        {label}
      </div>
      <div className={`text-4xl font-bold ${color}`}>{value}</div>
    </div>
  );
}
