import type { Metadata } from "next";
import { Inter, Outfit } from "next/font/google";
import "./globals.css";

const inter = Inter({
  variable: "--font-inter",
  subsets: ["latin", "cyrillic"],
});

const outfit = Outfit({
  variable: "--font-outfit",
  subsets: ["latin"],
});

export const metadata: Metadata = {
  title: "StackScout | Cyber-Industrial Intelligence",
  description: "Advanced analytics for Open Source ecosystems",
};

import Navbar from "@/components/layout/Navbar";

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="ru">
      <body
        className={`${inter.variable} ${outfit.variable} antialiased bg-cyber-black text-slate-200 min-h-screen bg-grid pb-24`}
      >
        <div className="fixed inset-0 pointer-events-none bg-linear-to-tr from-cyber-blue/5 via-transparent to-cyber-orange/5" />
        <Navbar />
        <main className="relative z-10">{children}</main>
      </body>
    </html>
  );
}
