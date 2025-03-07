#Autorzy: Łukasz Gałuszewski 247660, Julia Nocuń 247744

from collections import deque   #kolejka FIFO
import time

def bfs(board, start_board, order):

    max_recursion_depth = 0
    start_time = time.time()    #czas rozpoczęcia algorytmu
    visited_states = deque()    #lista stanów otwartych
    processed_states = set()    #lista stanów zamkniętych

    if board.solved(start_board):   #sprawdzamy czy początkowa plansza jest rozwiązaniem
        duration = time.time() - start_time
        return start_board, 1, 0, max_recursion_depth, duration

    visited_states.append(start_board)  
    processed_states.add(start_board)   

    while visited_states:  #petle wykonujemy dopóki w kolejce są nieprzetworzone stany
        node = visited_states.popleft()  #pobiera stan z przodu kolejki
        max_recursion_depth = max(max_recursion_depth, node.depth)  #aktualizujemy maksymalną głębokość rekursji
        for neighbour in board.neighbours(node, order):  
            if neighbour not in processed_states:  #jesli sąsiad nie został przetworzony:
                if board.solved(neighbour):  
                    vs = len(visited_states) + len(processed_states)  
                    ps = len(processed_states)  
                    duration = time.time() - start_time  
                    return neighbour, vs, ps, max_recursion_depth + 1, duration
                visited_states.append(neighbour)  #jesli sasiad nie jest stanem docelowym dodajemy go do listy stanów odwiedzonych
                processed_states.add(neighbour)  #oraz listy stanów przetworzonych 
    vs = len(visited_states) + len(processed_states)
    ps = len(processed_states)
    duration = time.time() - start_time
    return None, vs, ps, max_recursion_depth, duration  #gdy rozwiązanie nie zostało znalezione zwracamy None 
