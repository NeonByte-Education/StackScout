"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import { Search, LayoutDashboard, Home, ShieldCheck } from "lucide-react";

export default function Navbar() {
  const pathname = usePathname();

  const links = [
    { href: "/", label: "CORE", icon: Home },
    { href: "/explore", label: "EXPLORE", icon: Search },
    { href: "/dashboard", label: "STATS", icon: LayoutDashboard },
  ];

  return (
    <nav className="fixed bottom-8 left-1/2 -translate-x-1/2 z-50">
      <div className="glass-panel px-6 py-3 rounded-full flex items-center gap-8 border-cyber-blue/20 shadow-[0_0_30px_rgba(0,0,0,0.8)]">
        {links.map((link) => {
          const Icon = link.icon;
          const isActive = pathname === link.href;

          return (
            <Link
              key={link.href}
              href={link.href}
              className={`flex items-center gap-2 font-mono text-sm transition-all duration-300 ${
                isActive
                  ? "text-cyber-blue"
                  : "text-slate-500 hover:text-slate-300"
              }`}
            >
              <Icon size={18} className={isActive ? "animate-pulse" : ""} />
              <span className="hidden md:inline">{link.label}</span>
              {isActive && (
                <span className="w-1 h-1 bg-cyber-blue rounded-full absolute -top-1 left-1/2 -translate-x-1/2" />
              )}
            </Link>
          );
        })}
        <div className="w-px h-6 bg-white/10 mx-2 hidden md:block" />
        <div className="flex items-center gap-2 text-cyber-orange text-xs font-mono animate-pulse">
          <ShieldCheck size={16} />
          <span className="hidden lg:inline">SYSTEM_SECURE</span>
        </div>
      </div>
    </nav>
  );
}
