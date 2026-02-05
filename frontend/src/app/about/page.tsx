import type { Metadata } from "next";
import { Container, Box, Typography, Card, CardContent, Stack, Chip, Divider } from '@mui/material';
import { 
  RocketLaunch, 
  Visibility, 
  EmojiEvents,
  Code,
  Security,
  Speed,
  Public,
  GitHub
} from '@mui/icons-material';

export const metadata: Metadata = {
  title: "О нас | StackScout",
  description: "Узнайте больше о StackScout — профессиональной платформе для анализа Open Source библиотек",
};

export default function AboutPage() {
  const technologies = [
    'Next.js 15',
    'Spring Boot 3',
    'PostgreSQL',
    'Redis',
    'TypeScript',
    'Material-UI',
    'Docker',
    'REST API',
  ];

  const values = [
    {
      icon: Code,
      title: 'Open Source First',
      description: 'Мы верим в силу открытого кода и вносим вклад в сообщество',
    },
    {
      icon: Security,
      title: 'Безопасность',
      description: 'Защита данных и конфиденциальность — наши главные приоритеты',
    },
    {
      icon: Speed,
      title: 'Производительность',
      description: 'Быстрый и эффективный анализ тысяч библиотек в реальном времени',
    },
    {
      icon: Public,
      title: 'Доступность',
      description: 'Делаем аналитику Open Source доступной для всех разработчиков',
    },
  ];

  const features = [
    {
      title: 'Многоплатформенная поддержка',
      description: 'Анализ библиотек из npm, Maven, PyPI, NuGet и других популярных экосистем',
    },
    {
      title: 'Глубокая аналитика',
      description: 'Метрики качества кода, безопасности, производительности и активности сообщества',
    },
    {
      title: 'Автоматизация',
      description: 'CI/CD интеграция для автоматической проверки зависимостей в ваших проектах',
    },
    {
      title: 'Предиктивный анализ',
      description: 'ML-модели для прогнозирования надёжности и долговечности библиотек',
    },
  ];

  return (
    <Box sx={{ minHeight: '100vh', pt: 12, pb: 8 }}>
      <Container maxWidth="lg">
        {/* Header */}
        <Box sx={{ textAlign: 'center', mb: 10 }}>
          <Typography
            variant="h1"
            sx={{
              mb: 3,
              fontSize: { xs: '2.5rem', md: '4rem' },
              fontWeight: 800,
              background: 'linear-gradient(135deg, #4caf50 0%, #66bb6a 100%)',
              WebkitBackgroundClip: 'text',
              WebkitTextFillColor: 'transparent',
            }}
          >
            О StackScout
          </Typography>
          <Typography
            variant="h5"
            color="text.secondary"
            sx={{ maxWidth: '800px', mx: 'auto', lineHeight: 1.7 }}
          >
            Профессиональная платформа для анализа и мониторинга Open Source библиотек
          </Typography>
        </Box>

        {/* Mission */}
        <Card
          elevation={0}
          sx={{
            mb: 8,
            p: { xs: 4, md: 6 },
            backgroundImage: `linear-gradient(135deg, rgba(26, 26, 26, 0.9) 0%, rgba(55, 100, 80, 0.25) 100%), repeating-linear-gradient(90deg, transparent, transparent 2px, rgba(76, 175, 80, 0.02) 2px, rgba(76, 175, 80, 0.02) 4px)`,
            border: '1px solid',
            borderColor: 'rgba(76, 175, 80, 0.2)',
          }}
        >
          <Box
            sx={{
              display: 'grid',
              gridTemplateColumns: { xs: '1fr', md: 'repeat(2, 1fr)' },
              gap: 6,
              alignItems: 'center',
            }}
          >
            <Box>
              <Box sx={{ display: 'flex', alignItems: 'center', mb: 3 }}>
                <RocketLaunch sx={{ fontSize: 48, color: 'primary.main', mr: 2 }} />
                <Typography variant="h3" fontWeight={700}>
                  Наша миссия
                </Typography>
              </Box>
              <Typography variant="body1" sx={{ fontSize: '1.1rem', lineHeight: 1.8, color: 'text.secondary' }}>
                Мы создаём инструменты, которые помогают разработчикам и командам принимать 
                обоснованные решения о выборе Open Source зависимостей. Наша цель — сделать 
                экосистему открытого ПО более безопасной, прозрачной и доступной для всех.
              </Typography>
            </Box>
            <Box>
              <Box sx={{ display: 'flex', alignItems: 'center', mb: 3 }}>
                <Visibility sx={{ fontSize: 48, color: 'primary.main', mr: 2 }} />
                <Typography variant="h3" fontWeight={700}>
                  Наше видение
                </Typography>
              </Box>
              <Typography variant="body1" sx={{ fontSize: '1.1rem', lineHeight: 1.8, color: 'text.secondary' }}>
                Мы стремимся стать главным источником аналитической информации о Open Source 
                библиотеках, предоставляя разработчикам данные для выбора надёжных, 
                безопасных и поддерживаемых зависимостей.
              </Typography>
            </Box>
          </Box>
        </Card>

        {/* Values */}
        <Box sx={{ mb: 10 }}>
          <Typography
            variant="h2"
            sx={{
              mb: 6,
              fontWeight: 700,
              textAlign: 'center',
              fontSize: { xs: '2rem', md: '3rem' },
            }}
          >
            Наши ценности
          </Typography>
          <Box
            sx={{
              display: 'grid',
              gridTemplateColumns: { xs: '1fr', sm: 'repeat(2, 1fr)', md: 'repeat(4, 1fr)' },
              gap: 4,
            }}
          >
            {values.map((value, index) => {
              const Icon = value.icon;
              return (
                <Box key={index}>
                  <Card
                    elevation={0}
                    sx={{
                      height: '100%',
                      textAlign: 'center',
                      p: 3,
                      border: '1px solid',
                      borderColor: 'divider',
                      transition: 'all 0.3s ease',
                      '&:hover': {
                        borderColor: 'primary.main',
                        transform: 'translateY(-4px)',
                      },
                    }}
                  >
                    <Box
                      sx={{
                        display: 'inline-flex',
                        p: 2,
                        borderRadius: '50%',
                        bgcolor: 'primary.main',
                        color: 'white',
                        mb: 2,
                      }}
                    >
                      <Icon sx={{ fontSize: 32 }} />
                    </Box>
                    <Typography variant="h6" gutterBottom fontWeight={600}>
                      {value.title}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                      {value.description}
                    </Typography>
                  </Card>
                </Box>
              );
            })}
          </Box>
        </Box>

        {/* What We Offer */}
        <Box sx={{ mb: 10 }}>
          <Typography
            variant="h2"
            sx={{
              mb: 2,
              fontWeight: 700,
              textAlign: 'center',
              fontSize: { xs: '2rem', md: '3rem' },
            }}
          >
            Что мы предлагаем
          </Typography>
          <Typography
            variant="h6"
            color="text.secondary"
            sx={{ mb: 6, textAlign: 'center', maxWidth: '700px', mx: 'auto' }}
          >
            StackScout предоставляет комплексное решение для анализа Open Source библиотек
          </Typography>
          <Box
            sx={{
              display: 'grid',
              gridTemplateColumns: { xs: '1fr', md: 'repeat(2, 1fr)' },
              gap: 3,
            }}
          >
            {features.map((feature, index) => (
              <Box key={index}>
                <Card
                  elevation={0}
                  sx={{
                    height: '100%',
                    p: 3,
                    bgcolor: 'background.paper',
                    border: '1px solid',
                    borderColor: 'divider',
                  }}
                >
                  <CardContent>
                    <Typography variant="h5" gutterBottom fontWeight={600} color="primary.main">
                      {feature.title}
                    </Typography>
                    <Typography variant="body1" color="text.secondary" sx={{ lineHeight: 1.7 }}>
                      {feature.description}
                    </Typography>
                  </CardContent>
                </Card>
              </Box>
            ))}
          </Box>
        </Box>

        {/* Tech Stack */}
        <Card
          elevation={0}
          sx={{
            p: { xs: 4, md: 6 },
            mb: 8,
            border: '1px solid',
            borderColor: 'divider',
          }}
        >
          <Box sx={{ textAlign: 'center', mb: 4 }}>
            <EmojiEvents sx={{ fontSize: 48, color: 'primary.main', mb: 2 }} />
            <Typography variant="h3" fontWeight={700} gutterBottom>
              Современный технологический стек
            </Typography>
            <Typography variant="body1" color="text.secondary" sx={{ fontSize: '1.1rem' }}>
              StackScout построен с использованием передовых технологий для обеспечения 
              максимальной производительности и надёжности
            </Typography>
          </Box>
          <Divider sx={{ my: 4 }} />
          <Stack direction="row" spacing={2} flexWrap="wrap" justifyContent="center" useFlexGap>
            {technologies.map((tech, index) => (
              <Chip
                key={index}
                label={tech}
                variant="outlined"
                sx={{
                  fontSize: '1rem',
                  py: 2.5,
                  px: 1,
                  borderColor: 'primary.main',
                  color: 'primary.main',
                  fontWeight: 600,
                }}
              />
            ))}
          </Stack>
        </Card>

        {/* Open Source */}
        <Card
          elevation={0}
          sx={{
            p: { xs: 4, md: 6 },
            textAlign: 'center',
            backgroundImage: `linear-gradient(135deg, rgba(26, 26, 26, 0.8) 0%, rgba(55, 100, 80, 0.2) 100%)`,
            border: '1px solid',
            borderColor: 'rgba(76, 175, 80, 0.3)',
          }}
        >
          <GitHub sx={{ fontSize: 64, color: 'primary.main', mb: 2 }} />
          <Typography variant="h3" fontWeight={700} gutterBottom>
            Открытый исходный код
          </Typography>
          <Typography
            variant="body1"
            color="text.secondary"
            sx={{ mb: 3, maxWidth: '700px', mx: 'auto', fontSize: '1.1rem', lineHeight: 1.8 }}
          >
            StackScout — это open source проект. Мы верим в прозрачность и приветствуем вклад 
            сообщества. Наш код доступен на GitHub, и мы рады любым улучшениям и предложениям.
          </Typography>
          <Box sx={{ display: 'flex', gap: 2, justifyContent: 'center', flexWrap: 'wrap' }}>
            <Chip label="MIT License" color="primary" />
            <Chip label="Community Driven" color="primary" />
            <Chip label="Actively Maintained" color="primary" />
          </Box>
        </Card>
      </Container>
    </Box>
  );
}
