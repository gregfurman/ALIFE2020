Language Transmission in Artificial Creole Evolution

Abstract

This study applied the synthetic methodology to agent-based artificial
language evolution. The goal was to elucidate key environmental
mechanisms responsible for language
transmission between population groups in creole evolution.
Experiments evaluated the impact of varying portions of native
lexicons in an agent population, on the evolution of agent lexical
similarity thresholds, acceptance and transference of characters from
different lexicons to a common lexicon (creole) needed for resource
consumption.


#Experiment index and details

index,A,B,C,FitnessA,stationary
1,50,50,50,5,true
2,50,50,50,15,true
3,20,65,65,5,true
4,20,65,65,15,true
5,50,50,50,5,false
6,50,50,50,15,false
7,20,65,65,5,false
8,20,65,65,15,false

#SimulationGraphic.png
- Yellow squares - resources
- Black dots - agenst
- Grey circles - talking radii
- Therefore, agents (black dots) within grey circles surrounding resources are participating in 'talking games' over said resources

#Definitions
- Initially -> Refers to Generation 0 of the language evolution simulation

- Similarity Threshold -> A Similarity Threshold dictates what proportion of a candidate lexical term for a resource should be comprised of an agents own lexicon.

- Similarity -> A proportion between 0 and 1 defining how many lexical units 2 agents share with one another divided by the total amount of letters in an agent's genome. If agent A checks how similar it is to agent B the formula would be as follows: 
count(A elements ∩ B elements) / count(A elements)

#Simulation/Environment Constants
- Amount of Resources = 20

- Talking Radius = closest 200 cells surrounding an agent

- Type A Genome: [a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z]

- Type B Genome: [&, ', (, ), *, +, ,, -, ., /, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, :, ;, <, =, >, ?]

- Type C Genome: [A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z]


#Agent Movements
Agents will randomly move north, south, east or west. Agents movements will occur so long as there are resources in the environment. After All resources have been consumed (via language games) breeding and crossover will take place.  

#Language Game
Upon an agent encountering a resource, an agent will look in an area of 200 cells to look for agents to communicate over the resource with. After finding 1-8 agents, a language game commences in which a random agent will give a candidate term for a resource that is fully comprised of their own lexicon. A NEAT network takes in the a proportion of similarity

#Experiment 1
All agent types were initially equally represented in the population.
Agents moved about a 40x40 grid and communicated with one another over resources.
Upon a simulation beginning, each agent was given an equal amount of fitness.

#Experiment 2
All agent types were initially equally represented in the population.
Agents moved about a 40x40 grid and communicated with one another over resources.
Upon a simulation beginning, agents of type A were given 3x more fitness than agents B and C, who were given 5. Any agent with a lexicon comprised of 50% or more language type A was given this extra fitness.

#Experiment 3
All agent types were not initially equally represented in the population with 20 type A agents, 65 type B agents and 65 type C.
Agents moved about a 40x40 grid and communicated with one another over resources.
Upon a simulation beginning, each agent was given an equal amount of fitness.

#Experiment 4
All agent types were not initially equally represented in the population with 20 type A agents, 65 type B agents and 65 type C.
Agents moved about a 40x40 grid and communicated with one another over resources.
Upon a simulation beginning, agents of type A were given 3x more fitness than agents B and C, who were given 5. Any agent with a lexicon comprised of 50% or more language type A was given this extra fitness.

#Experiment 5
All agent types were initially equally represented in the population.
In the first 500 generations, agents of type A and B moved about a 40x20 grid afterwhich 50 agents with a majority lexicon of type A moved into the an environment of equal size as the initial but comrpising of 50 type B agents.
Upon a simulation beginning, each agent was given an equal amount of fitness.

#Experiment 6
All agent types were initially equally represented in the population.
In the first 500 generations, agents of type A and B moved about a 40x20 grid afterwhich 50 agents with a majority lexicon of type A moved into the an environment of equal size as the initial but comrpising of 50 type B agents.
Upon a simulation beginning, agents of type A were given 3x more fitness than agents B and C, who were given 5. Any agent with a lexicon comprised of 50% or more language type A was given this extra fitness.

#Experiment 7
All agent types were not initially equally represented in the population with 20 type A agents, 65 type B agents and 65 type C.
In the first 500 generations, agents of type A and B moved about a 40x20 grid afterwhich 50 agents with a majority lexicon of type A moved into the an environment of equal size as the initial but comrpising of 50 type B agents.
Upon a simulation beginning, each agent was given an equal amount of fitness.

#Experiment 8
All agent types were not initially equally represented in the population with 20 type A agents, 65 type B agents and 65 type C.
In the first 500 generations, agents of type A and B moved about a 40x20 grid afterwhich 50 agents with a majority lexicon of type A moved into the an environment of equal size as the initial but comrpising of 50 type B agents.
Upon a simulation beginning, agents of type A were given 3x more fitness than agents B and C, who were given 5. Any agent with a lexicon comprised of 50% or more language type A was given this extra fitness.


