# Procgen-world - Procedural World Generator

> Generate, persist and share procedural worlds

## Stack

- Backend : Java 21 - Spring Boot 4.x - PostgreSQL
- Frontend : Angular 20
- Infra : Docker compose - Github Actions

## Why this project?

I've always been fascinated by procedural generation, the idea that a single number (a seed) can deterministically produce an entire world, with its own geography, biomes and history.

Procgen-world is my starting exploration of that space. 
Rather than switching to a more "game-oriented" stack, I deliberately kept my usual tools (Java/Angular) to focus on the algorithmic challenges rather than learning a new ecosystem.
This is an intentional tradeoff, not an oversight.

Through this project I'm exploring:
- Coherent noise algorithms (Perlin Noise, octaves)
- Procedural content generation (biomes, cities)
- Full-stack architecture across few incremental phases

## Roadmap
- [ ] Phase 1 - Pure Java generation engine
- [ ] Phase 2 - Spring Boot REST API + PostgreSQL
- [ ] Phase 3 - Angular frontend + Canvas 2D
- [ ] Phase 4 - ...

> Full documentation coming soon.