
---

## 👥 Perfis de usuário

- **Paciente**: acesso às funcionalidades de acompanhamento e registro (ex.: sinais vitais, diário e rotinas).
- **Cuidador**: apoio na rotina do paciente e acompanhamento.
- **Profissional de saúde**: fluxos de cadastro/validação por tipo, e telas voltadas ao acompanhamento e vínculos.

> Observação: algumas telas podem estar em versão básica/placeholder conforme evolução incremental do projeto.

---

## ✅ Principais funcionalidades (versão atual)

### Autenticação e Cadastro
- Login
- Cadastro de usuário
- Seleção de perfil (Paciente/Cuidador/Profissional)
- Seleção de área profissional (quando aplicável)
- Finalização de cadastro (quando aplicável)

### Home
- Cards de acesso rápido
- Atalho para **Registrar Sinais Vitais**
- Atalho para **Diário**
- Atalho para **Lembretes**
- Botão de **Emergência**
- **Menu inferior** (navegação)

### Registrar Sinais Vitais
- Registro de:
  - Pressão arterial (sistólica/diastólica)
  - Frequência cardíaca
  - Saturação de O₂
  - Glicemia
  - Observação/anotação opcional
- Botão **Salvar**
- Mantém **menu inferior** na navegação

### Diário de Saúde (versão básica)
- Lista de itens/rotina (visual simples baseado na UI)
- Sem filtros avançados e sem botão de relatório (nesta versão)

### Lembretes (versão básica)
- Tela inicial de lembretes (baseada na UI)
- Fluxo simples para evolução incremental

### Emergência e Triagem
- Tela de triagem com foco em sinais FAST (baseado na UI)
- Botão de confirmar/acionar (fluxo pode variar conforme integração)

---

## 🧱 Arquitetura e qualidade (resumo)

### Estilo arquitetural (macro)
- **Cliente–Servidor**: App Android consome a API do backend.
- **Arquitetura em camadas (Layers)**:
  - **Apresentação** (UI Android / Controllers no backend)
  - **Negócio** (UseCases/Services)
  - **Dados** (Repositories/DAO + persistência)

### Organização (micro / boas práticas)
- **Backend**: separação por responsabilidades em pacotes (controllers, services, repositories, domain/dto, config).
- **Frontend (Android)**: organização por `ui/*` (telas) e camadas de `data/*` quando aplicável.
- Princípios aplicados:
  - **Baixo acoplamento e alta coesão**
  - Separação de responsabilidades por tela/módulo
  - Evolução incremental (features em branches + PR)

> Quando houver trade-offs (prazo vs. qualidade), as melhorias são registradas como **próximos passos** e/ou **issues**.

---

## 🧪 Evidências no repositório (para a disciplina)

Você pode apresentar evidências diretamente do GitHub:

- **README**: este arquivo descreve visão geral, execução e arquitetura.
- **Commits**: histórico com mensagens claras (ex.: “UI: home, vitals, diário…”).
- **Branches / Pull Requests**: desenvolvimento por feature (ex.: `feat-ui-screens`) + revisão via PR.
- **Issues**: registrar bugs, melhorias e tarefas pendentes (ex.: “persistir lembretes no backend”, “validação de campos em sinais vitais”).
- **Testes / Pipeline (CI)**:
  - Se houver GitHub Actions configurado, fica em **Actions**.
  - Caso ainda não exista, é recomendado criar um workflow simples (build do backend + build do app Android).

---

## ⚙️ Como executar o projeto (ambiente local)

### Pré-requisitos gerais
- **Git** (para clonar e contribuir)
- **Java JDK** (para backend; versão conforme o projeto)
- **Android Studio + Android SDK** (para o app)
- Conexão de rede liberada (o app usa INTERNET)

---

## 🔧 Backend (API)

> O backend fica na pasta `/backend`.

### Passos (geral)
1. Abra um terminal na pasta:
   ```bash
   cd backend
