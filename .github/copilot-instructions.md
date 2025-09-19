# Copilot Instructions - Card Store

This document provides comprehensive instructions for GitHub Copilot and other AI agents working on the `card-store` repository.

## Project Overview

This is a native Android application that allows users to store and manage their digital loyalty cards.
Everything is stored locally on the device, but can also be shared with other users via links.

### Key Features

- Local storage and management of digital loyalty cards
- Use in stores with QR codes to scan in stores
- Sharing cards with friends and family via QR codes
- Built with native technologies (Kotlin, Jetpack Compose)

## Development Workflow

### Essential Gradle Scripts

#### Code Formatting and Testing

```bash
# Format code (required before commit)
./gradlew ktfmt

# Compile (required before commit)
./gradlew assembleDebug
```

### Pre-commit Requirements

Before any commit, the following MUST pass:

1. `./gradlew ktfmtCheck` - Code formatting

## Commit Message Format

This project uses **Conventional Commits**:

### Format

```
<type>(scope): <description>

[optional body]

[optional footer(s)]
```

### Types

- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `refactor`: Code refactoring
- `test`: Adding or updating tests
- `build`: Maintenance tasks

### Examples

```bash
feat(card): add support for card icons
fix(share): handle missing data gracefully
docs(README): update installation instructions
refactor(navigation): update routes
```

## Code Conventions

### Function Organization

- Keep functions small and focused
- Use descriptive names that explain what the function does
- Place utility functions in appropriate `utils/` files

### Error Handling

- Provide meaningful error messages

## Copilot Agent

Everything required for Copilot Agent is already pre-installed via GitHub Actions:

- Java
- Android SDK
- ktfmt (already pre-run to initialize cache)
- Android build (already pre-run to initialize cache)
