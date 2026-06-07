# Academiau

Aplicativo web progressivo (PWA) para acompanhamento de treinos de musculação na divisão **Push, Pull, Legs (ABC)**.

## Funcionalidades

- **Dashboard** com sugestao automatica de treino do dia (Push, Pull ou Legs)
- **+1300 exercicios** com GIFs animados demonstrativos
- **Filtros avancados** por grupo muscular, equipamento e busca por nome
- **Rotinas personalizaveis** com controle de series, repeticoes e carga
- **Historico de treinos** com acompanhamento de progresso
- **PWA** — instale no celular como um app nativo
- **Funciona offline** apos o primeiro carregamento

## Tecnologias

- HTML5, CSS3, JavaScript (Vanilla)
- Service Worker para cache offline
- Web App Manifest para instalacao como PWA

## Como usar

### Online

Acesse: [https://gabrielrcaldeira.github.io/Academiau/](https://gabrielrcaldeira.github.io/Academiau/)

### Localmente

```bash
git clone https://github.com/gabrielrcaldeira/Academiau.git
cd Academiau
```

Abra o `index.html` em qualquer navegador ou use um servidor local:

```bash
npx serve .
```

## Estrutura do Projeto

```
Academiau/
├── index.html              # Pagina principal
├── app.js                  # Logica do aplicativo
├── style.css               # Estilos e tema escuro
├── sw.js                   # Service Worker (cache offline)
├── manifest.json           # Manifesto PWA
├── icon.svg                # Icone do app
├── gifs/                   # +1300 GIFs de exercicios
│   └── mapping_metadata.json  # Metadados dos exercicios
└── README.md
```

## Divisao de Treino

| Dia | Treino | Foco |
|-----|--------|------|
| A | **Push** | Peito, Ombro, Triceps |
| B | **Pull** | Costas, Biceps, Antebraco |
| C | **Legs** | Quadriceps, Posterior, Gluteo, Panturrilha |

## Licenca

Este projeto e de uso pessoal.
