# exploracao-de-cavernas

## Como Executar o Projeto

Este projeto utiliza a biblioteca **Processing** em Java e pode ser executado de duas formas: utilizando o **Maven** (que consome a instalação local do Processing) ou via **Scripts Diretos** (utilizando o `core.jar` incluído no projeto).

Escolha o método e o seu sistema operacional abaixo:

---

### Opção 1: Execução com Maven

> **Pré-requisito:** É necessário ter o Maven instalado e a biblioteca/instalação do Processing configurada no seu sistema.

#### 🐧 Linux
Execute o script bash via terminal:
```bash
./runLinux.sh
```

Se não funcionar, pode ser necessário rodar isso antes:
```bash
chmod +x runLinux.sh
```

#### 🪟 Windows
Execute o arquivo batch pelo prompt de comando (CMD) ou PowerShell:
```DOS
./runWindows.bat
```

---

### Opção 2: Execução Standalone (Sem Maven)
> **Pré-requisito:** Esta opção utiliza o arquivo core.jar do Processing que já está incluído no repositório do projeto.

#### 🐧 Linux
Execute o script bash via terminal:
```
./coreLinux.sh
```

Se não funcionar, pode ser necessário rodar isso antes:

```bash
chmod +x coreLinux.sh
```

#### 🪟 Windows
Execute o arquivo batch pelo prompt de comando (CMD) ou PowerShell:
```DOS
./coreWindows.bat
```

---

