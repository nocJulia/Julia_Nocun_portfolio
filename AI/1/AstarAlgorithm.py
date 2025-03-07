#Autorzy: Łukasz Gałuszewski 247660, Julia Nocuń 247744

from heapq import heappop, heappush     #heappop(heap) - usuwa i zwraca najmniejszy element z kopca; heappush(heap, element) - dodaje element do kopca
import time
import row_cols

class ASTR:
    def __init__(self, priority, state):
        self.state = state
        self.priority = priority

    def __lt__(self, second_board):    #porównujemy plansze po ich priorytecie(priorytet - funkcja f obliczana jako suma głębokości stanu i wartości heurystyki)
        return self.priority < second_board.priority
    
def astar(board, start, heuristic):

    max_recursion_depth = 0
    start_time = time.time()
    queue = list()
    processed_states = set()
    heappush(queue, ASTR(0, start))     #dodajemy stan początkowy do kolejki z priorytetem 0

    while queue:
        node = heappop(queue).state      #pobieramy stan z najniższym priorytetem
        max_recursion_depth = max(max_recursion_depth, node.depth)  
        if node not in processed_states:
            if board.solved(node):
                vs = len(queue) + len(processed_states)
                ps = len(processed_states)
                duration = time.time() - start_time
                return node, vs, ps, max_recursion_depth, duration

            processed_states.add(node)  #dodajemy stan do listy stanów przetworzonych
            for neighbours in board.neighbours(node):   #dla każdego sąsiada po kolei
                if neighbours not in processed_states:  #jeśli nie znajduje sie na liscie stanów przetworzonych
                    function_f = neighbours.depth + heuristic(neighbours)   #obliczamy funkcję f 
                    heappush(queue, ASTR(function_f, neighbours))   #i dodajemy do kolejki z odpowiednim priorytetem
    vs = len(queue) + len(processed_states)
    ps = len(processed_states)
    duration = time.time() - start_time
    return None, vs, ps, max_recursion_depth, duration

#poniższa heurystyka oblicza sumę odległości pomiędzy każdą cyfrą planszy a jej pozycją docelową
#na podstawie metryki Manhattan, czyli sumy różnic wartości współrzędnych wiersza i kolumny
def manhatan_heuristic(state):            #implementacja metryki manhatan
    distance = 0
    for i in range(row_cols.ROWS):
        for j in range(row_cols.COLUMNS):
            if state.board[i][j] != 0:
                goal_row, goal_column = divmod(state.board[i][j] - 1, row_cols.COLUMNS) #obliczamy docelową pozycję cyfry na podstawie wartości planszy
                distance_diff = abs(i - goal_row) + abs(j - goal_column)    #odległość Manhattan dla danego pola.
                distance = distance + distance_diff

    return  distance

#metryka Hamminga polega na obliczeniu ilosci pól, które nie znajdują się na swojej docelowej pozycji
def hamming_heuristic(state):       #implementacja metryki hamminga 
    distance = 0
    for i in range(row_cols.ROWS):
        for j in range(row_cols.COLUMNS):
            if state.board[i][j] != 0 and state.board[i][j] != row_cols.SOLVED_BOARD.board[i][j]:
                distance += 1
    return  distance