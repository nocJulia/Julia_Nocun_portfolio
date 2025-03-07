#Autorzy: Łukasz Gałuszewski 247660, Julia Nocuń 247744

import copy     #importujemy moduł copy do tworzenia głębokich kopii obiektów.
import row_cols

class Board:
    def __init__(self, board, parent, path, depth):
        self.board = board
        self.parent = parent
        self.path = path
        self.depth = depth

    def __hash__(self):
        return hash(str(self.board))

    def __eq__(self, second_board):                 #zgdonie z informacjami podanymi na laboratorium, gdy zmieniamy impelemnatcje funkcji haszującej
        return self.board == second_board.board     #zmieniamy również implementacje funkcji porównującej
        
def empty_field(state):
    empty_field = next(((i, j) for i, row in enumerate(state.board) for j, col in enumerate(row) if col == 0), None)
    return empty_field


def move(state, order): #funkcja wykonująca ruch na planszy

    #wykorzystujemy głęboką kopie tak aby mieć pewność, że bedziemy operować na całkiem odrębnych obiektach
    new_state = copy.deepcopy(state.board)
    current_depth = copy.deepcopy(state.depth)
    empty_row, empty_col = empty_field(state)   #znajdujemy pozycje pustego pola

    if order == "L":
        if empty_col > 0:
            new_state[empty_row][empty_col], new_state[empty_row][empty_col - 1] = new_state[empty_row][empty_col - 1], new_state[empty_row][empty_col]
        else:
            return None     #w przypadku gdy nie możemy wykonać danego ruchu funkcja zwraca None
    elif order == "R":
        if empty_col < row_cols.COLUMNS - 1:
            new_state[empty_row][empty_col], new_state[empty_row][empty_col + 1] = new_state[empty_row][empty_col + 1], new_state[empty_row][empty_col]
        else:
            return None
    elif order == "U":
        if empty_row > 0:
            new_state[empty_row][empty_col], new_state[empty_row - 1][empty_col] = new_state[empty_row - 1][empty_col], new_state[empty_row][empty_col]
        else:
            return None
    elif order == "D":
        if empty_row < row_cols.ROWS - 1:
            new_state[empty_row][empty_col], new_state[empty_row + 1][empty_col] = new_state[empty_row + 1][empty_col], new_state[empty_row][empty_col]
        else:
            return None

    return Board(new_state, state, order, current_depth + 1)    #funkcja zwraca nowy obiekt Board, który reprezentuje stan planszy po wykonaniu ruchu 
                                                                #(aktualizowane są wartości planszy, rodzica, ścieżki ruchu, głębokości)

def neighbours(state, order="RDUL"): #funkcja zwracająca sąsiadów aktualnego stanu planszy
    neighbour = []
    for direction in order:     #iterujemy przez wszystkie kierunki
        one_move = move(state, direction)   #pojedynczy ruch
        if one_move is not None:
            neighbour.append(one_move)  #stan planszy po wykonaniu ruchu dodawany jest do listy sąsiadów
    return neighbour


def solved(state):
    if row_cols.SOLVED_BOARD.__eq__(state):
        return True
    else:
        return False
    