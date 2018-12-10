# Incadium
Incadium is a roguelike game I am working on for both desktop and Android. I'm primarily using this as a learning experience for an Entity Component System Architecture.

![Image of game](https://i.imgur.com/lzc4k7l.png)

# Features
-Turn Based

-Lights, in differnt colors too!

-Randomly generated maps

-Pathfinding AI using Dijkstra Map, all enemy entites share a common map so that pathfinding only has to be calculated once per frame instead of once for each enemy

-Bitmasked walls in 8 directions

![Image of Dijkstra Map](https://i.imgur.com/z55UZOP.png)
Here's a visual of the Dijkstra Map, enemies will use gradient descent to find the player(Blue -> Yellow). 
