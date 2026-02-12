# SIMOD — Sistema de Monitoramento Domiciliar para Pacientes Pós-AVC

O **SIMOD** é um sistema voltado para **monitoramento domiciliar** e **acompanhamento** de pacientes **pós-AVC**, com foco em organização de rotinas (checklists/lembretes), registro de sinais vitais e suporte a triagem de emergência.

---

## Principais funcionalidades (visão do usuário)

- **Autenticação**
  - Login
  - Cadastro (Paciente / Cuidador / Profissional)
  - Etapas específicas para Profissional (área e registro)

- **Home**
  - Saudação + visão do dia
  - Checklist de hoje (visual)
  - Acesso rápido para:
    - **Registrar Sinais Vitais**
    - **Diário**
    - **Lembretes**
    - **Emergência e Triagem (FAST)**

- **Registro de Sinais Vitais**
  - Pressão arterial (sistólica/diastólica)
  - Frequência cardíaca
  - Saturação de O₂
  - Glicemia
  - Botão **Salvar** (fluxo inicial / base funcional)

- **Diário de Saúde**
  - Tela básica de diário com checklist/itens do dia (versão inicial)

- **Lembretes**
  - Tela básica para criar/visualizar lembretes (versão inicial)

- **Emergência e Triagem**
  - Orientação baseada no protocolo **FAST**
  - Ação de confirmação/ligação (fluxo inicial)

- **Menu inferior**
  - Navegação entre **Início**, **Sinais**, **Diário**, **Menu** (versão inicial)

---

## Stack / Tecnologias

**Backend**
- **Java 25 (LTS) + Spring Boot**
  - Framework robusto com injeção de dependências e ecossistema maduro

**Segurança**
- **Spring Security + JWT**
  - Autenticação stateless e escalável para APIs REST

**Persistência**
- **JPA/Hibernate + PostgreSQL**
  - Mapeamento objeto-relacional com integridade de dados

**API**
- **API REST**
  - Comunicação padronizada e independente de plataforma

**Frontend Android**
- **Android (MVVM)**
  - Separação de responsabilidades e maior testabilidade

**Versionamento**
- **Git + GitHub**
  - Controle de versão e colaboração via Pull Requests

---

## Estrutura do repositório (alto nível)

- `backend/` → API em Spring Boot (segurança, serviços, repositórios, migrations)
- `frontend/` → App Android (UI, Activities/ViewModels, resources)
- `docs/` → Documentação do projeto (manuais, anexos e artefatos)

---

## Como executar (visão rápida)

### 1) Backend (Spring Boot)
Pré-requisitos:
- Java
- Maven
- PostgreSQL

Passos (exemplo):
1. Configure o banco (PostgreSQL) e as variáveis de ambiente (ou `application.properties`).
2. Rode a aplicação:
   - `mvn spring-boot:run`

---

### 2) Frontend (Android)
Pré-requisitos:
- Android Studio (SDK instalado)
- Gradle (via Android Studio)

Passos:
1. Abra a pasta `frontend/` no Android Studio.
2. Aguarde o Gradle sincronizar.
3. Execute no emulador ou dispositivo físico.

---

## Arquitetura (resumo de qualidade)

- **Backend em camadas (Layers)**: controllers → services → repositories → banco  
- **Cliente-Servidor**: app Android (cliente) consumindo API REST (servidor)
- **Android com MVVM**: separação UI/Estado/Regra, favorecendo manutenção e testes
- **Segurança stateless com JWT**: controle de acesso para endpoints protegidos
- **Versionamento por branches + PR**: mudanças entram na `main` via revisão/merge

---
