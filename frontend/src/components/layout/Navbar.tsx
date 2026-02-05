"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import { AppBar, Toolbar, Button, Box, Container, Typography } from "@mui/material";
import { Search, Dashboard, Home, Info } from "@mui/icons-material";

export default function Navbar() {
  const pathname = usePathname();

  const links = [
    { href: "/", label: "Главная", icon: Home },
    { href: "/explore", label: "Исследовать", icon: Search },
    { href: "/dashboard", label: "Аналитика", icon: Dashboard },
    { href: "/about", label: "О нас", icon: Info },
  ];

  return (
    <AppBar
      position="fixed"
      elevation={0}
      sx={{
        bgcolor: 'background.paper',
        borderBottom: '1px solid',
        borderColor: 'divider',
      }}
    >
      <Container maxWidth="lg">
        <Toolbar disableGutters sx={{ justifyContent: 'space-between' }}>
          <Link href="/" style={{ textDecoration: 'none' }}>
            <Typography
              variant="h6"
              sx={{
                fontWeight: 700,
                color: 'primary.main',
                fontFamily: 'var(--font-outfit)',
                letterSpacing: '-0.02em',
              }}
            >
              StackScout
            </Typography>
          </Link>

          <Box sx={{ display: 'flex', gap: 1 }}>
            {links.map((link) => {
              const Icon = link.icon;
              const isActive = pathname === link.href;

              return (
                <Link key={link.href} href={link.href} style={{ textDecoration: 'none' }}>
                  <Button
                    startIcon={<Icon />}
                    variant={isActive ? "contained" : "text"}
                    sx={{
                      color: isActive ? 'white' : 'text.primary',
                      '&:hover': {
                        bgcolor: isActive ? 'primary.dark' : 'action.hover',
                      },
                    }}
                  >
                    {link.label}
                  </Button>
                </Link>
              );
            })}
          </Box>
        </Toolbar>
      </Container>
    </AppBar>
  );
}
