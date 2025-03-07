#Autorzy: Łukasz Gałuszewski 247660, Julia Nocuń 247744

import time

MAX_POSSIBLE_DEPTH = 21

def dfs(board, start_board, order):

    start_time = time.time()
    max_recursion_depth = 0  
    visited_states = [(start_board, 0)]     #uzywamy listy jako stosu/push (append()) i pop() - lista stanow otwartych
    processed_states = set()

    while visited_states:
        node, depth = visited_states.pop()  #pobieramy stan z wierzchołka stosu

        if board.solved(node):
            return node, len(visited_states) + len(processed_states), len(processed_states), max_recursion_depth, time.time() - start_time

        processed_states.add(tuple(map(tuple, node.board)))

        if depth < MAX_POSSIBLE_DEPTH:

            for n in reversed(board.neighbours(node, order)):   #zgodnie z wymaganiami podanymi na laboratorium odwracamy kolejność sąsiadow, dlatego że stos jest LIFO
                if tuple(map(tuple, n.board)) not in processed_states:  #jeśli sąsiad nie był jeszcze przetworzony
                    new_node = board.Board(                             #to na jego podstawie tworzymy nową plansze
                          board=n.board,
                          parent=node,
                          path=n.path,
                          depth=node.depth + 1
                    )
                    visited_states.append((new_node, depth+1))      #stworzoną plansze dodajemy do listy stanow odwiedzonych 
                    max_recursion_depth = max(max_recursion_depth, depth+1) #nastepnie ktualizujemy maksymalną głębokość rekursji
    return None, len(visited_states) + len(processed_states), len(processed_states), max_recursion_depth, time.time() - start_time