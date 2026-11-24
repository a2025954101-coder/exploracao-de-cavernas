# exploracao-de-cavernas

## Como Executar o Projeto

Este projeto utiliza a biblioteca **Processing** em Java. Há duas formas de compilá-lo e executá-lo: com a dependência Processing disponível no cache local do Maven, ou com o `core.jar` incluído no projeto.

Escolha o método e o seu sistema operacional abaixo:

---

### Opção 1: Dependência do Maven em cache (compilação com `javac`)

> **Pré-requisito:** É necessário ter o JDK e o arquivo `processing-core-4-4.5.0.jar` no cache local do Maven. Os scripts abaixo chamam `javac` e `java` diretamente; eles não executam o Maven.

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

