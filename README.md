# Mining Simulation (Java Multithreaded App)

Simulace těžebního dolu implementovaná v jazyce Java s využitím balíčku java.util.concurrent. Aplikace demonstruje paralelní zpracování, synchronizaci vláken a koordinaci sdílených zdrojů v prostředí připomínajícím reálný provoz dolu.

## Popis projektu

Tato aplikace simuluje provoz těžebního dolu, kde více workerů (vláken) současně:

- těží suroviny
- transportují materiál
- zpracovávají vytěžené zdroje

Každá část systému běží paralelně a sdílí omezené zdroje, což vytváří realistické scénáře synchronizace a potenciálních konfliktů.
Vstupem této aplikace je soubor, definující zdroje a následně proběhne paralelní simulace, kde výstupem je log operací, které probíhali.

Více informací o projektu ve složce [/doc](https://github.com/cernyfili/FAV_2324_PGS_u1-MultiThreadAppMineSimulation/tree/master/doc)
